package ru.droogcompanii.application.ui.fragment.filter.filters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.droogcompanii.application.ui.fragment.filter.Filter;
import ru.droogcompanii.application.ui.fragment.filter.FilterSet;

/**
 * Created by ls on 24.01.14.
 */
abstract class BaseFilters implements Filter {

    @Override
    public void readFrom(View view) {
        for (Filter filter : getFilters()) {
            filter.readFrom(view);
        }
    }

    protected abstract List<Filter> getFilters();

    @Override
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

    @Override
    public void restoreFrom(SharedPreferences sharedPreferences) {
        for (Filter filter : getFilters()) {
            filter.restoreFrom(sharedPreferences);
        }
    }

    @Override
    public void saveInto(SharedPreferences.Editor editor) {
        for (Filter filter : getFilters()) {
            filter.saveInto(editor);
        }
    }

    @Override
    public void includeIn(FilterSet filterSet) {
        for (Filter filter : getFilters()) {
            filter.includeIn(filterSet);
        }
    }
}
