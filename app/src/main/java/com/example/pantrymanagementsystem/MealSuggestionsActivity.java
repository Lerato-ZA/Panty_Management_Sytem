package com.example.pantrymanagementsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

public class MealSuggestionsActivity extends AppCompatActivity {

    private RecyclerView rvSuggestedRecipes;
    private TextView tvEmptySuggestions;
    private View layoutEmptySuggestions;
    private RecipeAdapter adapter;
    private PantryRepository repository;

    private List<Recipe> suggestedRecipes = new ArrayList<>();
    private Map<Long, List<RecipeIngredient>> ingredientsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_suggestions);

        View root = findViewById(R.id.mealSuggestionsRoot);
        View header = findViewById(R.id.layoutSuggestionsHeader);
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
        repository.getSuggestedRecipes((recipes, map) -> {
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
