package com.example.pantrymanagementsystem.data.entity;

public class Recipe {

    private long id;
    private String name;
    private String instructions;
    private int cookTimeMinutes;
    private int servings;

    public Recipe(String name, String instructions, int cookTimeMinutes, int servings) {
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
}