package com.example.pantrymanagementsystem.util;

import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UiUtils {

    private UiUtils() {
    }

    public static void applyEdgeToEdge(View root, View header) {
        if (root == null) return;

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout()
            );
            if (header != null) {
                int extraTop = (int) (8 * v.getResources().getDisplayMetrics().density);
                header.setPadding(
                        header.getPaddingLeft(),
                        insets.top + extraTop,
                        header.getPaddingRight(),
                        header.getPaddingBottom()
                );
            }
            v.setPadding(insets.left, 0, insets.right, insets.bottom);
            return windowInsets;
        });
    }
}
