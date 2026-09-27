package com.example.pantrymanagementsystem;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pantrymanagementsystem.data.entity.Recipe;
import com.example.pantrymanagementsystem.data.entity.RecipeIngredient;
import com.example.pantrymanagementsystem.repository.PantryRepository;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
// Add Recipe
public class AddRecipeActivity extends AppCompatActivity {

    private TextInputEditText etRecipeName, etIngredients, etInstructions, etCookTime, etServings;
    private Button btnSaveRecipe;
    private PantryRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        repository = PantryRepository.getInstance(this);

        etRecipeName = findViewById(R.id.etRecipeName);
        etIngredients = findViewById(R.id.etIngredients);
        etInstructions = findViewById(R.id.etInstructions);
        etCookTime = findViewById(R.id.etCookTime);
        etServings = findViewById(R.id.etServings);
        btnSaveRecipe = findViewById(R.id.btnSaveRecipe);

        btnSaveRecipe.setOnClickListener(v -> attemptSaveRecipe());
    }

    private void attemptSaveRecipe() {
        String name = etRecipeName.getText() != null ? etRecipeName.getText().toString().trim() : "";
        String ingredientsRaw = etIngredients.getText() != null ? etIngredients.getText().toString().trim() : "";
        String instructions = etInstructions.getText() != null ? etInstructions.getText().toString().trim() : "";
        String cookTimeStr = etCookTime.getText() != null ? etCookTime.getText().toString().trim() : "";
        String servingsStr = etServings.getText() != null ? etServings.getText().toString().trim() : "";

        // Required field validations
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

        // Parse ingredients text line by line
        List<RecipeIngredient> ingredientList = new ArrayList<>();
        String[] lines = ingredientsRaw.split("\n");
        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty()) {
                ingredientList.add(new RecipeIngredient(0, trimmed, 1.0, "unit"));
            }
        }

        repository.insertRecipe(recipe, ingredientList, recipeId -> {
            Toast.makeText(this, "Recipe '" + name + "' saved successfully!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        });
    }
}
