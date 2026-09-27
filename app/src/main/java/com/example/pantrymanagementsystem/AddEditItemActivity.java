package com.example.pantrymanagementsystem;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pantrymanagementsystem.model.PantryItem;
import com.example.pantrymanagementsystem.repository.PantryRepository;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;

public class AddEditItemActivity extends AppCompatActivity {

    private TextInputEditText etItemName, etQuantity, etSize, etExpiryDate;
    private Spinner spinnerCategory;
    private TextView tvFormTitle;
    private Button btnSaveItem;

    private PantryRepository repository;
    private boolean isEditMode = false;
    private long editingItemId = -1;

    // Categories
    private final String[] categories = {
            "Grains", "Vegetables", "Fruits", "Meat", "Dairy", "Canned Goods", "Spices", "Other"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);

        repository = PantryRepository.getInstance(this);

        tvFormTitle = findViewById(R.id.tvFormTitle);
        etItemName = findViewById(R.id.etItemName);
        etQuantity = findViewById(R.id.etQuantity);
        etSize = findViewById(R.id.etSize);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSaveItem = findViewById(R.id.btnSaveItem);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(categoryAdapter);

        etExpiryDate.setOnClickListener(v -> showDatePicker());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("item_id")) {
            isEditMode = true;
            editingItemId = intent.getLongExtra("item_id", -1);
            tvFormTitle.setText("Edit Pantry Item");
            btnSaveItem.setText("Update Item");

            repository.getPantryItemById(editingItemId, entity -> {
                if (entity != null) {
                    PantryItem item = PantryItem.fromEntity(entity);
                    etItemName.setText(item.getName());
                    etQuantity.setText(String.valueOf(item.getQuantity()));
                    etSize.setText(item.getSize());
                    if (item.getExpiryDate() != null) {
                        etExpiryDate.setText(item.getExpiryDate());
                    }

                    if (item.getCategory() != null) {
                        for (int i = 0; i < categories.length; i++) {
                            if (categories[i].equalsIgnoreCase(item.getCategory())) {
                                spinnerCategory.setSelection(i);
                                break;
                            }
                        }
                    }
                }
            });
        }

        btnSaveItem.setOnClickListener(v -> attemptSave());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String formatted = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            etExpiryDate.setText(formatted);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void attemptSave() {
        String name = etItemName.getText() != null ? etItemName.getText().toString().trim() : "";
        String quantityStr = etQuantity.getText() != null ? etQuantity.getText().toString().trim() : "";
        String size = etSize.getText() != null ? etSize.getText().toString().trim() : "";
        String category = spinnerCategory.getSelectedItem() != null ? spinnerCategory.getSelectedItem().toString() : "Other";
        String expiry = etExpiryDate.getText() != null ? etExpiryDate.getText().toString().trim() : "";

        if (name.isEmpty()) {
            etItemName.setError("Item name is required");
            return;
        }
        if (quantityStr.isEmpty()) {
            etQuantity.setError("Quantity is required");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            etQuantity.setError("Invalid number");
            return;
        }

        PantryItem uiItem = new PantryItem(editingItemId, name, quantity, size.isEmpty() ? "1 pcs" : size, category, expiry.isEmpty() ? null : expiry);

        if (isEditMode) {
            repository.updatePantryItem(uiItem.toEntity(), success -> {
                Toast.makeText(this, name + " updated", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            });
        } else {
            repository.insertPantryItem(uiItem.toEntity(), newId -> {
                Toast.makeText(this, name + " saved to database", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            });
        }
    }
}
