package com.example.pantrymanagementsystem.model;

import com.example.pantrymanagementsystem.data.entity.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

// UI model for a recipe
public class Recipe {

    private long id;
    private String name;
    private String instructions;
    private int cookTimeMinutes;
    private int servings;
    private List<RecipeIngredient> ingredients = new ArrayList<>();

    public Recipe() {
    }

    public Recipe(long id, String name, String instructions, int cookTimeMinutes, int servings) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.cookTimeMinutes = cookTimeMinutes;
        this.servings = servings;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public int getCookTimeMinutes() { return cookTimeMinutes; }
    public void setCookTimeMinutes(int cookTimeMinutes) { this.cookTimeMinutes = cookTimeMinutes; }

    public int getServings() { return servings; }
    public void setServings(int servings) { this.servings = servings; }

    public List<RecipeIngredient> getIngredients() { return ingredients; }
    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients != null ? ingredients : new ArrayList<>();
    }

    // Converts UI recipe model to database entity
    public com.example.pantrymanagementsystem.data.entity.Recipe toEntity() {
        com.example.pantrymanagementsystem.data.entity.Recipe entity =
                new com.example.pantrymanagementsystem.data.entity.Recipe(name, instructions, cookTimeMinutes, servings);
        entity.setId(id);
        return entity;
    }

    // Creates UI recipe model from database entity
    public static Recipe fromEntity(com.example.pantrymanagementsystem.data.entity.Recipe entity) {
        if (entity == null) return null;
        return new Recipe(entity.getId(), entity.getName(), entity.getInstructions(), entity.getCookTimeMinutes(), entity.getServings());
    }
}
