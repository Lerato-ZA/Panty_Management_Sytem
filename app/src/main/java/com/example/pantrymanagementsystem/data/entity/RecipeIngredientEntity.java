package com.example.pantrymanagementsystem.data.entity;

public class RecipeIngredientEntity {

    private long id;
    private long recipeId;
    private String ingredientName;
    private double requiredQuantity;
    private String unit;

    public RecipeIngredientEntity(long recipeId, String ingredientName,
                                  double requiredQuantity, String unit) {
        this.recipeId = recipeId;
        this.ingredientName = ingredientName;
        this.requiredQuantity = requiredQuantity;
        this.unit = unit;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getRecipeId() { return recipeId; }
    public void setRecipeId(long recipeId) { this.recipeId = recipeId; }

    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }

    public double getRequiredQuantity() { return requiredQuantity; }
    public void setRequiredQuantity(double requiredQuantity) { this.requiredQuantity = requiredQuantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}
