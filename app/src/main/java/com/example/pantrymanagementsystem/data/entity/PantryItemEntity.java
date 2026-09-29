package com.example.pantrymanagementsystem.data.entity;


// Item class
public class  PantryItem {

    private long id;
    private String name;
    private double quantity;
    private String unit;
    private String category;
    private Long expiryDate;
    private long dateAdded;

    public PantryItem(String name, double quantity, String unit,
                      String category, Long expiryDate, long dateAdded) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.category = category;
        this.expiryDate = expiryDate;
        this.dateAdded = dateAdded;
    }

    // Getters and setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Long getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Long expiryDate) { this.expiryDate = expiryDate; }

    public long getDateAdded() { return dateAdded; }
    public void setDateAdded(long dateAdded) { this.dateAdded = dateAdded; }
}