package com.example.pantrymanagementsystem.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.pantrymanagementsystem.data.DatabaseContract.PantryItemEntry;
import com.example.pantrymanagementsystem.data.DatabaseContract.RecipeEntry;
import com.example.pantrymanagementsystem.data.DatabaseContract.RecipeIngredientEntry;

// Populates the database with initial recipe collections and pantry items on first app installation
public class DatabaseSeeder {

    public static void seedPantryItems(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            // Ingredients for Pap and Wors (Tomato-Gravy)
            insertPantryItem(db, "Mielie meal", 2.0, "2 kg", "Grains", null);
            insertPantryItem(db, "Water", 10.0, "5 L", "Beverages", null);
            insertPantryItem(db, "Oil", 4.0, "1 L", "Other", null);
            insertPantryItem(db, "Onion", 6.0, "6 pcs", "Vegetables", null);
            insertPantryItem(db, "Chilli flakes", 1.0, "1 pack", "Spices", null);
            insertPantryItem(db, "Chopped tomatoes", 4.0, "400 g cans", "Canned Goods", null);
            insertPantryItem(db, "Tomato sauce", 1.0, "1 bottle", "Canned Goods", null);
            insertPantryItem(db, "Worcestershire sauce", 1.0, "1 bottle", "Canned Goods", null);
            insertPantryItem(db, "Brown sugar", 1.0, "1 kg", "Other", null);
            insertPantryItem(db, "Boerewors", 4.0, "4 pcs", "Meat", null);

            // Ingredients for Mac and Cheese
            insertPantryItem(db, "Elbow macaroni", 400.0, "400 g", "Grains", null);
            insertPantryItem(db, "Butter", 500.0, "500 g", "Dairy", null);
            insertPantryItem(db, "Flour", 2.0, "2 kg", "Grains", null);
            insertPantryItem(db, "Milk", 1.0, "1 L", "Dairy", null);
            insertPantryItem(db, "Cheddar cheese", 300.0, "300 g", "Dairy", null);
            insertPantryItem(db, "Gruyere cheese", 100.0, "100 g", "Dairy", null);

            // Additional ingredients for Chicken Curry & Egg Fried Rice
            insertPantryItem(db, "Chicken pieces", 500.0, "500 g", "Meat", null);
            insertPantryItem(db, "Curry powder", 1.0, "1 pack", "Spices", null);
            insertPantryItem(db, "Rice", 2.0, "2 kg", "Grains", null);
            insertPantryItem(db, "Eggs", 6.0, "6 pcs", "Dairy", null);
            insertPantryItem(db, "Garlic", 5.0, "5 cloves", "Vegetables", null);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public static void seedRecipes(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            // Recipe 1: Pap and Wors (Tomato-Gravy)
            long r1 = insertRecipe(db, "Pap and Wors (Tomato-Gravy)",
                    "1. Pap: Bring 4 cups water + 1 tbsp salt to a boil. Add 2 cups mielie meal, stir until thick. Reduce heat, cover, simmer 30 min.\n" +
                    "2. Tomato-Gravy: Heat 4 tbsp oil. Sauté 2 diced onions + 1 tsp chilli flakes (8 min). Add 1 can chopped tomatoes, 3 tbsp tomato sauce, 2 tbsp Worcestershire sauce, 1 tbsp brown sugar, 1 cup water. Simmer 20-30 min.\n" +
                    "3. Wors: Grill or pan-fry boerewors over medium-high heat 10-15 min. Rest 5 min, then slice. Serve over pap with gravy.", 30, 4);
            insertIngredient(db, r1, "Mielie meal", 2.0, "cups");
            insertIngredient(db, r1, "Water", 5.0, "cups");
            insertIngredient(db, r1, "Oil", 4.0, "tbsp");
            insertIngredient(db, r1, "Onion", 2.0, "pcs");
            insertIngredient(db, r1, "Chilli flakes", 1.0, "tsp");
            insertIngredient(db, r1, "Chopped tomatoes", 400.0, "g");
            insertIngredient(db, r1, "Tomato sauce", 3.0, "tbsp");
            insertIngredient(db, r1, "Worcestershire sauce", 2.0, "tbsp");
            insertIngredient(db, r1, "Brown sugar", 1.0, "tbsp");
            insertIngredient(db, r1, "Boerewors", 4.0, "pcs");

            // Recipe 2: Mac and Cheese
            long r2 = insertRecipe(db, "Mac and Cheese",
                    "1. Boil 400 g elbow macaroni in salted water until al dente. Drain.\n" +
                    "2. Melt 30 g butter over medium heat. Whisk in 3 tbsp flour; cook 1 min.\n" +
                    "3. Slowly add 500 ml milk, whisking constantly until thickened (3-4 min).\n" +
                    "4. Stir in 300 g cheddar + 100 g Gruyere until melted. Toss with pasta.\n" +
                    "5. Bake at 180 C for 15-20 min until bubbling and golden.", 20, 4);
            insertIngredient(db, r2, "Elbow macaroni", 400.0, "g");
            insertIngredient(db, r2, "Butter", 30.0, "g");
            insertIngredient(db, r2, "Flour", 3.0, "tbsp");
            insertIngredient(db, r2, "Milk", 500.0, "ml");
            insertIngredient(db, r2, "Cheddar cheese", 300.0, "g");
            insertIngredient(db, r2, "Gruyere cheese", 100.0, "g");

            // Recipe 3: Spaghetti Bolognese
            long r3 = insertRecipe(db, "Spaghetti Bolognese",
                    "1. Cook 400 g spaghetti in salted water until al dente. Drain.\n" +
                    "2. Heat 2 tbsp oil. Sauté 1 diced onion + 2 minced garlic cloves until soft.\n" +
                    "3. Add 500 g ground beef; brown well.\n" +
                    "4. Stir in 2 tbsp tomato paste (cook 2 min). Add 1 can chopped tomatoes, 1 cup beef stock, 1 tsp sugar, oregano, salt, pepper.\n" +
                    "5. Simmer uncovered 30-40 min. Serve over spaghetti with parmesan.", 40, 4);
            insertIngredient(db, r3, "Spaghetti", 400.0, "g");
            insertIngredient(db, r3, "Oil", 2.0, "tbsp");
            insertIngredient(db, r3, "Onion", 1.0, "pcs");
            insertIngredient(db, r3, "Garlic", 2.0, "cloves");
            insertIngredient(db, r3, "Ground beef", 500.0, "g");
            insertIngredient(db, r3, "Tomato paste", 2.0, "tbsp");
            insertIngredient(db, r3, "Chopped tomatoes", 400.0, "g");
            insertIngredient(db, r3, "Beef stock", 1.0, "cup");

            // Recipe 4: Chicken Curry
            long r4 = insertRecipe(db, "Chicken Curry",
                    "1. Marinate 500 g chicken in curry powder, turmeric, cumin, chilli, garlic, ginger, salt for 30 min.\n" +
                    "2. Sauté 1 diced onion in oil until golden. Add minced garlic + ginger (1 min).\n" +
                    "3. Add chicken; sear 3-4 min per side. Remove.\n" +
                    "4. Add 1 can chopped tomatoes + 1 cup chicken stock. Simmer 5 min.\n" +
                    "5. Return chicken. Add 100 ml cream or coconut milk. Simmer covered 25-30 min.", 30, 4);
            insertIngredient(db, r4, "Chicken pieces", 500.0, "g");
            insertIngredient(db, r4, "Curry powder", 1.0, "tbsp");
            insertIngredient(db, r4, "Turmeric", 1.0, "tsp");
            insertIngredient(db, r4, "Cumin", 1.0, "tsp");
            insertIngredient(db, r4, "Chilli powder", 1.0, "tsp");
            insertIngredient(db, r4, "Garlic", 2.0, "cloves");
            insertIngredient(db, r4, "Ginger", 1.0, "tbsp");
            insertIngredient(db, r4, "Oil", 3.0, "tbsp");
            insertIngredient(db, r4, "Onion", 1.0, "pcs");
            insertIngredient(db, r4, "Chopped tomatoes", 400.0, "g");
            insertIngredient(db, r4, "Chicken stock", 1.0, "cup");
            insertIngredient(db, r4, "Coconut milk", 100.0, "ml");

            // Recipe 5: Roti and Mince Curry
            long r5 = insertRecipe(db, "Roti and Mince Curry",
                    "1. Mince Curry: Sauté diced onion + minced garlic in oil. Add 500 g ground beef and brown well. Add curry powder, turmeric, chilli, cumin. Add chopped tomatoes + 1 cup water. Simmer 20 min. Add 100 ml cream or coconut milk.\n" +
                    "2. Roti: Mix 2 cups flour + salt + 1 tbsp oil + warm water into a soft dough. Rest 15 min. Divide into 8 balls, roll thin, cook on dry hot pan 1-2 min per side. Serve warm with curry.", 25, 4);
            insertIngredient(db, r5, "Ground beef", 500.0, "g");
            insertIngredient(db, r5, "Flour", 2.0, "cups");
            insertIngredient(db, r5, "Oil", 4.0, "tbsp");
            insertIngredient(db, r5, "Onion", 1.0, "pcs");
            insertIngredient(db, r5, "Garlic", 2.0, "cloves");
            insertIngredient(db, r5, "Curry powder", 2.0, "tbsp");
            insertIngredient(db, r5, "Turmeric", 1.0, "tsp");
            insertIngredient(db, r5, "Chilli powder", 1.0, "tsp");
            insertIngredient(db, r5, "Cumin", 1.0, "tsp");
            insertIngredient(db, r5, "Chopped tomatoes", 400.0, "g");
            insertIngredient(db, r5, "Coconut milk", 100.0, "ml");

            // Recipe 6: Egg Fried Rice
            long r6 = insertRecipe(db, "Egg Fried Rice",
                    "1. Cook 2 cups rice (day-old is best) and let cool.\n" +
                    "2. Scramble 3 eggs in a hot oiled wok; remove when set.\n" +
                    "3. Add 2 tbsp oil. Sauté 2 minced garlic + 2 spring onions. Add 1 cup diced mixed veg (peas, carrots, corn). Stir-fry 2 min.\n" +
                    "4. Add rice; stir-fry on high heat 3-4 min. Add 2 tbsp soy sauce + 1 tbsp oyster sauce. Toss in eggs.", 15, 2);
            insertIngredient(db, r6, "Rice", 2.0, "cups");
            insertIngredient(db, r6, "Eggs", 3.0, "pcs");
            insertIngredient(db, r6, "Oil", 4.0, "tbsp");
            insertIngredient(db, r6, "Garlic", 2.0, "cloves");
            insertIngredient(db, r6, "Spring onions", 2.0, "pcs");
            insertIngredient(db, r6, "Mixed veg", 1.0, "cup");
            insertIngredient(db, r6, "Soy sauce", 2.0, "tbsp");
            insertIngredient(db, r6, "Oyster sauce", 1.0, "tbsp");

            // Recipe 7: Biryani
            long r7 = insertRecipe(db, "Biryani",
                    "1. Parboil 2 cups basmati rice in salted water with 1 bay leaf + 4 cardamom pods until 70% done; drain.\n" +
                    "2. Sauté 1 large diced onion until deep golden. Add 500 g chicken/mutton; sear well.\n" +
                    "3. Add garam masala, turmeric, chilli powder, garlic, ginger. Add 1 can chopped tomatoes + 1 cup yogurt + 1 cup water. Simmer 20 min.\n" +
                    "4. Layer rice over meat with saffron milk, ghee, sliced raw onions. Cook on high 10 min, low 20 min (dum). Rest 10 min.", 50, 4);
            insertIngredient(db, r7, "Basmati rice", 2.0, "cups");
            insertIngredient(db, r7, "Chicken", 500.0, "g");
            insertIngredient(db, r7, "Onion", 3.0, "pcs");
            insertIngredient(db, r7, "Oil", 5.0, "tbsp");
            insertIngredient(db, r7, "Garam masala", 2.0, "tbsp");
            insertIngredient(db, r7, "Turmeric", 1.0, "tsp");
            insertIngredient(db, r7, "Chilli powder", 1.0, "tsp");
            insertIngredient(db, r7, "Garlic", 2.0, "cloves");
            insertIngredient(db, r7, "Ginger", 1.0, "tbsp");
            insertIngredient(db, r7, "Chopped tomatoes", 400.0, "g");
            insertIngredient(db, r7, "Yogurt", 1.0, "cup");

            // Recipe 8: Mash and Meatballs (Tomato Gravy)
            long r8 = insertRecipe(db, "Mash and Meatballs (Tomato Gravy)",
                    "1. Mash: Boil 500 g potatoes until tender. Drain, mash with 2 tbsp butter + 100 ml milk.\n" +
                    "2. Meatballs: Mix 500 g ground beef + 1 beaten egg + 1/2 cup breadcrumbs + 1 minced garlic + 1 tsp onion powder. Roll into 12-15 balls. Pan-fry in oil until browned.\n" +
                    "3. Gravy: Sauté 1 diced onion + 2 minced garlic. Add 2 tbsp tomato paste (1 min), 1 can chopped tomatoes, 1/2 cup stock. Simmer 10 min. Return meatballs to gravy; simmer 10 min. Serve over mash.", 30, 4);
            insertIngredient(db, r8, "Potatoes", 500.0, "g");
            insertIngredient(db, r8, "Ground beef", 500.0, "g");
            insertIngredient(db, r8, "Butter", 2.0, "tbsp");
            insertIngredient(db, r8, "Milk", 100.0, "ml");
            insertIngredient(db, r8, "Egg", 1.0, "pcs");
            insertIngredient(db, r8, "Breadcrumbs", 0.5, "cup");
            insertIngredient(db, r8, "Garlic", 3.0, "cloves");
            insertIngredient(db, r8, "Onion", 1.0, "pcs");
            insertIngredient(db, r8, "Tomato paste", 2.0, "tbsp");
            insertIngredient(db, r8, "Chopped tomatoes", 400.0, "g");

            // Recipe 9: Lamb Curry
            long r9 = insertRecipe(db, "Lamb Curry",
                    "1. Sauté 1 diced onion + 2 minced garlic + 1 tbsp ginger in 3 tbsp oil until golden.\n" +
                    "2. Add 500 g lamb cubes; sear on all sides.\n" +
                    "3. Add curry powder, turmeric, cumin, chilli, garam masala.\n" +
                    "4. Add 1 can chopped tomatoes + 1 cup lamb stock + 2 tbsp tomato paste. Simmer covered 1-1.5 hr until tender.\n" +
                    "5. Stir in 100 ml cream or coconut milk for the last 10 min. Serve over rice.", 90, 4);
            insertIngredient(db, r9, "Lamb cubes", 500.0, "g");
            insertIngredient(db, r9, "Oil", 3.0, "tbsp");
            insertIngredient(db, r9, "Onion", 1.0, "pcs");
            insertIngredient(db, r9, "Garlic", 2.0, "cloves");
            insertIngredient(db, r9, "Ginger", 1.0, "tbsp");
            insertIngredient(db, r9, "Curry powder", 2.0, "tbsp");
            insertIngredient(db, r9, "Turmeric", 1.0, "tsp");
            insertIngredient(db, r9, "Cumin", 1.0, "tsp");
            insertIngredient(db, r9, "Chilli powder", 1.0, "tsp");
            insertIngredient(db, r9, "Garam masala", 1.0, "tsp");
            insertIngredient(db, r9, "Chopped tomatoes", 400.0, "g");
            insertIngredient(db, r9, "Lamb stock", 1.0, "cup");
            insertIngredient(db, r9, "Tomato paste", 2.0, "tbsp");
            insertIngredient(db, r9, "Coconut milk", 100.0, "ml");

            // Recipe 10: Pap and Tin Fish
            long r10 = insertRecipe(db, "Pap and Tin Fish",
                    "1. Pap: Bring 4 cups water + 1 tbsp salt to boil. Add 2 cups mielie meal, stir until thick. Cover, simmer 30 min.\n" +
                    "2. Tin Fish: Sauté 1 chopped onion in oil until soft. Add 2 chopped tomatoes, curry powder, turmeric, garlic powder, oregano, pinch of chilli. Simmer 5 min.\n" +
                    "3. Add 1 tin (400 g) pilchards in tomato sauce, breaking up gently. Simmer 8-10 min. Serve over pap.", 30, 4);
            insertIngredient(db, r10, "Mielie meal", 2.0, "cups");
            insertIngredient(db, r10, "Pilchards in tomato sauce", 400.0, "g");
            insertIngredient(db, r10, "Oil", 2.0, "tbsp");
            insertIngredient(db, r10, "Onion", 1.0, "pcs");
            insertIngredient(db, r10, "Tomatoes", 2.0, "pcs");
            insertIngredient(db, r10, "Curry powder", 1.0, "tsp");
            insertIngredient(db, r10, "Turmeric", 0.5, "tsp");
            insertIngredient(db, r10, "Garlic powder", 1.0, "tsp");
            insertIngredient(db, r10, "Oregano", 1.0, "tsp");

            // Recipe 11: Mixed Veg Curry
            long r11 = insertRecipe(db, "Mixed Veg Curry",
                    "1. Sauté 1 diced onion + 2 minced garlic + 1 tbsp ginger in 3 tbsp oil.\n" +
                    "2. Add curry powder, turmeric, cumin, chilli powder (30 sec).\n" +
                    "3. Add 1 can chopped tomatoes + 1/2 cup water; simmer 5 min.\n" +
                    "4. Add 2 cups mixed veg (potatoes, carrots, cauliflower, peas, green beans). Simmer covered 20-25 min.\n" +
                    "5. Stir in 100 ml cream or coconut milk. Serve over rice.", 25, 4);
            insertIngredient(db, r11, "Mixed veg", 2.0, "cups");
            insertIngredient(db, r11, "Oil", 3.0, "tbsp");
            insertIngredient(db, r11, "Onion", 1.0, "pcs");
            insertIngredient(db, r11, "Garlic", 2.0, "cloves");
            insertIngredient(db, r11, "Ginger", 1.0, "tbsp");
            insertIngredient(db, r11, "Curry powder", 2.0, "tbsp");
            insertIngredient(db, r11, "Turmeric", 1.0, "tsp");
            insertIngredient(db, r11, "Cumin", 1.0, "tsp");
            insertIngredient(db, r11, "Chopped tomatoes", 400.0, "g");
            insertIngredient(db, r11, "Coconut milk", 100.0, "ml");

            // Recipe 12: Chow Mein
            long r12 = insertRecipe(db, "Chow Mein",
                    "1. Cook 2 cups chow mein or egg noodles; drain, toss with 1 tsp oil.\n" +
                    "2. Stir-fry 1 cup sliced chicken or pork in 2 tbsp oil on high heat 3 min. Remove.\n" +
                    "3. Add 2 tbsp oil. Stir-fry 1 cup cabbage, 1 cup carrots, 1 cup peas, garlic, ginger (2-3 min).\n" +
                    "4. Return meat and noodles. Add 3 tbsp soy sauce + 1 tbsp oyster sauce + 1 tbsp sesame oil. Toss on high heat 2 min.", 15, 2);
            insertIngredient(db, r12, "Noodles", 2.0, "cups");
            insertIngredient(db, r12, "Chicken", 1.0, "cup");
            insertIngredient(db, r12, "Cabbage", 1.0, "cup");
            insertIngredient(db, r12, "Carrots", 1.0, "cup");
            insertIngredient(db, r12, "Peas", 1.0, "cup");
            insertIngredient(db, r12, "Garlic", 2.0, "cloves");
            insertIngredient(db, r12, "Ginger", 1.0, "tbsp");
            insertIngredient(db, r12, "Soy sauce", 3.0, "tbsp");
            insertIngredient(db, r12, "Oyster sauce", 1.0, "tbsp");
            insertIngredient(db, r12, "Sesame oil", 1.0, "tbsp");

            // Recipe 13: Beef Stir-Fry
            long r13 = insertRecipe(db, "Beef Stir-Fry",
                    "1. Slice 400 g beef thinly. Marinate in 1 tbsp soy sauce + 1 tbsp cornstarch + 1 tsp oil for 15 min.\n" +
                    "2. Sauce: Mix 3 tbsp soy sauce + 1 tbsp oyster sauce + 1 tbsp hoisin + 1 tsp sesame oil + 1 tsp sugar.\n" +
                    "3. Sear beef in hot wok with 2 tbsp oil (1-2 min). Remove.\n" +
                    "4. Stir-fry 2 cups sliced veg (bell pepper, broccoli, onion, snap peas) 2-3 min.\n" +
                    "5. Return beef, pour sauce, toss 1 min. Serve over rice or noodles.", 15, 2);
            insertIngredient(db, r13, "Beef", 400.0, "g");
            insertIngredient(db, r13, "Soy sauce", 4.0, "tbsp");
            insertIngredient(db, r13, "Cornstarch", 1.0, "tbsp");
            insertIngredient(db, r13, "Oyster sauce", 1.0, "tbsp");
            insertIngredient(db, r13, "Hoisin sauce", 1.0, "tbsp");
            insertIngredient(db, r13, "Sesame oil", 1.0, "tsp");
            insertIngredient(db, r13, "Mixed veg", 2.0, "cups");

            // Recipe 14: Beef Burger
            long r14 = insertRecipe(db, "Beef Burger",
                    "1. Mix 500 g ground beef + 1 tsp salt + 1/2 tsp pepper + 1 tsp garlic powder. Form 4 patties.\n" +
                    "2. Cook patties in hot pan 3-4 min per side. Top each with 1 slice cheddar to melt.\n" +
                    "3. Toast 4 burger buns in the pan 1 min.\n" +
                    "4. Assemble: bottom bun, lettuce, tomato, patty + cheese, onion, pickles, ketchup/mustard, top bun.", 15, 4);
            insertIngredient(db, r14, "Ground beef", 500.0, "g");
            insertIngredient(db, r14, "Burger buns", 4.0, "pcs");
            insertIngredient(db, r14, "Cheddar cheese", 4.0, "pcs");
            insertIngredient(db, r14, "Garlic powder", 1.0, "tsp");
            insertIngredient(db, r14, "Lettuce", 1.0, "pcs");
            insertIngredient(db, r14, "Tomatoes", 1.0, "pcs");

            // Recipe 15: Fish and Chips
            long r15 = insertRecipe(db, "Fish and Chips",
                    "1. Fish: Mix 1 cup flour + 1 tsp baking powder + 1/2 tsp salt + pinch chilli + 250 ml cold lager/sparkling water into batter. Dust 4 fish fillets in flour, dip in batter, fry at 180 C (3-4 min per side).\n" +
                    "2. Chips: Cut 500 g potatoes into strips, soak and dry. First fry at 160 C (4-5 min). Second fry at 190 C (3-4 min) until golden and crisp. Serve together.", 20, 4);
            insertIngredient(db, r15, "Fish fillets", 4.0, "pcs");
            insertIngredient(db, r15, "Potatoes", 500.0, "g");
            insertIngredient(db, r15, "Flour", 1.0, "cup");
            insertIngredient(db, r15, "Baking powder", 1.0, "tsp");

            // Recipe 16: Fried Chicken
            long r16 = insertRecipe(db, "Fried Chicken",
                    "1. Marinate 8 chicken pieces in 200 ml buttermilk + 1 tbsp hot sauce + 1 tsp paprika + 1 tsp garlic powder (2 hr).\n" +
                    "2. Mix 1.5 cups flour + paprika + garlic powder + onion powder + salt + cayenne.\n" +
                    "3. Dredge chicken in flour mixture. Deep fry at 175 C for 12-15 min until internal temp reaches 75 C. Rest 5 min.", 25, 4);
            insertIngredient(db, r16, "Chicken pieces", 8.0, "pcs");
            insertIngredient(db, r16, "Buttermilk", 200.0, "ml");
            insertIngredient(db, r16, "Hot sauce", 1.0, "tbsp");
            insertIngredient(db, r16, "Flour", 1.5, "cups");
            insertIngredient(db, r16, "Paprika", 2.0, "tsp");
            insertIngredient(db, r16, "Garlic powder", 2.0, "tsp");

            // Recipe 17: Lasagna
            long r17 = insertRecipe(db, "Lasagna",
                    "1. Sauce: Sauté onion + garlic in oil. Add 500 g ground beef; brown. Stir in 2 tbsp tomato paste, 1 can chopped tomatoes, 1 can tomato puree, sugar, oregano. Simmer 30 min.\n" +
                    "2. Bechamel: Melt 30 g butter, whisk in 3 tbsp flour (1 min). Add 500 ml milk, simmer 5 min until thick. Season.\n" +
                    "3. Layer: Spread meat sauce, lasagna sheets, mozzarella, bechamel (3 layers). Bake at 180 C for 40 min.", 65, 6);
            insertIngredient(db, r17, "Lasagna sheets", 12.0, "pcs");
            insertIngredient(db, r17, "Ground beef", 500.0, "g");
            insertIngredient(db, r17, "Onion", 1.0, "pcs");
            insertIngredient(db, r17, "Garlic", 2.0, "cloves");
            insertIngredient(db, r17, "Tomato paste", 2.0, "tbsp");
            insertIngredient(db, r17, "Chopped tomatoes", 400.0, "g");
            insertIngredient(db, r17, "Butter", 30.0, "g");
            insertIngredient(db, r17, "Flour", 3.0, "tbsp");
            insertIngredient(db, r17, "Milk", 500.0, "ml");
            insertIngredient(db, r17, "Mozzarella", 200.0, "g");

            // Recipe 18: Omelette with Bacon
            long r18 = insertRecipe(db, "Omelette with Bacon",
                    "1. Pan-fry 3-4 slices of bacon over medium heat until crispy; remove and drain.\n" +
                    "2. Whisk 3 eggs + 1 tbsp milk + salt/pepper.\n" +
                    "3. Melt 1 tsp butter in pan. Pour in eggs. When nearly set, scatter bacon + 50 g grated cheese + chopped spring onions over one half. Fold and serve.", 10, 1);
            insertIngredient(db, r18, "Eggs", 3.0, "pcs");
            insertIngredient(db, r18, "Milk", 1.0, "tbsp");
            insertIngredient(db, r18, "Bacon", 4.0, "slices");
            insertIngredient(db, r18, "Butter", 1.0, "tsp");
            insertIngredient(db, r18, "Cheese", 50.0, "g");
            insertIngredient(db, r18, "Spring onions", 2.0, "pcs");

            // Recipe 19: Brown Stew
            long r19 = insertRecipe(db, "Brown Stew",
                    "1. Marinate 8 chicken pieces in chopped onion, scallions, garlic, ginger, thyme, allspice, paprika, brown sugar, browning sauce (2 hr).\n" +
                    "2. Sear chicken in 3 tbsp oil until browned; remove.\n" +
                    "3. Sauté marinade vegetables in pot. Add 1 can chopped tomatoes, 2 tbsp ketchup, 1 cup chicken stock, scotch bonnet pepper, bay leaves.\n" +
                    "4. Return chicken, cover and simmer 1.5-2 hr until fall-off-the-bone tender. Serve over rice.", 90, 4);
            insertIngredient(db, r19, "Chicken pieces", 8.0, "pcs");
            insertIngredient(db, r19, "Onion", 1.0, "pcs");
            insertIngredient(db, r19, "Scallions", 2.0, "pcs");
            insertIngredient(db, r19, "Garlic", 4.0, "cloves");
            insertIngredient(db, r19, "Ginger", 1.0, "tbsp");
            insertIngredient(db, r19, "Brown sugar", 1.0, "tbsp");
            insertIngredient(db, r19, "Soy sauce", 1.0, "tbsp");
            insertIngredient(db, r19, "Chopped tomatoes", 400.0, "g");
            insertIngredient(db, r19, "Chicken stock", 1.0, "cup");

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private static void insertPantryItem(SQLiteDatabase db, String name, double quantity, String unit, String category, Long expiryDate) {
        ContentValues values = new ContentValues();
        values.put(PantryItemEntry.COLUMN_NAME, name);
        values.put(PantryItemEntry.COLUMN_QUANTITY, quantity);
        values.put(PantryItemEntry.COLUMN_UNIT, unit);
        values.put(PantryItemEntry.COLUMN_CATEGORY, category);
        if (expiryDate != null) {
            values.put(PantryItemEntry.COLUMN_EXPIRY_DATE, expiryDate);
        } else {
            values.putNull(PantryItemEntry.COLUMN_EXPIRY_DATE);
        }
        values.put(PantryItemEntry.COLUMN_DATE_ADDED, System.currentTimeMillis());
        db.insert(PantryItemEntry.TABLE_NAME, null, values);
    }

    private static long insertRecipe(SQLiteDatabase db, String name, String instructions, int cookTime, int servings) {
        ContentValues values = new ContentValues();
        values.put(RecipeEntry.COLUMN_NAME, name);
        values.put(RecipeEntry.COLUMN_INSTRUCTIONS, instructions);
        values.put(RecipeEntry.COLUMN_COOK_TIME_MINUTES, cookTime);
        values.put(RecipeEntry.COLUMN_SERVINGS, servings);
        return db.insert(RecipeEntry.TABLE_NAME, null, values);
    }

    private static void insertIngredient(SQLiteDatabase db, long recipeId, String ingredientName, double quantity, String unit) {
        ContentValues values = new ContentValues();
        values.put(RecipeIngredientEntry.COLUMN_RECIPE_ID, recipeId);
        values.put(RecipeIngredientEntry.COLUMN_INGREDIENT_NAME, ingredientName);
        values.put(RecipeIngredientEntry.COLUMN_REQUIRED_QUANTITY, quantity);
        values.put(RecipeIngredientEntry.COLUMN_UNIT, unit);
        db.insert(RecipeIngredientEntry.TABLE_NAME, null, values);
    }
}
