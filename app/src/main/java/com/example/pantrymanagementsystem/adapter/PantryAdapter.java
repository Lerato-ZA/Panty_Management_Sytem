package com.example.pantrymanagementsystem.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pantrymanagementsystem.R;
import com.example.pantrymanagementsystem.model.PantryItem;

import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    public interface OnItemActionListener {
        void onEditClicked(PantryItem item);
        void onDeleteClicked(PantryItem item);
    }

    private List<PantryItem> items;
    private final OnItemActionListener listener;

    public PantryAdapter(List<PantryItem> items, OnItemActionListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateItems(List<PantryItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = items.get(position);
        holder.tvItemName.setText(item.getName());

        String category = item.getCategory() != null ? item.getCategory().trim() : "Other";
        if (holder.tvCategoryIconText != null) {
            holder.tvCategoryIconText.setText(getCategoryEmoji(category));
        }

        if (holder.layoutCategoryAvatar != null) {
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.OVAL);
            shape.setColor(getCategoryBgColor(category));
            holder.layoutCategoryAvatar.setBackground(shape);
        }

        String details = "Size: " + item.getSize() + "  \u2022  Qty: " + item.getQuantity()
                + "  \u2022  Category: " + category;
        holder.tvItemDetails.setText(details);

        if (holder.tvItemExpiry != null) {
            if (item.getExpiryDate() != null && !item.getExpiryDate().trim().isEmpty()) {
                holder.tvItemExpiry.setVisibility(View.VISIBLE);
                holder.tvItemExpiry.setText("Expiry: " + item.getExpiryDate());
            } else {
                holder.tvItemExpiry.setVisibility(View.GONE);
            }
        }

        holder.btnEditItem.setOnClickListener(v -> {
            if (listener != null) listener.onEditClicked(item);
        });
        holder.btnDeleteItem.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClicked(item);
        });
    }

    // Icons for food category
    private String getCategoryEmoji(String category) {
        switch (category.toLowerCase()) {
            case "grains": return "🌾";
            case "vegetables": return "🥦";
            case "fruits": return "🍎";
            case "meat": return "🥩";
            case "dairy": return "🧀";
            case "canned goods": return "🥫";
            case "spices": return "🧂";
            default: return "🥣";
        }
    }

    private int getCategoryBgColor(String category) {
        switch (category.toLowerCase()) {
            case "grains": return Color.parseColor("#FEF3C7");
            case "vegetables": return Color.parseColor("#DCFCE7");
            case "fruits": return Color.parseColor("#FCE7F3");
            case "meat": return Color.parseColor("#FEE2E2");
            case "dairy": return Color.parseColor("#DBEAFE");
            case "canned goods": return Color.parseColor("#EDE9FE");
            case "spices": return Color.parseColor("#FFEDD5");
            default: return Color.parseColor("#F1F5F2");
        }
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvItemDetails, tvItemExpiry, tvCategoryIconText;
        FrameLayout layoutCategoryAvatar;
        ImageButton btnEditItem, btnDeleteItem;

        PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemDetails = itemView.findViewById(R.id.tvItemDetails);
            tvItemExpiry = itemView.findViewById(R.id.tvItemExpiry);
            tvCategoryIconText = itemView.findViewById(R.id.tvCategoryIconText);
            layoutCategoryAvatar = itemView.findViewById(R.id.layoutCategoryAvatar);
            btnEditItem = itemView.findViewById(R.id.btnEditItem);
            btnDeleteItem = itemView.findViewById(R.id.btnDeleteItem);
        }
    }
}
