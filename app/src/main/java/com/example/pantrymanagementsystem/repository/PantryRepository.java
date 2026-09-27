package com.example.pantrymanagementsystem.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.pantrymanagementsystem.data.dao.PantryItemDao;
import com.example.pantrymanagementsystem.data.dao.RecipeDao;
import com.example.pantrymanagementsystem.data.dao.RecipeIngredientDao;
import com.example.pantrymanagementsystem.data.entity.PantryItem;
import com.example.pantrymanagementsystem.data.entity.Recipe;
import com.example.pantrymanagementsystem.data.entity.RecipeIngredient;

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
        void onComplete(List<Recipe> recipes, Map<Long, List<RecipeIngredient>> ingredientsMap);
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

    public void getAllPantryItems(Callback<List<PantryItem>> callback) {
        executor.execute(() -> {
            List<PantryItem> items = pantryItemDao.getAll();
            mainHandler.post(() -> callback.onComplete(items));
        });
    }

    public void getPantryItemById(long id, Callback<PantryItem> callback) {
        executor.execute(() -> {
            PantryItem item = pantryItemDao.getById(id);
            mainHandler.post(() -> callback.onComplete(item));
        });
    }

    public void insertPantryItem(PantryItem item, Callback<Long> callback) {
        executor.execute(() -> {
            long newId = pantryItemDao.insert(item);
            mainHandler.post(() -> callback.onComplete(newId));
        });
    }

    public void updatePantryItem(PantryItem item, Callback<Boolean> callback) {
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

    public void searchPantryItems(String query, Callback<List<PantryItem>> callback) {
        executor.execute(() -> {
            List<PantryItem> items = pantryItemDao.search(query);
            mainHandler.post(() -> callback.onComplete(items));
        });
    }

    // Recipe Operations

    public void getAllRecipes(Callback<List<Recipe>> callback) {
        executor.execute(() -> {
            List<Recipe> recipes = recipeDao.getAll();
            mainHandler.post(() -> callback.onComplete(recipes));
        });
    }

    public void getRecipeById(long recipeId, Callback<Recipe> callback) {
        executor.execute(() -> {
            Recipe recipe = recipeDao.getById(recipeId);
            mainHandler.post(() -> callback.onComplete(recipe));
        });
    }

    public void getIngredientsForRecipe(long recipeId, Callback<List<RecipeIngredient>> callback) {
        executor.execute(() -> {
            List<RecipeIngredient> ingredients = recipeIngredientDao.getForRecipe(recipeId);
            mainHandler.post(() -> callback.onComplete(ingredients));
        });
    }

    public void insertRecipe(Recipe recipe, List<RecipeIngredient> ingredients, Callback<Long> callback) {
        executor.execute(() -> {
            long recipeId = recipeDao.insert(recipe);
            if (recipeId > 0 && ingredients != null) {
                for (RecipeIngredient ing : ingredients) {
                    ing.setRecipeId(recipeId);
                }
                recipeIngredientDao.insertAll(ingredients);
            }
            mainHandler.post(() -> callback.onComplete(recipeId));
        });
    }

    public void updateRecipe(Recipe recipe, List<RecipeIngredient> ingredients, Callback<Boolean> callback) {
        executor.execute(() -> {
            int rows = recipeDao.update(recipe);
            recipeIngredientDao.deleteForRecipe(recipe.getId());
            if (ingredients != null) {
                for (RecipeIngredient ing : ingredients) {
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
            List<PantryItem> pantry = pantryItemDao.getAll();
            List<Recipe> allRecipes = recipeDao.getAll();
            List<Recipe> suggestedRecipes = new ArrayList<>();
            Map<Long, List<RecipeIngredient>> resultMap = new HashMap<>();

            for (Recipe recipe : allRecipes) {
                List<RecipeIngredient> ingredients = recipeIngredientDao.getForRecipe(recipe.getId());
                if (ingredients.isEmpty()) {
                    continue;
                }

                boolean canMake = true;

                for (RecipeIngredient req : ingredients) {
                    boolean ingredientMatch = false;

                    for (PantryItem item : pantry) {
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

        // Clean punctuation and normalize
        String p = pantryItemName.replaceAll("[^a-zA-Z0-9\\s]", " ").replaceAll("\\s+", " ").trim().toLowerCase();
        String r = requiredName.replaceAll("[^a-zA-Z0-9\\s]", " ").replaceAll("\\s+", " ").trim().toLowerCase();

        if (p.isEmpty() || r.isEmpty()) return false;

        if (p.equals(r) || p.contains(r) || r.contains(p)) {
            return true;
        }

        String[] pWords = p.split(" ");
        String[] rWords = r.split(" ");

        for (String rWord : rWords) {
            if (rWord.length() <= 2) continue; // skip short words like "of", "in"
            for (String pWord : pWords) {
                if (pWord.equals(rWord) || pWord.contains(rWord) || rWord.contains(pWord)) {
                    return true;
                }
            }
        }

        return false;
    }
}
