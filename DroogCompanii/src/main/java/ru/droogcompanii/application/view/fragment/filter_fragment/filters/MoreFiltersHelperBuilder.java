package ru.droogcompanii.application.view.fragment.filter_fragment.filters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.view.fragment.filter_fragment.Filter;

/**
 * Created by ls on 24.01.14.
 */
class MoreFiltersHelperBuilder {
    public static MoreFilters.Helper build(PartnerCategory partnerCategory) {
        if (partnerCategory == null) {
            return new DummyHelper();
        }
        throw new RuntimeException("Not implemented yet!");
    }

    private static class DummyHelper implements MoreFilters.Helper {

        @Override
        public List<Filter> prepareDefaultFilters() {
            return noFilters();
        }

        private List<Filter> noFilters() {
            return new ArrayList<Filter>();
        }

        @Override
        public View prepareViewOfFilters(View containerView) {
            return noView(containerView.getContext());
        }

        private View noView(Context context) {
            return new TextView(context);
        }
    }
}
