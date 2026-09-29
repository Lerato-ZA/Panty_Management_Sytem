package com.example.pantrymanagementsystem.model;

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
}
