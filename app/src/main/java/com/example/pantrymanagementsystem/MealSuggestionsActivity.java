package com.example.pantrymanagementsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pantrymanagementsystem.adapter.RecipeAdapter;
import com.example.pantrymanagementsystem.data.entity.PantryItemEntity;
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
    private Button btnTabReady, btnTabAlmost;
    private RecipeAdapter adapter;
    private PantryService pantryService;

    private final List<Recipe> readyRecipes = new ArrayList<>();
    private final List<Recipe> almostRecipes = new ArrayList<>();
    private final List<Recipe> displayedRecipes = new ArrayList<>();
    private final Map<Long, List<RecipeIngredientEntity>> ingredientsMap = new HashMap<>();
    private final List<PantryItemEntity> pantryItems = new ArrayList<>();

    private boolean isShowingReady = true;

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
        btnTabReady = findViewById(R.id.btnTabReady);
        btnTabAlmost = findViewById(R.id.btnTabAlmost);
        View btnBack = findViewById(R.id.btnBack);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        if (btnTabReady != null) {
            btnTabReady.setOnClickListener(v -> {
                isShowingReady = true;
                updateTabSelection();
                displayCurrentTab();
            });
        }

        if (btnTabAlmost != null) {
            btnTabAlmost.setOnClickListener(v -> {
                isShowingReady = false;
                updateTabSelection();
                displayCurrentTab();
            });
        }

        adapter = new RecipeAdapter(displayedRecipes, ingredientsMap);
        rvSuggestedRecipes.setLayoutManager(new LinearLayoutManager(this));
        rvSuggestedRecipes.setAdapter(adapter);

        loadSuggestions();
    }

    private void loadSuggestions() {
        pantryService.getSuggestedRecipes((ready, almost, map, pantry) -> {
            readyRecipes.clear();
            almostRecipes.clear();
            ingredientsMap.clear();
            pantryItems.clear();

            if (ready != null) readyRecipes.addAll(ready);
            if (almost != null) almostRecipes.addAll(almost);
            if (map != null) ingredientsMap.putAll(map);
            if (pantry != null) pantryItems.addAll(pantry);

            if (btnTabReady != null) {
                btnTabReady.setText("Ready (" + readyRecipes.size() + ")");
            }
            if (btnTabAlmost != null) {
                btnTabAlmost.setText("Almost (" + almostRecipes.size() + ")");
            }

            updateTabSelection();
            displayCurrentTab();
        });
    }

    private void updateTabSelection() {
        if (btnTabReady != null && btnTabAlmost != null) {
            btnTabReady.setAlpha(isShowingReady ? 1.0f : 0.6f);
            btnTabAlmost.setAlpha(isShowingReady ? 0.6f : 1.0f);
        }
    }

    private void displayCurrentTab() {
        displayedRecipes.clear();
        List<Recipe> currentSource = isShowingReady ? readyRecipes : almostRecipes;

        if (currentSource != null && !currentSource.isEmpty()) {
            displayedRecipes.addAll(currentSource);
            if (layoutEmptySuggestions != null) layoutEmptySuggestions.setVisibility(View.GONE);
            if (tvEmptySuggestions != null) tvEmptySuggestions.setVisibility(View.GONE);
            rvSuggestedRecipes.setVisibility(View.VISIBLE);
        } else {
            if (layoutEmptySuggestions != null) layoutEmptySuggestions.setVisibility(View.VISIBLE);
            if (tvEmptySuggestions != null) {
                tvEmptySuggestions.setVisibility(View.VISIBLE);
                if (isShowingReady) {
                    tvEmptySuggestions.setText("No recipes match 100% of your current pantry quantities.\n\nCheck the 'Almost' tab to see recipes that are only 1 or 2 items short!");
                } else {
                    tvEmptySuggestions.setText("No recipes are almost ready (1-2 items short).\n\nAdd more ingredients or recipes to unlock suggestions!");
                }
            }
            rvSuggestedRecipes.setVisibility(View.GONE);
        }

        adapter.updateData(displayedRecipes, ingredientsMap, pantryItems);
    }
}
