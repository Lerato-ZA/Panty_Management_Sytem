package com.example.pantrymanagementsystem.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pantrymanagementsystem.R;
import com.example.pantrymanagementsystem.data.entity.Recipe;
import com.example.pantrymanagementsystem.data.entity.RecipeIngredient;

import java.util.List;
import java.util.Map;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    public interface OnRecipeActionListener {
        void onEditClicked(Recipe recipe);
        void onDeleteClicked(Recipe recipe);
    }

    private List<Recipe> recipes;
    private Map<Long, List<RecipeIngredient>> ingredientsMap;
    private final OnRecipeActionListener listener;

    public RecipeAdapter(List<Recipe> recipes, Map<Long, List<RecipeIngredient>> ingredientsMap) {
        this(recipes, ingredientsMap, null);
    }

    public RecipeAdapter(List<Recipe> recipes, Map<Long, List<RecipeIngredient>> ingredientsMap, OnRecipeActionListener listener) {
        this.recipes = recipes;
        this.ingredientsMap = ingredientsMap;
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Recipe> newRecipes, Map<Long, List<RecipeIngredient>> newMap) {
        this.recipes = newRecipes;
        this.ingredientsMap = newMap;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.tvRecipeName.setText(recipe.getName());

        // Original meta format: "Cook time: X mins  •  Servings: Y"
        String meta = "Cook time: " + recipe.getCookTimeMinutes() + " mins  \u2022  Servings: " + recipe.getServings();
        holder.tvRecipeMeta.setText(meta);

        StringBuilder ingText = new StringBuilder();
        List<RecipeIngredient> ingredients = ingredientsMap != null ? ingredientsMap.get(recipe.getId()) : null;
        if (ingredients != null && !ingredients.isEmpty()) {
            for (RecipeIngredient ing : ingredients) {
                ingText.append("\u2022 ").append(ing.getIngredientName())
                        .append(" (").append(ing.getRequiredQuantity() % 1 == 0 ? (int) ing.getRequiredQuantity() : ing.getRequiredQuantity())
                        .append(" ").append(ing.getUnit()).append(")\n");
            }
        } else {
            ingText.append("No specific ingredients listed");
        }
        holder.tvRecipeIngredients.setText(ingText.toString().trim());

        holder.tvRecipeInstructions.setText(recipe.getInstructions());

        if (listener != null) {
            holder.btnEditRecipe.setVisibility(View.VISIBLE);
            holder.btnDeleteRecipe.setVisibility(View.VISIBLE);
            holder.btnEditRecipe.setOnClickListener(v -> listener.onEditClicked(recipe));
            holder.btnDeleteRecipe.setOnClickListener(v -> listener.onDeleteClicked(recipe));
        } else {
            holder.btnEditRecipe.setVisibility(View.GONE);
            holder.btnDeleteRecipe.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return recipes == null ? 0 : recipes.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecipeName, tvRecipeMeta, tvRecipeIngredients, tvRecipeInstructions;
        ImageButton btnEditRecipe, btnDeleteRecipe;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecipeName = itemView.findViewById(R.id.tvRecipeName);
            tvRecipeMeta = itemView.findViewById(R.id.tvRecipeMeta);
            tvRecipeIngredients = itemView.findViewById(R.id.tvRecipeIngredients);
            tvRecipeInstructions = itemView.findViewById(R.id.tvRecipeInstructions);
            btnEditRecipe = itemView.findViewById(R.id.btnEditRecipe);
            btnDeleteRecipe = itemView.findViewById(R.id.btnDeleteRecipe);
        }
    }
}
