package ru.droogcompanii.application.view.fragment.filter_fragment.filters.helpers_for_more_filters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.view.fragment.filter_fragment.Filter;
import ru.droogcompanii.application.view.fragment.filter_fragment.filters.MoreFilters;

/**
 * Created by ls on 27.01.14.
 */
class DummyHelper implements MoreFilters.Helper {

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
}