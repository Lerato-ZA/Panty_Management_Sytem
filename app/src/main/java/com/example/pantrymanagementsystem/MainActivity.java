package com.example.pantrymanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pantrymanagementsystem.adapter.PantryAdapter;
import com.example.pantrymanagementsystem.model.PantryItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PantryAdapter.OnItemActionListener {

    private RecyclerView rvPantryItems;
    private TextView tvEmptyState;
    private PantryAdapter adapter;

    private List<PantryItem> pantryItems = new ArrayList<>();
    private ActivityResultLauncher<Intent> addEditItemLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvPantryItems = findViewById(R.id.rvPantryItems);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        FloatingActionButton fabAddItem = findViewById(R.id.fabAddItem);
        View btnSuggestMeal = findViewById(R.id.btnSuggestMeal);

        loadSampleData();

        adapter = new PantryAdapter(pantryItems, this);
        rvPantryItems.setLayoutManager(new LinearLayoutManager(this));
        rvPantryItems.setAdapter(adapter);
        refreshEmptyState();

        addEditItemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        String name = data.getStringExtra("item_name");
                        int quantity = data.getIntExtra("item_quantity", 1);
                        String size = data.getStringExtra("item_size");
                        String category = data.getStringExtra("item_category");
                        String expiry = data.getStringExtra("item_expiry");
                        boolean isEdit = data.getBooleanExtra("is_edit", false);
                        long itemId = data.getLongExtra("item_id", -1);

                        if (isEdit) {
                            boolean updated = false;
                            for (PantryItem item : pantryItems) {
                                if (item.getId() == itemId) {
                                    item.setName(name);
                                    item.setQuantity(quantity);
                                    item.setSize(size);
                                    item.setCategory(category);
                                    item.setExpiryDate(expiry);
                                    updated = true;
                                    break;
                                }
                            }
                            if (updated) {
                                Toast.makeText(this, name + " updated", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            long newId = pantryItems.isEmpty() ? 1 : pantryItems.get(pantryItems.size() - 1).getId() + 1;
                            PantryItem newItem = new PantryItem(newId, name, quantity, size, category, expiry);
                            pantryItems.add(newItem);
                            Toast.makeText(this, name + " added", Toast.LENGTH_SHORT).show();
                        }

                        adapter.updateItems(pantryItems);
                        refreshEmptyState();
                    }
                }
        );

        fabAddItem.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditItemActivity.class);
            addEditItemLauncher.launch(intent);
        });

        btnSuggestMeal.setOnClickListener(v -> {
            Toast.makeText(this, "Meal suggestion logic comes in Tier 2", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onEditClicked(PantryItem item) {
        Intent intent = new Intent(this, AddEditItemActivity.class);
        intent.putExtra("item_id", item.getId());
        intent.putExtra("item_name", item.getName());
        intent.putExtra("item_quantity", item.getQuantity());
        intent.putExtra("item_size", item.getSize());
        intent.putExtra("item_category", item.getCategory());
        intent.putExtra("item_expiry", item.getExpiryDate());
        addEditItemLauncher.launch(intent);
    }

    @Override
    public void onDeleteClicked(PantryItem item) {
        pantryItems.remove(item);
        adapter.updateItems(pantryItems);
        refreshEmptyState();
        Toast.makeText(this, item.getName() + " removed", Toast.LENGTH_SHORT).show();
    }

    private void refreshEmptyState() {
        boolean isEmpty = pantryItems.isEmpty();
        tvEmptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        rvPantryItems.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    private void loadSampleData() {
        pantryItems.add(new PantryItem(1, "Rice", 2, "2 kg", "Grains", null));
        pantryItems.add(new PantryItem(2, "Tinned Tomatoes", 4, "400 g", "Canned Goods", "2026-12-01"));
        pantryItems.add(new PantryItem(3, "Onions", 6, "1 kg", "Vegetables", null));
        pantryItems.add(new PantryItem(4, "Chicken Breast", 1, "500 g", "Meat", "2026-10-02"));
    }
}
