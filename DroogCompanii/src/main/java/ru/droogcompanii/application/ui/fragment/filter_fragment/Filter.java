package ru.droogcompanii.application.ui.fragment.filter_fragment;

import android.content.SharedPreferences;
import android.view.View;

/**
 * Created by ls on 21.01.14.
 */
public interface Filter {
    void readFrom(View view);
    void displayOn(View view);
    void restoreFrom(SharedPreferences sharedPreferences);
    void saveInto(SharedPreferences.Editor editor);
    void includeIn(FilterSet filterSet);
}
