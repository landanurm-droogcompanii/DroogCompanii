package ru.droogcompanii.application.ui.screens.main.filters_dialog.filters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import java.util.Collection;

/**
 * Created by ls on 25.03.14.
 */
public interface FilterHelper {
    boolean isActive();
    Collection<String> getColumnsOfPartnerPoint();
    Filter getFilter();
    View inflateContentView(Context context);
    void displayOn(View view);
    void readFrom(View view);
    void readFrom(SharedPreferences sharedPreferences);
    void writeTo(SharedPreferences.Editor editor);
}
