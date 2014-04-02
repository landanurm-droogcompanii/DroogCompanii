package ru.droogcompanii.application.util.ui.view;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by ls on 02.04.14.
 */
public class SimpleLayoutInflater {
    private final LayoutInflater layoutInflater;

    public SimpleLayoutInflater(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    public View inflate(int layoutId) {
        return layoutInflater.inflate(layoutId, null);
    }
}
