package ru.droogcompanii.application.view.fragment.filter_fragment.filters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.droogcompanii.application.view.fragment.filter_fragment.Filter;
import ru.droogcompanii.application.view.fragment.filter_fragment.FilterSet;

/**
 * Created by ls on 24.01.14.
 */
abstract class BaseFilters {

    public void readFrom(View view) {
        for (Filter filter : getFilters()) {
            filter.readFrom(view);
        }
    }

    protected abstract List<Filter> getFilters();

    public void displayOn(View containerView) {
        View viewOfFilters = prepareViewOfFilters(containerView.getContext());
        for (Filter filter : getFilters()) {
            filter.displayOn(viewOfFilters);
        }
        tryAddFilterViewToContainer(containerView, viewOfFilters);
    }

    protected abstract View prepareViewOfFilters(Context context);

    private void tryAddFilterViewToContainer(View containerView, View viewOfFilters) {
        if (!(containerView instanceof ViewGroup)) {
            throw new IllegalArgumentException("containerView must be instance of ViewGroup");
        }
        ViewGroup viewGroup = (ViewGroup) containerView;
        viewGroup.removeAllViews();
        viewGroup.addView(viewOfFilters);
    }

    public void restoreFrom(SharedPreferences sharedPreferences) {
        for (Filter filter : getFilters()) {
            filter.restoreFrom(sharedPreferences);
        }
    }

    public void saveInto(SharedPreferences.Editor editor) {
        for (Filter filter : getFilters()) {
            filter.saveInto(editor);
        }
    }

    public void includeIn(FilterSet filterSet) {
        for (Filter filter : getFilters()) {
            filter.includeIn(filterSet);
        }
    }
}
