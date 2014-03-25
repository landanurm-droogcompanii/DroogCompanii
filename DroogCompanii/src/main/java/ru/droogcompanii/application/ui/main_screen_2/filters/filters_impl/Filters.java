package ru.droogcompanii.application.ui.main_screen_2.filters.filters_impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.util.OnEachHandler;

/**
 * Created by ls on 25.03.14.
 */
public class Filters implements Serializable {
    private final List<Filter> filters;

    public static Filters getCurrentFilters(Context context) {
        Filters currentFilters = new Filters();
        SharedPreferences sharedPreferences = FiltersSharedPreferencesProvider.get(context);
        currentFilters.readFrom(sharedPreferences);
        return currentFilters;
    }

    public static Filters getReadedFrom(View view) {
        Filters filters = new Filters();
        filters.readFrom(view);
        return filters;
    }

    public Filters() {
        filters = FiltersProvider.getFilters();
    }

    public void readFrom(final SharedPreferences sharedPreferences) {
        forEachFilter(new OnEachHandler<Filter>() {
            @Override
            public void onEach(Filter each) {
                each.readFrom(sharedPreferences);
            }
        });
    }

    private void forEachFilter(OnEachHandler<Filter> onEachHandler) {
        for (Filter each : filters) {
            onEachHandler.onEach(each);
        }
    }

    private void forEachActiveFilter(OnEachHandler<Filter> onEachHandler) {
        for (Filter each : filters) {
            if (each.isActivated()) {
                onEachHandler.onEach(each);
            }
        }
    }

    public void writeTo(final SharedPreferences.Editor editor) {
        forEachFilter(new OnEachHandler<Filter>() {
            @Override
            public void onEach(Filter each) {
                each.writeTo(editor);
            }
        });
    }

    public Set<String> getColumnsOfPartnerPoint() {
        final Set<String> columns = new HashSet<String>();
        forEachActiveFilter(new OnEachHandler<Filter>() {
            @Override
            public void onEach(Filter each) {
                columns.addAll(each.getColumnsOfPartnerPoint());
            }
        });
        return columns;
    }

    public Filter.Checker getChecker() {
        final List<Filter.Checker> activeCheckers = getActiveCheckers();
        if (activeCheckers.isEmpty()) {
            return new DummyChecker(true);
        } else if (activeCheckers.size() == 1) {
            return activeCheckers.get(0);
        } else {
            return new CombinedChecker(activeCheckers);
        }
    }

    private List<Filter.Checker> getActiveCheckers() {
        final List<Filter.Checker> activeCheckers = new ArrayList<Filter.Checker>();
        forEachActiveFilter(new OnEachHandler<Filter>() {
            @Override
            public void onEach(Filter each) {
                activeCheckers.add(each.getChecker());
            }
        });
        return activeCheckers;
    }

    private static class CombinedChecker implements Filter.Checker {
        private final Collection<Filter.Checker> checkers;

        public CombinedChecker(Collection<Filter.Checker> checkers) {
            this.checkers = checkers;
        }

        @Override
        public boolean isMeet(PartnerPoint partnerPoint, Cursor cursor) {
            for (Filter.Checker each : checkers) {
                if (!each.isMeet(partnerPoint, cursor)) {
                    return false;
                }
            }
            return true;
        }
    }

    public View inflateContentView(final Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        final LinearLayout filtersLayout = (LinearLayout) layoutInflater.inflate(R.layout.vertical_linear_layout, null);
        forEachFilter(new OnEachHandler<Filter>() {
            @Override
            public void onEach(Filter each) {
                filtersLayout.addView(each.inflateContentView(context));
            }
        });
        return filtersLayout;
    }

    public void displayOn(final View view) {
        forEachFilter(new OnEachHandler<Filter>() {
            @Override
            public void onEach(Filter each) {
                each.displayOn(view);
            }
        });
    }

    public void readFrom(final View view) {
        forEachFilter(new OnEachHandler<Filter>() {
            @Override
            public void onEach(Filter each) {
                each.readFrom(view);
            }
        });
    }

}
