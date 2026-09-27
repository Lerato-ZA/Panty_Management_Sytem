package com.example.pantrymanagementsystem.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pantrymanagementsystem.data.DatabaseContract.RecipeIngredientEntry;
import com.example.pantrymanagementsystem.data.DatabaseHelper;
import com.example.pantrymanagementsystem.data.entity.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientDao {

    private final DatabaseHelper dbHelper;

    public RecipeIngredientDao(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    public long insert(RecipeIngredient ingredient) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.insert(RecipeIngredientEntry.TABLE_NAME, null, toContentValues(ingredient));
    }

    /** Inserts a batch inside a single transaction (e.g. all ingredients for one new recipe). */
    public void insertAll(List<RecipeIngredient> ingredients) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (RecipeIngredient ingredient : ingredients) {
                db.insert(RecipeIngredientEntry.TABLE_NAME, null, toContentValues(ingredient));
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public List<RecipeIngredient> getForRecipe(long recipeId) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = RecipeIngredientEntry.COLUMN_RECIPE_ID + " = ?";
        String[] args = { String.valueOf(recipeId) };
        try (Cursor cursor = db.query(RecipeIngredientEntry.TABLE_NAME, null,
                selection, args, null, null, null)) {
            while (cursor.moveToNext()) {
                ingredients.add(fromCursor(cursor));
            }
        }
        return ingredients;
    }

    public List<RecipeIngredient> getAll() {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try (Cursor cursor = db.query(RecipeIngredientEntry.TABLE_NAME, null,
                null, null, null, null, null)) {
            while (cursor.moveToNext()) {
                ingredients.add(fromCursor(cursor));
            }
        }
        return ingredients;
    }

    public int update(RecipeIngredient ingredient) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = RecipeIngredientEntry._ID + " = ?";
        String[] args = { String.valueOf(ingredient.getId()) };
        return db.update(RecipeIngredientEntry.TABLE_NAME, toContentValues(ingredient), selection, args);
    }

    public int delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = RecipeIngredientEntry._ID + " = ?";
        String[] args = { String.valueOf(id) };
        return db.delete(RecipeIngredientEntry.TABLE_NAME, selection, args);
    }

    private ContentValues toContentValues(RecipeIngredient ingredient) {
        ContentValues values = new ContentValues();
        values.put(RecipeIngredientEntry.COLUMN_RECIPE_ID, ingredient.getRecipeId());
        values.put(RecipeIngredientEntry.COLUMN_INGREDIENT_NAME, ingredient.getIngredientName());
        values.put(RecipeIngredientEntry.COLUMN_REQUIRED_QUANTITY, ingredient.getRequiredQuantity());
        values.put(RecipeIngredientEntry.COLUMN_UNIT, ingredient.getUnit());
        return values;
    }

    private RecipeIngredient fromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(RecipeIngredientEntry._ID));
        long recipeId = cursor.getLong(cursor.getColumnIndexOrThrow(RecipeIngredientEntry.COLUMN_RECIPE_ID));
        String ingredientName = cursor.getString(cursor.getColumnIndexOrThrow(RecipeIngredientEntry.COLUMN_INGREDIENT_NAME));
        double requiredQuantity = cursor.getDouble(cursor.getColumnIndexOrThrow(RecipeIngredientEntry.COLUMN_REQUIRED_QUANTITY));
        String unit = cursor.getString(cursor.getColumnIndexOrThrow(RecipeIngredientEntry.COLUMN_UNIT));

        RecipeIngredient ingredient = new RecipeIngredient(recipeId, ingredientName, requiredQuantity, unit);
        ingredient.setId(id);
        return ingredient;
    }
}