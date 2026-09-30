# Codebase Architecture & File Explanation

This document provides a comprehensive explanation of every file in the **Pantry Management System** Android project. The project follows a clean **3-Tier Architecture**:

```
+-------------------------------------------------------------+
|                         GUI LAYER                           |
|  Activities, Adapters, UI Models (PantryItem, Recipe), UI   |
+------------------------------+------------------------------+
                               |
                               v
+-------------------------------------------------------------+
|                     BUSINESS LOGIC LAYER                    |
|  PantryService, Meal Matching Rules, Mappers, Threading     |
+------------------------------+------------------------------+
                               |
                               v
+-------------------------------------------------------------+
|                        DATABASE LAYER                       |
|  DatabaseHelper, Contract, DAOs, Database Entities          |
+-------------------------------------------------------------+
```

---

## 1. GUI / Presentation Layer

This layer handles screen layouts, user inputs, event listeners, and displaying data using dedicated presentation models. It has zero coupling to raw database entities or SQLite queries.

### Activities
* **[MainActivity.java](file:///C:/Users/lerato_signa/StudioProjects/Panty_Management_Sytem/app/src/main/java/com/example/pantrymanagementsystem/MainActivity.java)**
  * **Purpose:** The main dashboard screen. Displays pantry statistics (total items, total recipes), lists all current pantry items in a `RecyclerView`, handles item deletion/editing, and provides navigation buttons to add items, view recipes, or generate meal suggestions.
* **[AddEditItemActivity.java](file:///C:/Users/lerato_signa/StudioProjects/Panty_Management_Sytem/app/src/main/java/com/example/pantrymanagementsystem/AddEditItemActivity.java)**
  * **Purpose:** Form screen for creating a new pantry item or updating an existing item (name, quantity, size/unit, category dropdown spinner, and date picker dialog for expiry date).
* **[AddRecipeActivity.java](file:///C:/Users/lerato_signa/StudioProjects/Panty_Management_Sytem/app/src/main/java/com/example/pantrymanagementsystem/AddRecipeActivity.java)**
  * **Purpose:** Form screen for adding a new recipe or editing an existing recipe (recipe title, multiline ingredient list, preparation steps, cook time in minutes, and servings count).
* **[RecipeListActivity.java](file:///C:/Users/lerato_signa/StudioProjects/Panty_Management_Sytem/app/src/main/java/com/example/pantrymanagementsystem/RecipeListActivity.java)**
  * **Purpose:** Screen that lists all saved recipes along with their required ingredients, cook time, servings, and instructions. Supports edit and delete actions.
* **[MealSuggestionsActivity.java](file:///C:/Users/lerato_signa/StudioProjects/Panty_Management_Sytem/app/src/main/java/com/example/pantrymanagementsystem/MealSuggestionsActivity.java)**
  * **Purpose:** Screen displaying recipes that can be prepared using the items currently available in the user's pantry.

### Adapters & UI Utilities
* **[PantryAdapter.java](file:///C:/Users/lerato_signa/StudioProjects/Panty_Management_Sytem/app/src/main/java/com/example/pantrymanagementsystem/adapter/PantryAdapter.java)**
  * **Purpose:** `RecyclerView.Adapter` that binds `PantryItem` UI models to `item_pantry.xml` card items. Configures category-based avatar colors and emojis, item details formatting, and item action callbacks (edit/delete).
* **[RecipeAdapter.java](file:///C:/Users/lerato_signa/StudioProjects/Panty_Management_Sytem/app/src/main/java/com/example/pantrymanagementsystem/adapter/RecipeAdapter.java)**
  * **Purpose:** `RecyclerView.Adapter` that binds `Recipe` UI models and associated ingredient lists to `item_recipe.xml` card items.
* **[UiUtils.java](file:///C:/Users/lerato_signa/StudioProjects/Panty_Management_Sytem/app/src/main/java/com/example/pantrymanagementsystem/util/UiUtils.java)**
  * **Purpose:** Utility class providing `applyEdgeToEdge()` to handle window cutout and system bar inset paddings consistently across all Activity layouts.

### GUI Models
* **[PantryItem.java](file:///C:/Users/lerato_signa/StudioProjects/Panty_Management_Sytem/app/src/main/java/com/example/pantrymanagementsystem/model/PantryItem.java)**
  * **Purpose:** Presentation model representing a pantry item tailored for UI rendering (e.g., formatted expiry string `yyyy-MM-dd`, size/unit representation).
* **[Recipe.java](file:///C:/Users/lerato_signa/StudioProjects/Panty_Management_Sytem/app/src/main/java/com/example/pantrymanagementsystem/model/Recipe.java)**
  * **Purpose:** Presentation model representing a recipe and its ingredients tailored for UI views.

---

## 2. Business Logic Layer

This layer contains business rules, algorithms, data conversion mappers, and background thread orchestration.

* **[PantryService.java](file:///C:/Users/lerato_signa/StudioProjects/Panty_Management_Sytem/app/src/main/java/com/example/pantrymanagementsystem/service/PantryService.java)**
  * **Purpose:** The central service mediator connecting GUI screens to the database. Responsibilities include:
    * Executing database queries on a background thread (`ExecutorService`) and delivering results back to the main UI thread (`Handler`).
    * Converting between GUI presentation models (`PantryItem`, `Recipe`) and database entities (`PantryItemEntity`, `RecipeEntity`, `RecipeIngredientEntity`).
    * Running the **Meal Suggestion Algorithm**: compares required recipe ingredients with available pantry stock using normalized string containment matching (`isIngredientMatch`).

---

## 3. Database / Persistence Layer

This layer manages SQLite database creation, schema contracts, tables, Data Access Objects (DAOs), and persistence entities.

### Schema & Helper
* **[DatabaseContract.java](file:///C:/Users/lerato_signa/StudioProjects/Panty_Management_Sytem/app/src/main/java/com/example/pantrymanagementsystem/data/DatabaseContract.java)**
  * **Purpose:** Defines constant column names, table names (`pantry_items`, `recipes`, `recipe_ingredients`), and primary key definitions for SQLite database tables.
* **[DatabaseHelper.java](file:///C:/Users/lerato_signa/StudioProjects/Panty_Management_Sytem/app/src/main/java/com/example/pantrymanagementsystem/data/DatabaseHelper.java)**
  * **Purpose:** `SQLiteOpenHelper` singleton implementation managing the database file `pantry_manager.db`. Handles table creation SQL statements, database version upgrades, and enables foreign key CASCADE deletion constraints.

### Data Access Objects (DAOs)
* **[PantryItemDao.java](file:///C:/Users/lerato_signa/StudioProjects/Panty_Management_Sytem/app/src/main/java/com/example/pantrymanagementsystem/data/dao/PantryItemDao.java)**
  * **Purpose:** Executes SQL queries (`insert`, `getById`, `getAll`, `search`, `update`, `delete`) for `PantryItemEntity` records.
* **[RecipeDao.java](file:///C:/Users/lerato_signa/StudioProjects/Panty_Management_Sytem/app/src/main/java/com/example/pantrymanagementsystem/data/dao/RecipeDao.java)**
  * **Purpose:** Executes SQL queries (`insert`, `getById`, `getAll`, `update`, `delete`) for `RecipeEntity` records.
* **[RecipeIngredientDao.java](file:///C:/Users/lerato_signa/StudioProjects/Panty_Management_Sytem/app/src/main/java/com/example/pantrymanagementsystem/data/dao/RecipeIngredientDao.java)**
  * **Purpose:** Executes SQL queries and batch transactions (`insertAll`, `getForRecipe`, `deleteForRecipe`) for `RecipeIngredientEntity` records linked to recipes.

### Persistence Entities
* **[PantryItemEntity.java](file:///C:/Users/lerato_signa/StudioProjects/Panty_Management_Sytem/app/src/main/java/com/example/pantrymanagementsystem/data/entity/PantryItemEntity.java)**
  * **Purpose:** Low-level data object matching the `pantry_items` table schema (stores expiry date as Epoch timestamp milliseconds `Long`).
* **[RecipeEntity.java](file:///C:/Users/lerato_signa/StudioProjects/Panty_Management_Sytem/app/src/main/java/com/example/pantrymanagementsystem/data/entity/RecipeEntity.java)**
  * **Purpose:** Low-level data object matching the `recipes` table schema.
* **[RecipeIngredientEntity.java](file:///C:/Users/lerato_signa/StudioProjects/Panty_Management_Sytem/app/src/main/java/com/example/pantrymanagementsystem/data/entity/RecipeIngredientEntity.java)**
  * **Purpose:** Low-level data object matching the `recipe_ingredients` table schema, with `recipeId` foreign key.

---

## 4. UI Layouts & XML Resources (`app/src/main/res/`)

### Layout Files (`res/layout/`)
* **`activity_main.xml`:** Layout for the main dashboard containing top header stats, empty state view, pantry `RecyclerView`, and FABs.
* **`activity_add_edit_item.xml`:** Layout for the item add/edit screen with TextInput fields and category spinner.
* **`activity_add_recipe.xml`:** Layout for the recipe add/edit screen with inputs for cook time, servings, ingredients, and preparation steps.
* **`activity_recipe_list.xml`:** Layout for displaying saved recipes in a `RecyclerView`.
* **`activity_meal_suggestions.xml`:** Layout for displaying suggested recipes in a `RecyclerView`.
* **`item_pantry.xml`:** Layout for a single pantry item card inside the pantry `RecyclerView`.
* **`item_recipe.xml`:** Layout for a single recipe card inside the recipe `RecyclerView`.

### Values & Styles (`res/values/`)
* **`colors.xml`:** Palette definitions for primary, surface, background, text, and category pill colors.
* **`strings.xml`:** Application string resources.
* **`themes.xml` & `values-night/themes.xml`:** Material Design theme definitions and system bar configurations.

### Drawables (`res/drawable/`)
* Vector drawables (`ic_add.xml`, `ic_edit.xml`, `ic_delete.xml`, `ic_pantry.xml`, `ic_recipes.xml`, `ic_sparkle.xml`, `ic_calendar.xml`, `ic_timer.xml`, `ic_scale.xml`, `ic_check.xml`, `ic_arrow_back.xml`, `ic_arrow_forward.xml`, `ic_empty_box.xml`, `ic_empty_recipe.xml`, `ic_people.xml`, `ic_restaurant.xml`) for UI icons.
* Shape drawables (`bg_header_curved.xml`, `bg_action_card_recipes.xml`, `bg_action_card_suggest.xml`, `bg_pill_primary.xml`, `bg_stat_badge.xml`, etc.) for card and background styling.

---

## 5. App Manifest & Build Configuration

* **[AndroidManifest.xml](file:///C:/Users/lerato_signa/StudioProjects/Panty_Management_Sytem/app/src/main/AndroidManifest.xml)**
  * **Purpose:** Android system manifest declaring the application package, app theme, backup rules, launcher activity (`MainActivity`), and soft input configurations for form activities.
* **[build.gradle.kts (App)](file:///C:/Users/lerato_signa/StudioProjects/Panty_Management_Sytem/app/build.gradle.kts)**
  * **Purpose:** App module Gradle script setting compile SDK targets, target SDK 37, minimum SDK 25, Java 11 compatibility, and dependencies (`AppCompat`, `Material`, `ConstraintLayout`, `Activity-KTX`).
* **[build.gradle.kts (Root)](file:///C:/Users/lerato_signa/StudioProjects/Panty_Management_Sytem/build.gradle.kts) & [settings.gradle.kts](file:///C:/Users/lerato_signa/StudioProjects/Panty_Management_Sytem/settings.gradle.kts)**
  * **Purpose:** Root Gradle build configuration, plugin management, repository definitions, and module inclusion (`:app`).
