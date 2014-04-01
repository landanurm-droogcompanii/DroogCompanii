package ru.droogcompanii.application.ui.screens.main.filters_dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.screens.main.filters_dialog.filters.Filters;

/**
 * Created by ls on 01.04.14.
 */
public class FiltersViewMaker {
    private final Context context;
    private final FiltersDialogFragment.Callbacks callbacks;
    private final Filters filters;

    public FiltersViewMaker(Context context, Filters filters, FiltersDialogFragment.Callbacks callbacks) {
        this.context = context;
        this.filters = filters;
        this.callbacks = callbacks;
    }

    public View make() {
        View mainContent = wrapInScrollView(prepareFiltersContentView());
        View panelClearDone = prepareCancelDonePanel();
        return combineIntoVerticalLinearLayout(mainContent, panelClearDone);
    }

    private View wrapInScrollView(View view) {
        ScrollView scrollView = (ScrollView) inflateLayout(R.layout.view_scroll);
        scrollView.addView(view);
        return scrollView;
    }

    private View inflateLayout(int layoutId) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return layoutInflater.inflate(layoutId, null);
    }

    private View prepareFiltersContentView() {
        View filtersContentView = filters.inflateContentView(context);
        filters.displayOn(filtersContentView);
        return filtersContentView;
    }

    private View prepareCancelDonePanel() {
        View panel = inflateLayout(R.layout.view_cancel_done_panel);
        panel.findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbacks.onFiltersCancel();
            }
        });
        panel.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbacks.onFiltersDone();
            }
        });
        return panel;
    }

    private View combineIntoVerticalLinearLayout(View... viewsToCombine) {
        ViewGroup viewGroup = (ViewGroup) inflateLayout(R.layout.vertical_linear_layout);
        for (View each : viewsToCombine) {
            viewGroup.addView(each);
        }
        return viewGroup;
    }
}
