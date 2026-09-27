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

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class AddEditItemActivity extends AppCompatActivity {

    private TextInputEditText etItemName, etQuantity, etSize, etExpiryDate;
    private Spinner spinnerCategory;
    private TextView tvFormTitle;
    private Button btnSaveItem;

    private final String[] categories = {
            "Grains", "Vegetables", "Fruits", "Meat", "Dairy", "Canned Goods", "Spices", "Other"
    };

    private boolean isEditMode = false;
    private long editingItemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_item);

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

        // Check if editing an existing item
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("item_id")) {
            isEditMode = true;
            editingItemId = intent.getLongExtra("item_id", -1);
            tvFormTitle.setText("Edit Pantry Item");
            btnSaveItem.setText("Update Item");

            String itemName = intent.getStringExtra("item_name");
            etItemName.setText(itemName != null ? itemName : "");
            int qty = intent.getIntExtra("item_quantity", 1);
            etQuantity.setText(String.valueOf(qty));
            String itemSize = intent.getStringExtra("item_size");
            etSize.setText(itemSize != null ? itemSize : "");
            String itemExpiry = intent.getStringExtra("item_expiry");
            etExpiryDate.setText(itemExpiry != null ? itemExpiry : "");

            String category = intent.getStringExtra("item_category");
            if (category != null) {
                for (int i = 0; i < categories.length; i++) {
                    if (categories[i].equals(category)) {
                        spinnerCategory.setSelection(i);
                        break;
                    }
                }
            }
        }

        btnSaveItem.setOnClickListener(v -> attemptSave());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String formatted = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
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
        if (expiry.isEmpty()) expiry = null;

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

        Intent resultIntent = new Intent();
        resultIntent.putExtra("item_id", editingItemId);
        resultIntent.putExtra("item_name", name);
        resultIntent.putExtra("item_quantity", quantity);
        resultIntent.putExtra("item_size", size.isEmpty() ? "1 pcs" : size);
        resultIntent.putExtra("item_category", category);
        resultIntent.putExtra("item_expiry", expiry);
        resultIntent.putExtra("is_edit", isEditMode);

        setResult(RESULT_OK, resultIntent);
        Toast.makeText(this, isEditMode ? "Updated " + name : "Added " + name, Toast.LENGTH_SHORT).show();
        finish();
    }
}
