package com.example.pantrymanagementsystem.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.pantrymanagementsystem.data.dao.PantryItemDao;
import com.example.pantrymanagementsystem.data.dao.RecipeDao;
import com.example.pantrymanagementsystem.data.dao.RecipeIngredientDao;
import com.example.pantrymanagementsystem.data.entity.PantryItemEntity;
import com.example.pantrymanagementsystem.data.entity.RecipeEntity;
import com.example.pantrymanagementsystem.data.entity.RecipeIngredientEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Repository class that connects the UI screens to the background database operations
public class PantryRepository {

    public interface Callback<T> {
        void onComplete(T result);
    }

    public interface SuggestionCallback {
        void onComplete(List<RecipeEntity> recipes, Map<Long, List<RecipeIngredientEntity>> ingredientsMap);
    }

    private final PantryItemDao pantryItemDao;
    private final RecipeDao recipeDao;
    private final RecipeIngredientDao recipeIngredientDao;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private static volatile PantryRepository instance;

    public static PantryRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (PantryRepository.class) {
                if (instance == null) {
                    instance = new PantryRepository(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    private PantryRepository(Context context) {
        this.pantryItemDao = new PantryItemDao(context);
        this.recipeDao = new RecipeDao(context);
        this.recipeIngredientDao = new RecipeIngredientDao(context);
    }

    // Pantry Item Operations

    public void getAllPantryItems(Callback<List<PantryItemEntity>> callback) {
        executor.execute(() -> {
            List<PantryItemEntity> items = pantryItemDao.getAll();
            mainHandler.post(() -> callback.onComplete(items));
        });
    }

    public void getPantryItemById(long id, Callback<PantryItemEntity> callback) {
        executor.execute(() -> {
            PantryItemEntity item = pantryItemDao.getById(id);
            mainHandler.post(() -> callback.onComplete(item));
        });
    }

    public void insertPantryItem(PantryItemEntity item, Callback<Long> callback) {
        executor.execute(() -> {
            long newId = pantryItemDao.insert(item);
            mainHandler.post(() -> callback.onComplete(newId));
        });
    }

    public void updatePantryItem(PantryItemEntity item, Callback<Boolean> callback) {
        executor.execute(() -> {
            int rows = pantryItemDao.update(item);
            mainHandler.post(() -> callback.onComplete(rows > 0));
        });
    }

    public void deletePantryItem(long id, Callback<Boolean> callback) {
        executor.execute(() -> {
            int rows = pantryItemDao.delete(id);
            mainHandler.post(() -> callback.onComplete(rows > 0));
        });
    }

    // Recipe Operations

    public void getAllRecipes(Callback<List<RecipeEntity>> callback) {
        executor.execute(() -> {
            List<RecipeEntity> recipes = recipeDao.getAll();
            mainHandler.post(() -> callback.onComplete(recipes));
        });
    }

    public void getRecipeById(long recipeId, Callback<RecipeEntity> callback) {
        executor.execute(() -> {
            RecipeEntity recipe = recipeDao.getById(recipeId);
            mainHandler.post(() -> callback.onComplete(recipe));
        });
    }

    public void getIngredientsForRecipe(long recipeId, Callback<List<RecipeIngredientEntity>> callback) {
        executor.execute(() -> {
            List<RecipeIngredientEntity> ingredients = recipeIngredientDao.getForRecipe(recipeId);
            mainHandler.post(() -> callback.onComplete(ingredients));
        });
    }

    public void insertRecipe(RecipeEntity recipe, List<RecipeIngredientEntity> ingredients, Callback<Long> callback) {
        executor.execute(() -> {
            long recipeId = recipeDao.insert(recipe);
            if (recipeId > 0 && ingredients != null) {
                for (RecipeIngredientEntity ing : ingredients) {
                    ing.setRecipeId(recipeId);
                }
                recipeIngredientDao.insertAll(ingredients);
            }
            mainHandler.post(() -> callback.onComplete(recipeId));
        });
    }

    public void updateRecipe(RecipeEntity recipe, List<RecipeIngredientEntity> ingredients, Callback<Boolean> callback) {
        executor.execute(() -> {
            int rows = recipeDao.update(recipe);
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

    // Strict Recipe Matching and Meal Suggestions

    public void getSuggestedRecipes(SuggestionCallback callback) {
        executor.execute(() -> {
            List<PantryItemEntity> pantry = pantryItemDao.getAll();
            List<RecipeEntity> allRecipes = recipeDao.getAll();
            List<RecipeEntity> suggestedRecipes = new ArrayList<>();
            Map<Long, List<RecipeIngredientEntity>> resultMap = new HashMap<>();

            for (RecipeEntity recipe : allRecipes) {
                List<RecipeIngredientEntity> ingredients = recipeIngredientDao.getForRecipe(recipe.getId());
                if (ingredients.isEmpty()) {
                    continue;
                }

                boolean canMake = true;

                for (RecipeIngredientEntity req : ingredients) {
                    boolean ingredientMatch = false;

                    for (PantryItemEntity item : pantry) {
                        if (item.getQuantity() > 0 && isIngredientMatch(item.getName(), req.getIngredientName())) {
                            ingredientMatch = true;
                            break;
                        }
                    }

                    if (!ingredientMatch) {
                        canMake = false;
                        break;
                    }
                }

                if (canMake) {
                    suggestedRecipes.add(recipe);
                    resultMap.put(recipe.getId(), ingredients);
                }
            }

            mainHandler.post(() -> callback.onComplete(suggestedRecipes, resultMap));
        });
    }

    private boolean isIngredientMatch(String pantryItemName, String requiredName) {
        if (pantryItemName == null || requiredName == null) return false;

        String p = pantryItemName.replaceAll("[^a-zA-Z0-9\\s]", " ").replaceAll("\\s+", " ").trim().toLowerCase();
        String r = requiredName.replaceAll("[^a-zA-Z0-9\\s]", " ").replaceAll("\\s+", " ").trim().toLowerCase();

        if (p.isEmpty() || r.isEmpty()) return false;

        if (p.equals(r) || p.contains(r) || r.contains(p)) {
            return true;
        }

        String[] pWords = p.split(" ");
        String[] rWords = r.split(" ");

        for (String rWord : rWords) {
            if (rWord.length() <= 2) continue;
            for (String pWord : pWords) {
                if (pWord.equals(rWord) || pWord.contains(rWord) || rWord.contains(pWord)) {
                    return true;
                }
            }
        }

        return false;
    }
}
