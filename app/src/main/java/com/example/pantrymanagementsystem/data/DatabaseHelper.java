package com.example.pantrymanagementsystem.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pantrymanagementsystem.data.DatabaseContract.PantryItemEntry;
import com.example.pantrymanagementsystem.data.DatabaseContract.RecipeEntry;
import com.example.pantrymanagementsystem.data.DatabaseContract.RecipeIngredientEntry;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pantry_manager.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_RECIPES =
            "CREATE TABLE " + RecipeEntry.TABLE_NAME + " (" +
                    RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RecipeEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                    RecipeEntry.COLUMN_INSTRUCTIONS + " TEXT NOT NULL, " +
                    RecipeEntry.COLUMN_COOK_TIME_MINUTES + " INTEGER NOT NULL, " +
                    RecipeEntry.COLUMN_SERVINGS + " INTEGER NOT NULL)";

    private static final String SQL_CREATE_PANTRY_ITEMS =
            "CREATE TABLE " + PantryItemEntry.TABLE_NAME + " (" +
                    PantryItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PantryItemEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                    PantryItemEntry.COLUMN_QUANTITY + " REAL NOT NULL, " +
                    PantryItemEntry.COLUMN_UNIT + " TEXT NOT NULL, " +
                    PantryItemEntry.COLUMN_CATEGORY + " TEXT NOT NULL, " +
                    PantryItemEntry.COLUMN_EXPIRY_DATE + " INTEGER, " +
                    PantryItemEntry.COLUMN_DATE_ADDED + " INTEGER NOT NULL)";

    private static final String SQL_CREATE_RECIPE_INGREDIENTS =
            "CREATE TABLE " + RecipeIngredientEntry.TABLE_NAME + " (" +
                    RecipeIngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RecipeIngredientEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                    RecipeIngredientEntry.COLUMN_INGREDIENT_NAME + " TEXT NOT NULL, " +
                    RecipeIngredientEntry.COLUMN_REQUIRED_QUANTITY + " REAL NOT NULL, " +
                    RecipeIngredientEntry.COLUMN_UNIT + " TEXT NOT NULL, " +
                    "FOREIGN KEY (" + RecipeIngredientEntry.COLUMN_RECIPE_ID + ") REFERENCES " +
                    RecipeEntry.TABLE_NAME + "(" + RecipeEntry._ID + ") ON DELETE CASCADE)";

    private static final String SQL_DELETE_RECIPE_INGREDIENTS =
            "DROP TABLE IF EXISTS " + RecipeIngredientEntry.TABLE_NAME;
    private static final String SQL_DELETE_PANTRY_ITEMS =
            "DROP TABLE IF EXISTS " + PantryItemEntry.TABLE_NAME;
    private static final String SQL_DELETE_RECIPES =
            "DROP TABLE IF EXISTS " + RecipeEntry.TABLE_NAME;

    private static volatile DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_RECIPES);
        db.execSQL(SQL_CREATE_PANTRY_ITEMS);
        db.execSQL(SQL_CREATE_RECIPE_INGREDIENTS);

        // Seed initial recipes and matching pantry items into database on first app installation
        DatabaseSeeder.seedRecipes(db);
        DatabaseSeeder.seedPantryItems(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_RECIPE_INGREDIENTS);
        db.execSQL(SQL_DELETE_PANTRY_ITEMS);
        db.execSQL(SQL_DELETE_RECIPES);
        onCreate(db);
    }
}
