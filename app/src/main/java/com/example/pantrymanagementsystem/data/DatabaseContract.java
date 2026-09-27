package com.example.pantrymanagementsystem.data;

import android.provider.BaseColumns;

public final class DatabaseContract {

    private DatabaseContract() {}

    public static class PantryItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "pantry_items";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_UNIT = "unit";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_EXPIRY_DATE = "expiry_date";   // nullable, epoch millis
        public static final String COLUMN_DATE_ADDED = "date_added";     // epoch millis
    }

    public static class RecipeEntry implements BaseColumns {
        public static final String TABLE_NAME = "recipes";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_INSTRUCTIONS = "instructions";
        public static final String COLUMN_COOK_TIME_MINUTES = "cook_time_minutes";
        public static final String COLUMN_SERVINGS = "servings";
    }

    public static class RecipeIngredientEntry implements BaseColumns {
        public static final String TABLE_NAME = "recipe_ingredients";
        public static final String COLUMN_RECIPE_ID = "recipe_id";           // FK -> recipes._id
        public static final String COLUMN_INGREDIENT_NAME = "ingredient_name"; // matched vs pantry_items.name
        public static final String COLUMN_REQUIRED_QUANTITY = "required_quantity";
        public static final String COLUMN_UNIT = "unit";
    }
}
