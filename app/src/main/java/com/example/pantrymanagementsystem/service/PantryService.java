package com.example.pantrymanagementsystem.service;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.pantrymanagementsystem.data.dao.PantryItemDao;
import com.example.pantrymanagementsystem.data.dao.RecipeDao;
import com.example.pantrymanagementsystem.data.dao.RecipeIngredientDao;
import com.example.pantrymanagementsystem.data.entity.PantryItemEntity;
import com.example.pantrymanagementsystem.data.entity.RecipeEntity;
import com.example.pantrymanagementsystem.data.entity.RecipeIngredientEntity;
import com.example.pantrymanagementsystem.model.PantryItem;
import com.example.pantrymanagementsystem.model.Recipe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PantryService {

    public interface Callback<T> {
        void onComplete(T result);
    }

    public interface SuggestionCallback {
        void onComplete(List<Recipe> recipes, Map<Long, List<RecipeIngredientEntity>> ingredientsMap);
    }

    private final PantryItemDao pantryItemDao;
    private final RecipeDao recipeDao;
    private final RecipeIngredientDao recipeIngredientDao;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private static volatile PantryService instance;

    public static PantryService getInstance(Context context) {
        if (instance == null) {
            synchronized (PantryService.class) {
                if (instance == null) {
                    instance = new PantryService(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    private PantryService(Context context) {
        this.pantryItemDao = new PantryItemDao(context);
        this.recipeDao = new RecipeDao(context);
        this.recipeIngredientDao = new RecipeIngredientDao(context);
    }

    // Pantry Item Logic

    public void getAllPantryItems(Callback<List<PantryItem>> callback) {
        executor.execute(() -> {
            List<PantryItemEntity> entities = pantryItemDao.getAll();
            List<PantryItem> items = new ArrayList<>();
            for (PantryItemEntity entity : entities) {
                items.add(toGuiModel(entity));
            }
            mainHandler.post(() -> callback.onComplete(items));
        });
    }

    public void getPantryItemById(long id, Callback<PantryItem> callback) {
        executor.execute(() -> {
            PantryItemEntity entity = pantryItemDao.getById(id);
            PantryItem item = entity != null ? toGuiModel(entity) : null;
            mainHandler.post(() -> callback.onComplete(item));
        });
    }

    public void insertPantryItem(PantryItem item, Callback<Long> callback) {
        executor.execute(() -> {
            PantryItemEntity entity = toEntity(item);
            long newId = pantryItemDao.insert(entity);
            mainHandler.post(() -> callback.onComplete(newId));
        });
    }

    public void updatePantryItem(PantryItem item, Callback<Boolean> callback) {
        executor.execute(() -> {
            PantryItemEntity entity = toEntity(item);
            int rows = pantryItemDao.update(entity);
            mainHandler.post(() -> callback.onComplete(rows > 0));
        });
    }

    public void deletePantryItem(long id, Callback<Boolean> callback) {
        executor.execute(() -> {
            int rows = pantryItemDao.delete(id);
            mainHandler.post(() -> callback.onComplete(rows > 0));
        });
    }

    // Recipe Logic

    public void getAllRecipes(Callback<List<Recipe>> callback) {
        executor.execute(() -> {
            List<RecipeEntity> entities = recipeDao.getAll();
            List<Recipe> recipes = new ArrayList<>();
            for (RecipeEntity entity : entities) {
                recipes.add(toGuiModel(entity));
            }
            mainHandler.post(() -> callback.onComplete(recipes));
        });
    }

    public void getRecipeById(long recipeId, Callback<Recipe> callback) {
        executor.execute(() -> {
            RecipeEntity entity = recipeDao.getById(recipeId);
            Recipe recipe = entity != null ? toGuiModel(entity) : null;
            mainHandler.post(() -> callback.onComplete(recipe));
        });
    }

    public void getIngredientsForRecipe(long recipeId, Callback<List<RecipeIngredientEntity>> callback) {
        executor.execute(() -> {
            List<RecipeIngredientEntity> ingredients = recipeIngredientDao.getForRecipe(recipeId);
            mainHandler.post(() -> callback.onComplete(ingredients));
        });
    }

    public void insertRecipe(Recipe recipe, List<RecipeIngredientEntity> ingredients, Callback<Long> callback) {
        executor.execute(() -> {
            RecipeEntity entity = toEntity(recipe);
            long recipeId = recipeDao.insert(entity);
            if (recipeId > 0 && ingredients != null) {
                for (RecipeIngredientEntity ing : ingredients) {
                    ing.setRecipeId(recipeId);
                }
                recipeIngredientDao.insertAll(ingredients);
            }
            mainHandler.post(() -> callback.onComplete(recipeId));
        });
    }

    public void updateRecipe(Recipe recipe, List<RecipeIngredientEntity> ingredients, Callback<Boolean> callback) {
        executor.execute(() -> {
            RecipeEntity entity = toEntity(recipe);
            int rows = recipeDao.update(entity);
            recipeIngredientDao.deleteForRecipe(recipe.getId());
            if (ingredients != null) {
                for (RecipeIngredientEntity ing : ingredients) {
                    ing.setRecipeId(recipe.getId());
                }
                recipeIngredientDao.insertAll(ingredients);
            }
            mainHandler.post(() -> callback.onComplete(rows > 0));
        });
    }

    public void deleteRecipe(long recipeId, Callback<Boolean> callback) {
        executor.execute(() -> {
            int rows = recipeDao.delete(recipeId);
            mainHandler.post(() -> callback.onComplete(rows > 0));
        });
    }

    // Meal Suggestions Business Algorithm

    public void getSuggestedRecipes(SuggestionCallback callback) {
        executor.execute(() -> {
            List<PantryItemEntity> pantry = pantryItemDao.getAll();
            List<RecipeEntity> allRecipes = recipeDao.getAll();
            List<Recipe> suggestedRecipes = new ArrayList<>();
            Map<Long, List<RecipeIngredientEntity>> resultMap = new HashMap<>();

            for (RecipeEntity recipeEntity : allRecipes) {
                List<RecipeIngredientEntity> ingredients = recipeIngredientDao.getForRecipe(recipeEntity.getId());
                if (ingredients.isEmpty()) continue;

                boolean canMake = true;
                for (RecipeIngredientEntity req : ingredients) {
                    boolean matched = false;
                    for (PantryItemEntity item : pantry) {
                        if (item.getQuantity() > 0 && isIngredientMatch(item.getName(), req.getIngredientName())) {
                            matched = true;
                            break;
                        }
                    }
                    if (!matched) {
                        canMake = false;
                        break;
                    }
                }

                if (canMake) {
                    suggestedRecipes.add(toGuiModel(recipeEntity));
                    resultMap.put(recipeEntity.getId(), ingredients);
                }
            }

            mainHandler.post(() -> callback.onComplete(suggestedRecipes, resultMap));
        });
    }

    private boolean isIngredientMatch(String pantryName, String requiredName) {
        if (pantryName == null || requiredName == null) return false;

        String p = pantryName.replaceAll("[^a-zA-Z0-9\\s]", " ").trim().toLowerCase();
        String r = requiredName.replaceAll("[^a-zA-Z0-9\\s]", " ").trim().toLowerCase();

        if (p.isEmpty() || r.isEmpty()) return false;
        if (p.equalsIgnoreCase(r) || p.contains(r) || r.contains(p)) return true;

        for (String rWord : r.split("\\s+")) {
            if (rWord.length() <= 2) continue;
            if (p.contains(rWord)) return true;
        }
        return false;
    }

    // Entity <-> GUI Mappers

    private PantryItem toGuiModel(PantryItemEntity entity) {
        if (entity == null) return null;
        String expiryStr = null;
        if (entity.getExpiryDate() != null) {
            expiryStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(entity.getExpiryDate()));
        }
        return new PantryItem(
                entity.getId(),
                entity.getName(),
                (int) entity.getQuantity(),
                entity.getUnit(),
                entity.getCategory(),
                expiryStr
        );
    }

    private PantryItemEntity toEntity(PantryItem model) {
        if (model == null) return null;
        Long expiryMillis = null;
        if (model.getExpiryDate() != null && !model.getExpiryDate().trim().isEmpty()) {
            try {
                Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(model.getExpiryDate().trim());
                if (d != null) expiryMillis = d.getTime();
            } catch (Exception ignored) {
            }
        }
        PantryItemEntity entity = new PantryItemEntity(
                model.getName(),
                model.getQuantity(),
                model.getSize() != null && !model.getSize().trim().isEmpty() ? model.getSize() : "pcs",
                model.getCategory() != null ? model.getCategory() : "Other",
                expiryMillis,
                System.currentTimeMillis()
        );
        entity.setId(model.getId());
        return entity;
    }

    private Recipe toGuiModel(RecipeEntity entity) {
        if (entity == null) return null;
        return new Recipe(
                entity.getId(),
                entity.getName(),
                entity.getInstructions(),
                entity.getCookTimeMinutes(),
                entity.getServings()
        );
    }

    private RecipeEntity toEntity(Recipe model) {
        if (model == null) return null;
        RecipeEntity entity = new RecipeEntity(
                model.getName(),
                model.getInstructions(),
                model.getCookTimeMinutes(),
                model.getServings()
        );
        entity.setId(model.getId());
        return entity;
    }
}
