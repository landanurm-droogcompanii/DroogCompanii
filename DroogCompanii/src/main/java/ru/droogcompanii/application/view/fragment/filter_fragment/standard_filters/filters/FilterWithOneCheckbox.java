package ru.droogcompanii.application.view.fragment.filter_fragment.standard_filters.filters;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.CheckBox;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.view.fragment.filter_fragment.Filters;

/**
 * Created by ls on 21.01.14.
 */
abstract class FilterWithOneCheckbox implements ru.droogcompanii.application.view.fragment.filter_fragment.Filter {
    private static final int ID_OF_CHECKBOX = R.id.sortByTitleCheckBox;

    private boolean needToInclude;

    protected FilterWithOneCheckbox() {
        needToInclude = isNeedToIncludeByDefault();
    }

    protected abstract boolean isNeedToIncludeByDefault();
    protected abstract int getIdOfCheckbox();
    protected abstract void necessarilyIncludeIn(Filters filters);

    @Override
    public void readFrom(View view) {
        needToInclude = findCheckBox(view).isChecked();
    }

    private CheckBox findCheckBox(View view) {
        return (CheckBox) view.findViewById(getIdOfCheckbox());
    }

    @Override
    public void displayOn(View view) {
        findCheckBox(view).setChecked(needToInclude);
    }

    @Override
    public void restoreFrom(SharedPreferences sharedPreferences) {
        needToInclude = sharedPreferences.getBoolean(getKey(), isNeedToIncludeByDefault());
    }

    @Override
    public void saveInto(SharedPreferences.Editor editor) {
        editor.putBoolean(getKey(), needToInclude);
    }

    protected String getKey() {
        return getClass().getSimpleName();
    }

    @Override
    public void includeIn(Filters filters) {
        if (needToInclude) {
            necessarilyIncludeIn(filters);
        }
    }
}
