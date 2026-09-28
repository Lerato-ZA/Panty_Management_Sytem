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

import com.example.pantrymanagementsystem.adapter.PantryAdapter;
import com.example.pantrymanagementsystem.model.PantryItem;
import com.example.pantrymanagementsystem.repository.PantryRepository;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PantryAdapter.OnItemActionListener {

    private RecyclerView rvPantryItems;
    private TextView tvEmptyState;
    private View layoutEmptyState;
    private TextView tvStatTotalItems;
    private TextView tvStatTotalRecipes;
    private PantryAdapter adapter;
    private PantryRepository repository;

    private List<PantryItem> pantryItems = new ArrayList<>();
    private ActivityResultLauncher<Intent> addEditItemLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Apply Edge-to-Edge System Bar and Cutout Insets
        View mainRoot = findViewById(R.id.mainRoot);
        View layoutHeader = findViewById(R.id.layoutHeader);
        if (mainRoot != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainRoot, (v, windowInsets) -> {
                Insets insets = windowInsets.getInsets(
                        WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout()
                );
                if (layoutHeader != null) {
                    int extraTop = (int) (8 * getResources().getDisplayMetrics().density);
                    layoutHeader.setPadding(
                            layoutHeader.getPaddingLeft(),
                            insets.top + extraTop,
                            layoutHeader.getPaddingRight(),
                            layoutHeader.getPaddingBottom()
                    );
                }
                v.setPadding(insets.left, 0, insets.right, insets.bottom);
                return windowInsets;
            });
        }

        repository = PantryRepository.getInstance(this);

        rvPantryItems = findViewById(R.id.rvPantryItems);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        tvStatTotalItems = findViewById(R.id.tvStatTotalItems);
        tvStatTotalRecipes = findViewById(R.id.tvStatTotalRecipes);
        View fabAddItem = findViewById(R.id.fabAddItem);
        View btnSuggestMeal = findViewById(R.id.btnSuggestMeal);
        View btnRecipes = findViewById(R.id.btnRecipes);

        adapter = new PantryAdapter(pantryItems, this);
        rvPantryItems.setLayoutManager(new LinearLayoutManager(this));
        rvPantryItems.setAdapter(adapter);

        loadData();

        addEditItemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadData();
                    }
                }
        );

        if (fabAddItem != null) {
            fabAddItem.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, AddEditItemActivity.class);
                addEditItemLauncher.launch(intent);
            });
        }

        if (btnRecipes != null) {
            btnRecipes.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, RecipeListActivity.class);
                startActivity(intent);
            });
        }

        if (btnSuggestMeal != null) {
            btnSuggestMeal.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, MealSuggestionsActivity.class);
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        repository.getAllPantryItems(entities -> {
            pantryItems.clear();
            if (entities != null) {
                for (com.example.pantrymanagementsystem.data.entity.PantryItem entity : entities) {
                    pantryItems.add(PantryItem.fromEntity(entity));
                }
            }
            adapter.updateItems(pantryItems);
            refreshEmptyState();
        });

        repository.getAllRecipes(recipes -> {
            int recipeCount = (recipes != null) ? recipes.size() : 0;
            if (tvStatTotalRecipes != null) {
                tvStatTotalRecipes.setText(String.valueOf(recipeCount));
            }
        });
    }

    @Override
    public void onEditClicked(PantryItem item) {
        Intent intent = new Intent(this, AddEditItemActivity.class);
        intent.putExtra("item_id", item.getId());
        addEditItemLauncher.launch(intent);
    }

    @Override
    public void onDeleteClicked(PantryItem item) {
        repository.deletePantryItem(item.getId(), success -> {
            loadData();
            Toast.makeText(this, item.getName() + " removed", Toast.LENGTH_SHORT).show();
        });
    }

    private void refreshEmptyState() {
        int count = pantryItems.size();
        boolean isEmpty = (count == 0);

        if (tvStatTotalItems != null) {
            tvStatTotalItems.setText(String.valueOf(count));
        }

        if (layoutEmptyState != null) {
            layoutEmptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        } else if (tvEmptyState != null) {
            tvEmptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        }

        rvPantryItems.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }
}
