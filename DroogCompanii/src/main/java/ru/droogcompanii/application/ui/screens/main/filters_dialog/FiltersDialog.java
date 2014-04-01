package ru.droogcompanii.application.ui.screens.main.filters_dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import ru.droogcompanii.application.ui.screens.main.filters_dialog.filters.Filters;

/**
 * Created by ls on 25.03.14.
 */
public class FiltersDialog extends Dialog {

    private final FiltersViewMaker filtersViewMaker;
    private View contentView;

    public FiltersDialog(Context context, Filters filters, FiltersDialogFragment.Callbacks callbacks) {
        super(context);
        filtersViewMaker = new FiltersViewMaker(context, filters, callbacks);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView = filtersViewMaker.make();
        setContentView(contentView);
    }

    public View getContentView() {
        return contentView;
    }


}
