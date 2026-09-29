package com.example.pantrymanagementsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pantrymanagementsystem.adapter.RecipeAdapter;
import com.example.pantrymanagementsystem.data.entity.RecipeIngredientEntity;
import com.example.pantrymanagementsystem.model.Recipe;
import com.example.pantrymanagementsystem.service.PantryService;
import com.example.pantrymanagementsystem.util.UiUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MealSuggestionsActivity extends AppCompatActivity {

    private RecyclerView rvSuggestedRecipes;
    private TextView tvEmptySuggestions;
    private View layoutEmptySuggestions;
    private RecipeAdapter adapter;
    private PantryService pantryService;

    private final List<Recipe> suggestedRecipes = new ArrayList<>();
    private final Map<Long, List<RecipeIngredientEntity>> ingredientsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_suggestions);

        View root = findViewById(R.id.mealSuggestionsRoot);
        View header = findViewById(R.id.layoutSuggestionsHeader);
        UiUtils.applyEdgeToEdge(root, header);

        pantryService = PantryService.getInstance(this);

        rvSuggestedRecipes = findViewById(R.id.rvSuggestedRecipes);
        tvEmptySuggestions = findViewById(R.id.tvEmptySuggestions);
        layoutEmptySuggestions = findViewById(R.id.layoutEmptySuggestions);
        View btnBack = findViewById(R.id.btnBack);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        adapter = new RecipeAdapter(suggestedRecipes, ingredientsMap);
        rvSuggestedRecipes.setLayoutManager(new LinearLayoutManager(this));
        rvSuggestedRecipes.setAdapter(adapter);

        loadSuggestions();
    }

    private void loadSuggestions() {
        pantryService.getSuggestedRecipes((recipes, map) -> {
            suggestedRecipes.clear();
            ingredientsMap.clear();

            boolean hasSuggestions = (recipes != null && !recipes.isEmpty());
            if (hasSuggestions) {
                suggestedRecipes.addAll(recipes);
                if (map != null) ingredientsMap.putAll(map);
                if (layoutEmptySuggestions != null) layoutEmptySuggestions.setVisibility(View.GONE);
                if (tvEmptySuggestions != null) tvEmptySuggestions.setVisibility(View.GONE);
                rvSuggestedRecipes.setVisibility(View.VISIBLE);
            } else {
                if (layoutEmptySuggestions != null) layoutEmptySuggestions.setVisibility(View.VISIBLE);
                if (tvEmptySuggestions != null) tvEmptySuggestions.setVisibility(View.VISIBLE);
                rvSuggestedRecipes.setVisibility(View.GONE);
            }

            adapter.updateData(suggestedRecipes, ingredientsMap);
        });
    }
}
