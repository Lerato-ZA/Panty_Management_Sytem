package com.example.pantrymanagementsystem.util;

import com.example.pantrymanagementsystem.data.entity.PantryItemEntity;
import com.example.pantrymanagementsystem.data.entity.RecipeIngredientEntity;

import java.util.ArrayList;
import java.util.List;

public class IngredientMatcher {

    private IngredientMatcher() {
    }

    public static boolean isIngredientMatch(String pantryName, String requiredName) {
        if (pantryName == null || requiredName == null) return false;

        String p = pantryName.replaceAll("[^a-zA-Z0-9\\s]", " ").trim().toLowerCase();
        String r = requiredName.replaceAll("[^a-zA-Z0-9\\s]", " ").trim().toLowerCase();

        if (p.isEmpty() || r.isEmpty()) return false;
        if (p.equals(r) || stem(p).equals(stem(r))) return true;
        if (p.contains(r) || r.contains(p)) return true;

        String[] pWords = filterStopWords(p.split("\\s+"));
        String[] rWords = filterStopWords(r.split("\\s+"));

        if (pWords.length == 0 || rWords.length == 0) return false;

        for (String rWord : rWords) {
            String rStem = stem(rWord);
            for (String pWord : pWords) {
                String pStem = stem(pWord);
                if (pStem.equals(rStem) || pWord.contains(rWord) || rWord.contains(pWord)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isQuantitySatisfied(PantryItemEntity item, RecipeIngredientEntity req) {
        if (item == null || req == null) return false;
        double available = item.getQuantity();
        double required = req.getRequiredQuantity();

        if (available <= 0) return false;
        if (available >= required) return true;

        String pantryUnit = item.getUnit() != null ? item.getUnit().trim().toLowerCase() : "";
        String reqUnit = req.getUnit() != null ? req.getUnit().trim().toLowerCase() : "";

        if (available >= 1.0) {
            if (!pantryUnit.equalsIgnoreCase(reqUnit) || required > 10.0) {
                return true;
            }
        }

        return false;
    }

    public static PantryItemEntity findPantryMatch(List<PantryItemEntity> pantry, RecipeIngredientEntity req) {
        if (pantry == null || req == null) return null;

        for (PantryItemEntity item : pantry) {
            if (item.getQuantity() > 0 && isIngredientMatch(item.getName(), req.getIngredientName())) {
                return item;
            }
        }

        for (PantryItemEntity item : pantry) {
            if (isIngredientMatch(item.getName(), req.getIngredientName())) {
                return item;
            }
        }

        return null;
    }

    private static String stem(String word) {
        if (word == null) return "";
        String w = word.toLowerCase();
        if (w.endsWith("es") && w.length() > 4) return w.substring(0, w.length() - 2);
        if (w.endsWith("s") && w.length() > 3) return w.substring(0, w.length() - 1);
        return w;
    }

    private static String[] filterStopWords(String[] words) {
        List<String> list = new ArrayList<>();
        for (String w : words) {
            String t = w.trim().toLowerCase();
            if (t.length() <= 2) continue;
            if (t.equals("fresh") || t.equals("organic") || t.equals("chopped") ||
                t.equals("sliced") || t.equals("diced") || t.equals("ground") ||
                t.equals("canned") || t.equals("large") || t.equals("small") ||
                t.equals("cup") || t.equals("cups") || t.equals("unit") || t.equals("piece") ||
                t.equals("gram") || t.equals("grams") || t.equals("tbsp") || t.equals("tsp")) {
                continue;
            }
            list.add(t);
        }
        return list.toArray(new String[0]);
    }
}
