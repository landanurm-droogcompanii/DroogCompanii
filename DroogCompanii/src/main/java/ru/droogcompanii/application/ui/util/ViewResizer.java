package ru.droogcompanii.application.ui.util;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ls on 19.03.14.
 */
public class ViewResizer {

    public static void setSize(View view, int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    public static void setWidth(View view, int width) {
        setSize(view, width, view.getHeight());
    }

    public static void setHeight(View view, int height) {
        setSize(view, view.getWidth(), height);
    }
}
