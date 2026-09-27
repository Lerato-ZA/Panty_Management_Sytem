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

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PantryRepository {

    public interface Callback<T> {
        void onComplete(T result);
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

    //  Pantry Item Operations

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

    public void updateRecipe(Recipe recipe, Callback<Boolean> callback) {
        executor.execute(() -> {
            int rows = recipeDao.update(recipe);
            mainHandler.post(() -> callback.onComplete(rows > 0));
        });
    }

    public void deleteRecipe(long recipeId, Callback<Boolean> callback) {
        executor.execute(() -> {
            int rows = recipeDao.delete(recipeId);
            mainHandler.post(() -> callback.onComplete(rows > 0));
        });
    }
}
