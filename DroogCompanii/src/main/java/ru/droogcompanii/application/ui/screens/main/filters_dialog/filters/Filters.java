package ru.droogcompanii.application.ui.screens.main.filters_dialog.filters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.util.OnEachHandler;

/**
 * Created by ls on 25.03.14.
 */
public class Filters implements Serializable {

    public static Filters getCurrent(Context context) {
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


    private final List<FilterHelper> filters;

    public Filters() {
        filters = FilterHelpersProvider.get();
    }

    public void readFrom(final SharedPreferences sharedPreferences) {
        forEachFilter(new OnEachHandler<FilterHelper>() {
            @Override
            public void onEach(FilterHelper each) {
                each.readFrom(sharedPreferences);
            }
        });
    }

    private void forEachFilter(OnEachHandler<FilterHelper> onEachHandler) {
        for (FilterHelper each : filters) {
            onEachHandler.onEach(each);
        }
    }

    private void forEachActiveFilter(OnEachHandler<FilterHelper> onEachHandler) {
        for (FilterHelper each : filters) {
            if (each.isActive()) {
                onEachHandler.onEach(each);
            }
        }
    }

    public void writeTo(final SharedPreferences.Editor editor) {
        forEachFilter(new OnEachHandler<FilterHelper>() {
            @Override
            public void onEach(FilterHelper each) {
                each.writeTo(editor);
            }
        });
    }

    public Set<String> getColumnsOfPartnerPoint() {
        final Set<String> columns = new HashSet<String>();
        forEachActiveFilter(new OnEachHandler<FilterHelper>() {
            @Override
            public void onEach(FilterHelper each) {
                columns.addAll(each.getColumnsOfPartnerPoint());
            }
        });
        return columns;
    }

    public Filter getActiveFilter() {
        final List<Filter> activeFilters = prepareActiveFilters();
        if (activeFilters.isEmpty()) {
            return new DummyFilter(true);
        } else if (activeFilters.size() == 1) {
            return activeFilters.get(0);
        } else {
            return new CombinedFilter(activeFilters);
        }
    }

    private List<Filter> prepareActiveFilters() {
        final List<Filter> active = new ArrayList<Filter>();
        forEachActiveFilter(new OnEachHandler<FilterHelper>() {
            @Override
            public void onEach(FilterHelper each) {
                active.add(each.getFilter());
            }
        });
        return active;
    }

    public View inflateContentView(final Context context) {
        final ViewGroup container = prepareContainer(context);
        forEachFilter(new OnEachHandler<FilterHelper>() {
            @Override
            public void onEach(FilterHelper each) {
                container.addView(each.inflateContentView(context));
            }
        });
        return container;
    }

    private static ViewGroup prepareContainer(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return (ViewGroup) layoutInflater.inflate(R.layout.vertical_linear_layout, null);
    }

    public void displayOn(final View view) {
        forEachFilter(new OnEachHandler<FilterHelper>() {
            @Override
            public void onEach(FilterHelper each) {
                each.displayOn(view);
            }
        });
    }

    public void readFrom(final View view) {
        forEachFilter(new OnEachHandler<FilterHelper>() {
            @Override
            public void onEach(FilterHelper each) {
                each.readFrom(view);
            }
        });
    }

    public void share(Context context) {
        SharedPreferences sharedPreferences = FiltersSharedPreferencesProvider.get(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        writeTo(editor);
        editor.commit();
    }
}
