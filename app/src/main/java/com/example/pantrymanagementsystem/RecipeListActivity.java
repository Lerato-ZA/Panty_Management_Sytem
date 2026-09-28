package com.example.pantrymanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pantrymanagementsystem.adapter.RecipeAdapter;
import com.example.pantrymanagementsystem.data.entity.Recipe;
import com.example.pantrymanagementsystem.data.entity.RecipeIngredient;
import com.example.pantrymanagementsystem.repository.PantryRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeListActivity extends AppCompatActivity implements RecipeAdapter.OnRecipeActionListener {

    private RecyclerView rvRecipes;
    private TextView tvEmptyRecipes;
    private View layoutEmptyRecipes;
    private RecipeAdapter adapter;
    private PantryRepository repository;

    private List<Recipe> recipeList = new ArrayList<>();
    private Map<Long, List<RecipeIngredient>> ingredientsMap = new HashMap<>();
    private ActivityResultLauncher<Intent> addEditRecipeLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        // Apply Window Insets
        View root = findViewById(R.id.recipeListRoot);
        View header = findViewById(R.id.layoutRecipeHeader);
        if (root != null) {
            ViewCompat.setOnApplyWindowInsetsListener(root, (v, windowInsets) -> {
                Insets insets = windowInsets.getInsets(
                        WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout()
                );
                if (header != null) {
                    int extraTop = (int) (8 * getResources().getDisplayMetrics().density);
                    header.setPadding(
                            header.getPaddingLeft(),
                            insets.top + extraTop,
                            header.getPaddingRight(),
                            header.getPaddingBottom()
                    );
                }
                v.setPadding(insets.left, 0, insets.right, insets.bottom);
                return windowInsets;
            });
        }

        repository = PantryRepository.getInstance(this);

        rvRecipes = findViewById(R.id.rvRecipes);
        tvEmptyRecipes = findViewById(R.id.tvEmptyRecipes);
        layoutEmptyRecipes = findViewById(R.id.layoutEmptyRecipes);
        View fabAddRecipe = findViewById(R.id.fabAddRecipe);
        View btnBack = findViewById(R.id.btnBack);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        adapter = new RecipeAdapter(recipeList, ingredientsMap, this);
        rvRecipes.setLayoutManager(new LinearLayoutManager(this));
        rvRecipes.setAdapter(adapter);

        addEditRecipeLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadRecipes();
                    }
                }
        );

        if (fabAddRecipe != null) {
            fabAddRecipe.setOnClickListener(v -> {
                Intent intent = new Intent(RecipeListActivity.this, AddRecipeActivity.class);
                addEditRecipeLauncher.launch(intent);
            });
        }

        loadRecipes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecipes();
    }

    private void loadRecipes() {
        repository.getAllRecipes(recipes -> {
            recipeList.clear();
            ingredientsMap.clear();

            boolean hasRecipes = (recipes != null && !recipes.isEmpty());
            if (hasRecipes) {
                recipeList.addAll(recipes);
                if (layoutEmptyRecipes != null) layoutEmptyRecipes.setVisibility(View.GONE);
                if (tvEmptyRecipes != null) tvEmptyRecipes.setVisibility(View.GONE);
                rvRecipes.setVisibility(View.VISIBLE);

                for (Recipe recipe : recipes) {
                    repository.getIngredientsForRecipe(recipe.getId(), ingredients -> {
                        if (ingredients != null) {
                            ingredientsMap.put(recipe.getId(), ingredients);
                            adapter.updateData(recipeList, ingredientsMap);
                        }
                    });
                }
            } else {
                if (layoutEmptyRecipes != null) layoutEmptyRecipes.setVisibility(View.VISIBLE);
                if (tvEmptyRecipes != null) tvEmptyRecipes.setVisibility(View.VISIBLE);
                rvRecipes.setVisibility(View.GONE);
            }

            adapter.updateData(recipeList, ingredientsMap);
        });
    }

    @Override
    public void onEditClicked(Recipe recipe) {
        Intent intent = new Intent(this, AddRecipeActivity.class);
        intent.putExtra("recipe_id", recipe.getId());
        addEditRecipeLauncher.launch(intent);
    }

    @Override
    public void onDeleteClicked(Recipe recipe) {
        repository.deleteRecipe(recipe.getId(), success -> {
            Toast.makeText(this, "Recipe '" + recipe.getName() + "' deleted", Toast.LENGTH_SHORT).show();
            loadRecipes();
        });
    }
}
