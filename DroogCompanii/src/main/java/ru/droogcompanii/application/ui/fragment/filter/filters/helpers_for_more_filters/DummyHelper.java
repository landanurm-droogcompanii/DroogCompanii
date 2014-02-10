package ru.droogcompanii.application.ui.fragment.filter.filters.helpers_for_more_filters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.ui.fragment.filter.Filter;
import ru.droogcompanii.application.ui.fragment.filter.filters.MoreFilters;

/**
 * Created by ls on 27.01.14.
 */
class DummyHelper implements MoreFilters.Helper, Serializable {

    @Override
    public List<Filter> prepareDefaultFilters() {
        return noFilters();
    }

    private List<Filter> noFilters() {
        return new ArrayList<Filter>();
    }

    @Override
    public View prepareViewOfFilters(Context context) {
        return noView(context);
    }

    private View noView(Context context) {
        return new TextView(context);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null)) {
            return false;
        }
        return getClass() == obj.getClass();
    }

    @Override
    public int hashCode() {
        return 1;
    }
}