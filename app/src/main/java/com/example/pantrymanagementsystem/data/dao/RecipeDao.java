package com.example.pantrymanagementsystem.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pantrymanagementsystem.data.DatabaseContract.RecipeEntry;
import com.example.pantrymanagementsystem.data.DatabaseHelper;
import com.example.pantrymanagementsystem.data.entity.RecipeEntity;

import java.util.ArrayList;
import java.util.List;

public class RecipeDao {

    private final DatabaseHelper dbHelper;

    public RecipeDao(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    public long insert(RecipeEntity recipe) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.insert(RecipeEntry.TABLE_NAME, null, toContentValues(recipe));
    }

    public RecipeEntity getById(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = RecipeEntry._ID + " = ?";
        String[] args = { String.valueOf(id) };
        try (Cursor cursor = db.query(RecipeEntry.TABLE_NAME, null,
                selection, args, null, null, null)) {
            return cursor.moveToFirst() ? fromCursor(cursor) : null;
        }
    }

    public List<RecipeEntity> getAll() {
        List<RecipeEntity> recipes = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String orderBy = RecipeEntry.COLUMN_NAME + " ASC";
        try (Cursor cursor = db.query(RecipeEntry.TABLE_NAME, null,
                null, null, null, null, orderBy)) {
            while (cursor.moveToNext()) {
                recipes.add(fromCursor(cursor));
            }
        }
        return recipes;
    }

    public int update(RecipeEntity recipe) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = RecipeEntry._ID + " = ?";
        String[] args = { String.valueOf(recipe.getId()) };
        return db.update(RecipeEntry.TABLE_NAME, toContentValues(recipe), selection, args);
    }

    public int delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = RecipeEntry._ID + " = ?";
        String[] args = { String.valueOf(id) };
        return db.delete(RecipeEntry.TABLE_NAME, selection, args);
    }

    private ContentValues toContentValues(RecipeEntity recipe) {
        ContentValues values = new ContentValues();
        values.put(RecipeEntry.COLUMN_NAME, recipe.getName());
        values.put(RecipeEntry.COLUMN_INSTRUCTIONS, recipe.getInstructions());
        values.put(RecipeEntry.COLUMN_COOK_TIME_MINUTES, recipe.getCookTimeMinutes());
        values.put(RecipeEntry.COLUMN_SERVINGS, recipe.getServings());
        return values;
    }

    private RecipeEntity fromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(RecipeEntry._ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(RecipeEntry.COLUMN_NAME));
        String instructions = cursor.getString(cursor.getColumnIndexOrThrow(RecipeEntry.COLUMN_INSTRUCTIONS));
        int cookTime = cursor.getInt(cursor.getColumnIndexOrThrow(RecipeEntry.COLUMN_COOK_TIME_MINUTES));
        int servings = cursor.getInt(cursor.getColumnIndexOrThrow(RecipeEntry.COLUMN_SERVINGS));

        RecipeEntity recipe = new RecipeEntity(name, instructions, cookTime, servings);
        recipe.setId(id);
        return recipe;
    }
}
