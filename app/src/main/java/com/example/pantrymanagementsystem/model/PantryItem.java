package com.example.pantrymanagementsystem.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// UI model for a pantry item
public class PantryItem {

    private long id;
    private String name;
    private int quantity;
    private String size;
    private String category;
    private String expiryDate;

    public PantryItem() {
    }

    public PantryItem(long id, String name, int quantity, String size, String category, String expiryDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.size = size;
        this.category = category;
        this.expiryDate = expiryDate;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    // Converts UI model to database entity
    public com.example.pantrymanagementsystem.data.entity.PantryItem toEntity() {
        Long expiryMillis = null;
        if (expiryDate != null && !expiryDate.trim().isEmpty()) {
            try {
                Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(expiryDate.trim());
                if (d != null) expiryMillis = d.getTime();
            } catch (Exception ignored) {
            }
        }
        com.example.pantrymanagementsystem.data.entity.PantryItem entity =
                new com.example.pantrymanagementsystem.data.entity.PantryItem(
                        name,
                        quantity,
                        size != null && !size.trim().isEmpty() ? size : "pcs",
                        category != null ? category : "Other",
                        expiryMillis,
                        System.currentTimeMillis()
                );
        entity.setId(id);
        return entity;
    }

    // Creates UI model from database entity
    public static PantryItem fromEntity(com.example.pantrymanagementsystem.data.entity.PantryItem entity) {
        if (entity == null) return null;
        String expiryStr = null;
        if (entity.getExpiryDate() != null) {
            expiryStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(entity.getExpiryDate()));
        }
        return new PantryItem(
                entity.getId(),
                entity.getName(),
                (int) entity.getQuantity(),
                entity.getUnit(),
                entity.getCategory(),
                expiryStr
        );
    }
}
