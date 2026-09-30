package com.example.pantrymanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pantrymanagementsystem.data.entity.RecipeIngredientEntity;
import com.example.pantrymanagementsystem.model.Recipe;
import com.example.pantrymanagementsystem.service.PantryService;
import com.example.pantrymanagementsystem.util.UiUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeActivity extends AppCompatActivity {

    private TextInputEditText etRecipeName, etIngredients, etInstructions, etCookTime, etServings;
    private TextView tvFormTitle;
    private Button btnSaveRecipe;
    private PantryService pantryService;

    private boolean isEditMode = false;
    private long editingRecipeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        View root = findViewById(R.id.addRecipeRoot);
        View header = findViewById(R.id.layoutRecipeFormHeader);
        UiUtils.applyEdgeToEdge(root, header);

        pantryService = PantryService.getInstance(this);

        tvFormTitle = findViewById(R.id.tvFormTitle);
        etRecipeName = findViewById(R.id.etRecipeName);
        etIngredients = findViewById(R.id.etIngredients);
        etInstructions = findViewById(R.id.etInstructions);
        etCookTime = findViewById(R.id.etCookTime);
        etServings = findViewById(R.id.etServings);
        btnSaveRecipe = findViewById(R.id.btnSaveRecipe);
        View btnBack = findViewById(R.id.btnBack);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("recipe_id")) {
            isEditMode = true;
            editingRecipeId = intent.getLongExtra("recipe_id", -1);
            if (tvFormTitle != null) tvFormTitle.setText("Edit Recipe");
            if (btnSaveRecipe != null) btnSaveRecipe.setText("Update Recipe");

            pantryService.getRecipeById(editingRecipeId, recipe -> {
                if (recipe != null) {
                    etRecipeName.setText(recipe.getName());
                    etInstructions.setText(recipe.getInstructions());
                    etCookTime.setText(String.valueOf(recipe.getCookTimeMinutes()));
                    etServings.setText(String.valueOf(recipe.getServings()));

                    pantryService.getIngredientsForRecipe(editingRecipeId, ingredients -> {
                        if (ingredients != null && !ingredients.isEmpty()) {
                            StringBuilder sb = new StringBuilder();
                            for (RecipeIngredientEntity ing : ingredients) {
                                int qtyInt = (int) ing.getRequiredQuantity();
                                if (qtyInt > 1 || ing.getRequiredQuantity() != 1.0) {
                                    sb.append(qtyInt).append(" ").append(ing.getIngredientName()).append("\n");
                                } else {
                                    sb.append(ing.getIngredientName()).append("\n");
                                }
                            }
                            etIngredients.setText(sb.toString().trim());
                        }
                    });
                }
            });
        }

        if (btnSaveRecipe != null) {
            btnSaveRecipe.setOnClickListener(v -> attemptSaveRecipe());
        }
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

        Recipe recipe = new Recipe(editingRecipeId > 0 ? editingRecipeId : 0, name, instructions, cookTime, servings);

        List<RecipeIngredientEntity> ingredientList = new ArrayList<>();
        String[] lines = ingredientsRaw.split("\n");
        for (String line : lines) {
            RecipeIngredientEntity ing = parseIngredientLine(editingRecipeId > 0 ? editingRecipeId : 0, line);
            if (ing != null) {
                ingredientList.add(ing);
            }
        }

        if (isEditMode) {
            pantryService.updateRecipe(recipe, ingredientList, success -> {
                Toast.makeText(this, "Recipe '" + name + "' updated!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            });
        } else {
            pantryService.insertRecipe(recipe, ingredientList, recipeId -> {
                Toast.makeText(this, "Recipe '" + name + "' saved!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            });
        }
    }

    private RecipeIngredientEntity parseIngredientLine(long recipeId, String line) {
        if (line == null) return null;
        String trimmed = line.trim();
        if (trimmed.isEmpty()) return null;

        double qty = 1.0;
        String unit = "pcs";
        String name = trimmed;

        String[] parts = trimmed.split("\\s+", 2);
        if (parts.length > 0) {
            try {
                qty = Double.parseDouble(parts[0]);
                if (parts.length > 1) {
                    name = parts[1].trim();
                }
            } catch (NumberFormatException ignored) {
            }
        }

        if (name.isEmpty()) {
            name = trimmed;
        }

        return new RecipeIngredientEntity(recipeId, name, qty, unit);
    }
}
