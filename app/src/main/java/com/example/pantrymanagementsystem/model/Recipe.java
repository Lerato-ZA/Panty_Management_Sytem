package com.example.pantrymanagementsystem.model;

import com.example.pantrymanagementsystem.data.entity.RecipeIngredientEntity;

import java.util.ArrayList;
import java.util.List;

// UI model for a recipe
public class Recipe {

    private long id;
    private String name;
    private String instructions;
    private int cookTimeMinutes;
    private int servings;
    private List<RecipeIngredientEntity> ingredients = new ArrayList<>();

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

    public List<RecipeIngredientEntity> getIngredients() { return ingredients; }
    public void setIngredients(List<RecipeIngredientEntity> ingredients) {
        this.ingredients = ingredients != null ? ingredients : new ArrayList<>();
    }
}
