package ru.droogcompanii.application.ui.main_screen_2.filters;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.main_screen_2.filters.filters_impl.Filters;

/**
 * Created by ls on 25.03.14.
 */
public class FiltersDialog extends Dialog {

    private final FiltersDialogFragment.Callbacks callbacks;
    private final Filters filters;
    private View contentView;

    public FiltersDialog(Context context, Filters filters, FiltersDialogFragment.Callbacks callbacks) {
        super(context);
        this.callbacks = callbacks;
        this.filters = filters;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.contentView = prepareContentView();
        setContentView(contentView);
    }

    private View prepareContentView() {
        Context context = getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ScrollView filtersContentView = (ScrollView) layoutInflater.inflate(R.layout.view_scroll, null);
        filtersContentView.addView(filters.inflateContentView(context));
        filters.displayOn(filtersContentView);
        ViewGroup contentView = (ViewGroup) layoutInflater.inflate(R.layout.vertical_linear_layout, null);
        contentView.addView(filtersContentView);
        contentView.addView(prepareClearDonePanel(layoutInflater));
        return contentView;
    }

    private View prepareClearDonePanel(LayoutInflater layoutInflater) {
        View view = layoutInflater.inflate(R.layout.view_clear_done_panel, null);
        view.findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbacks.onClear();
            }
        });
        view.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbacks.onDone();
            }
        });
        return view;
    }

    public View getContentView() {
        return contentView;
    }


}
