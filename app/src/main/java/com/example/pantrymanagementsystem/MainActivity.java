package com.example.pantrymanagementsystem;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.pantrymanagementsystem.data.dao.PantryItemDao;
import com.example.pantrymanagementsystem.model.PantryItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PantryAdapter.OnItemActionListener {

    private RecyclerView rvPantryItems;
    private TextView tvEmptyState;
    private PantryAdapter adapter;
    private PantryItemDao pantryItemDao;

    private List<PantryItem> pantryItems = new ArrayList<>();
    private ActivityResultLauncher<Intent> addEditItemLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pantryItemDao = new PantryItemDao(this);

        rvPantryItems = findViewById(R.id.rvPantryItems);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        FloatingActionButton fabAddItem = findViewById(R.id.fabAddItem);
        View btnSuggestMeal = findViewById(R.id.btnSuggestMeal);

        adapter = new PantryAdapter(pantryItems, this);
        rvPantryItems.setLayoutManager(new LinearLayoutManager(this));
        rvPantryItems.setAdapter(adapter);

        loadPantryItemsFromDb();

        addEditItemLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadPantryItemsFromDb();
                    }
                }
        );

        fabAddItem.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditItemActivity.class);
            addEditItemLauncher.launch(intent);
        });

        btnSuggestMeal.setOnClickListener(v ->
            Toast.makeText(this, "Meal suggestion logic comes in Tier 2", Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantryItemsFromDb();
    }

    private void loadPantryItemsFromDb() {
        List<com.example.pantrymanagementsystem.data.entity.PantryItem> entities = pantryItemDao.getAll();
        pantryItems.clear();

        for (com.example.pantrymanagementsystem.data.entity.PantryItem entity : entities) {
            pantryItems.add(PantryItem.fromEntity(entity));
        }
        adapter.updateItems(pantryItems);
        refreshEmptyState();
    }

    @Override
    public void onEditClicked(PantryItem item) {
        Intent intent = new Intent(this, AddEditItemActivity.class);
        intent.putExtra("item_id", item.getId());
        addEditItemLauncher.launch(intent);
    }

    @Override
    public void onDeleteClicked(PantryItem item) {
        pantryItemDao.delete(item.getId());
        loadPantryItemsFromDb();
        Toast.makeText(this, item.getName() + " removed from database", Toast.LENGTH_SHORT).show();
    }

    private void refreshEmptyState() {
        boolean isEmpty = pantryItems.isEmpty();
        tvEmptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        rvPantryItems.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }
}
