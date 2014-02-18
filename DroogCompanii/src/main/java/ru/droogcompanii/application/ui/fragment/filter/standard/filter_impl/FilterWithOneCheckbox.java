package ru.droogcompanii.application.ui.fragment.filter.standard.filter_impl;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.CheckBox;

import java.io.Serializable;

import ru.droogcompanii.application.ui.fragment.filter.Filter;
import ru.droogcompanii.application.ui.fragment.filter.FilterSet;

/**
 * Created by ls on 21.01.14.
 */
abstract class FilterWithOneCheckbox implements Filter, Serializable {
    private boolean needToInclude;

    protected FilterWithOneCheckbox() {
        needToInclude = isNeedToIncludeByDefault();
    }

    protected abstract boolean isNeedToIncludeByDefault();
    protected abstract int getIdOfCheckbox();
    protected abstract void necessarilyIncludeIn(FilterSet filterSet);

    @Override
    public void readFrom(View view) {
        needToInclude = findCheckBox(view).isChecked();
    }

    private CheckBox findCheckBox(View view) {
        return WrappedCheckBoxFinder.findByContainerId(view, getIdOfCheckbox());
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
        return getClass().getName();
    }

    @Override
    public void includeIn(FilterSet filterSet) {
        if (needToInclude) {
            necessarilyIncludeIn(filterSet);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        FilterWithOneCheckbox other = (FilterWithOneCheckbox) obj;
        return (needToInclude == other.needToInclude);
    }

    @Override
    public int hashCode() {
        return (needToInclude ? 1 : 0) + getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": needToInclude(" + needToInclude + ")";
    }
}
