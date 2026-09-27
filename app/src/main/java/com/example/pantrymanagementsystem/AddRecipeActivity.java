package com.example.pantrymanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pantrymanagementsystem.data.entity.Recipe;
import com.example.pantrymanagementsystem.data.entity.RecipeIngredient;
import com.example.pantrymanagementsystem.repository.PantryRepository;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeActivity extends AppCompatActivity {

    private TextInputEditText etRecipeName, etIngredients, etInstructions, etCookTime, etServings;
    private TextView tvFormTitle;
    private Button btnSaveRecipe;
    private PantryRepository repository;

    private boolean isEditMode = false;
    private long editingRecipeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        repository = PantryRepository.getInstance(this);

        tvFormTitle = findViewById(R.id.tvFormTitle);
        etRecipeName = findViewById(R.id.etRecipeName);
        etIngredients = findViewById(R.id.etIngredients);
        etInstructions = findViewById(R.id.etInstructions);
        etCookTime = findViewById(R.id.etCookTime);
        etServings = findViewById(R.id.etServings);
        btnSaveRecipe = findViewById(R.id.btnSaveRecipe);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("recipe_id")) {
            isEditMode = true;
            editingRecipeId = intent.getLongExtra("recipe_id", -1);
            if (tvFormTitle != null) tvFormTitle.setText("Edit Recipe");
            btnSaveRecipe.setText("Update Recipe");

            repository.getRecipeById(editingRecipeId, recipe -> {
                if (recipe != null) {
                    etRecipeName.setText(recipe.getName());
                    etInstructions.setText(recipe.getInstructions());
                    etCookTime.setText(String.valueOf(recipe.getCookTimeMinutes()));
                    etServings.setText(String.valueOf(recipe.getServings()));

                    repository.getIngredientsForRecipe(editingRecipeId, ingredients -> {
                        if (ingredients != null && !ingredients.isEmpty()) {
                            StringBuilder sb = new StringBuilder();
                            for (RecipeIngredient ing : ingredients) {
                                sb.append(ing.getIngredientName()).append("\n");
                            }
                            etIngredients.setText(sb.toString().trim());
                        }
                    });
                }
            });
        }

        btnSaveRecipe.setOnClickListener(v -> attemptSaveRecipe());
    }

    private void attemptSaveRecipe() {
        String name = etRecipeName.getText() != null ? etRecipeName.getText().toString().trim() : "";
        String ingredientsRaw = etIngredients.getText() != null ? etIngredients.getText().toString().trim() : "";
        String instructions = etInstructions.getText() != null ? etInstructions.getText().toString().trim() : "";
        String cookTimeStr = etCookTime.getText() != null ? etCookTime.getText().toString().trim() : "";
        String servingsStr = etServings.getText() != null ? etServings.getText().toString().trim() : "";

        if (name.isEmpty()) {
            etRecipeName.setError("Recipe name is required");
            return;
        }
        if (ingredientsRaw.isEmpty()) {
            etIngredients.setError("Ingredients are required");
            return;
        }
        if (instructions.isEmpty()) {
            etInstructions.setError("Preparation steps are required");
            return;
        }

        int cookTime = 0;
        if (!cookTimeStr.isEmpty()) {
            try {
                cookTime = Integer.parseInt(cookTimeStr);
            } catch (NumberFormatException ignored) {
            }
        }

        int servings = 1;
        if (!servingsStr.isEmpty()) {
            try {
                servings = Integer.parseInt(servingsStr);
            } catch (NumberFormatException ignored) {
            }
        }

        Recipe recipe = new Recipe(name, instructions, cookTime, servings);

        List<RecipeIngredient> ingredientList = new ArrayList<>();
        String[] lines = ingredientsRaw.split("\n");
        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty()) {
                ingredientList.add(new RecipeIngredient(editingRecipeId > 0 ? editingRecipeId : 0, trimmed, 1.0, "unit"));
            }
        }

        if (isEditMode) {
            recipe.setId(editingRecipeId);
            repository.updateRecipe(recipe, ingredientList, success -> {
                Toast.makeText(this, "Recipe '" + name + "' updated!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            });
        } else {
            repository.insertRecipe(recipe, ingredientList, recipeId -> {
                Toast.makeText(this, "Recipe '" + name + "' saved!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            });
        }
    }
}
