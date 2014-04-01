package ru.droogcompanii.application.ui.screens.main.filters_dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import ru.droogcompanii.application.ui.screens.main.filters_dialog.filters.Filters;
import ru.droogcompanii.application.util.LogUtils;
import ru.droogcompanii.application.util.ui.fragment.FragmentUtils;

/**
 * Created by ls on 25.03.14.
 */
public class FiltersDialogFragment extends DialogFragment {

    public static interface Callbacks {
        void onFiltersDone();
        void onFiltersCancel();
    }

    private static final Callbacks DUMMY_CALLBACKS = new Callbacks() {
        @Override
        public void onFiltersDone() {
            LogUtils.debug("DUMMY_CALLBACKS.onFiltersDone()");
        }

        @Override
        public void onFiltersCancel() {
            LogUtils.debug("DUMMY_CALLBACKS.onFiltersCancel()");
        }
    };

    private static class Key {
        public static final String FILTERS = "FILTERS";
    }


    public static void show(FragmentActivity fragmentActivity) {
        FragmentUtils.show(new FiltersDialogFragment(), fragmentActivity);
    }


    private Callbacks callbacks;
    private Filters displayedFilters;
    private FiltersDialog dialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        callbacks = DUMMY_CALLBACKS;
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
        // need to read displayed filters here, because
        // in onSaveInstanceState() <dialog> or <content view> may be not available
        displayedFilters = readDisplayedFilters();
    }

    private Filters readDisplayedFilters() {
        View filtersView = dialog.getContentView();
        return Filters.getReadedFrom(filtersView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveDisplayedFiltersState(outState);
    }

    private void saveDisplayedFiltersState(Bundle outState) {
        outState.putSerializable(Key.FILTERS, displayedFilters);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = prepareDialog(savedInstanceState);
        return dialog;
    }

    private FiltersDialog prepareDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        Filters filters = (savedInstanceState == null) ? Filters.getCurrent(context) : restoreFilters(savedInstanceState);
        FiltersDialog filtersDialog = new FiltersDialog(context, filters, ACTUAL_CALLBACKS);
        filtersDialog.setCanceledOnTouchOutside(false);
        return filtersDialog;
    }

    private static Filters restoreFilters(Bundle savedInstanceState) {
        return (Filters) savedInstanceState.getSerializable(Key.FILTERS);
    }

    private final Callbacks ACTUAL_CALLBACKS = new Callbacks() {
        @Override
        public void onFiltersDone() {
            shareDisplayedFilters();
            callbacks.onFiltersDone();
            dismiss();
        }

        @Override
        public void onFiltersCancel() {
            callbacks.onFiltersCancel();
            dismiss();
        }
    };

    private void shareDisplayedFilters() {
        readDisplayedFilters().share(getActivity());
    }
}
