package ru.droogcompanii.application.ui.fragment.filter.standard.filter_impl;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

/**
 * Created by ls on 18.02.14.
 */
public class WrappedCheckBoxFinder {
    public static CheckBox findByContainerId(View view, int containerId) {
        ViewGroup container = (ViewGroup) view.findViewById(containerId);
        return (CheckBox) container.getChildAt(0);
    }
}
