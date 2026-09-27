package com.example.pantrymanagementsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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

public class MealSuggestionsActivity extends AppCompatActivity {

    private RecyclerView rvSuggestedRecipes;
    private TextView tvEmptySuggestions;
    private RecipeAdapter adapter;
    private PantryRepository repository;

    private List<Recipe> suggestedRecipes = new ArrayList<>();
    private Map<Long, List<RecipeIngredient>> ingredientsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_suggestions);

        repository = PantryRepository.getInstance(this);

        rvSuggestedRecipes = findViewById(R.id.rvSuggestedRecipes);
        tvEmptySuggestions = findViewById(R.id.tvEmptySuggestions);

        adapter = new RecipeAdapter(suggestedRecipes, ingredientsMap);
        rvSuggestedRecipes.setLayoutManager(new LinearLayoutManager(this));
        rvSuggestedRecipes.setAdapter(adapter);

        loadSuggestions();
    }

    private void loadSuggestions() {
        repository.getSuggestedRecipes((recipes, map) -> {
            suggestedRecipes.clear();
            ingredientsMap.clear();

            if (recipes != null && !recipes.isEmpty()) {
                suggestedRecipes.addAll(recipes);
                if (map != null) ingredientsMap.putAll(map);
                tvEmptySuggestions.setVisibility(View.GONE);
                rvSuggestedRecipes.setVisibility(View.VISIBLE);
            } else {
                tvEmptySuggestions.setVisibility(View.VISIBLE);
                rvSuggestedRecipes.setVisibility(View.GONE);
            }

            adapter.updateData(suggestedRecipes, ingredientsMap);
        });
    }
}
