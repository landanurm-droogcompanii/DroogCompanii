package ru.droogcompanii.application.ui.main_screen_2.filters_dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.main_screen_2.filters_dialog.filters.Filters;

/**
 * Created by ls on 25.03.14.
 */
public class FiltersDialog extends Dialog {

    private final Filters filters;
    private final FiltersDialogFragment.Callbacks callbacks;
    private View contentView;

    public FiltersDialog(Context context, Filters filters, FiltersDialogFragment.Callbacks callbacks) {
        super(context);
        this.callbacks = callbacks;
        this.filters = filters;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView = prepareContentView();
        setContentView(contentView);
    }

    private View prepareContentView() {
        View contentView = wrapInScrollView(prepareFiltersContentView());
        View panelClearDone = prepareCancelDonePanel();
        return combineIntoVerticalLinearLayout(contentView, panelClearDone);
    }

    private View wrapInScrollView(View view) {
        ScrollView scrollView = (ScrollView) inflateLayout(R.layout.view_scroll);
        scrollView.addView(view);
        return scrollView;
    }

    private View inflateLayout(int layoutId) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        return layoutInflater.inflate(layoutId, null);
    }

    private View prepareFiltersContentView() {
        View filtersContentView = filters.inflateContentView(getContext());
        filters.displayOn(filtersContentView);
        return filtersContentView;
    }

    private View prepareCancelDonePanel() {
        View panel = inflateLayout(R.layout.view_cancel_done_panel);
        panel.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
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

    public View getContentView() {
        return contentView;
    }


}
