package ru.droogcompanii.application.ui.screens.main.filters_dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import ru.droogcompanii.application.ui.screens.main.filters_dialog.filters.Filters;
import ru.droogcompanii.application.ui.screens.main.filters_dialog.filters.FiltersSharedPreferencesProvider;
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
            // do nothing
        }

        @Override
        public void onFiltersCancel() {
            // do nothing
        }
    };

    private static class Key {
        public static final String FILTERS = "FILTERS" + FiltersDialogFragment.class.getName();
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
        displayedFilters = getDisplayedFilters();
    }

    private Filters getDisplayedFilters() {
        return Filters.getReadedFrom(dialog.getContentView());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveFiltersState(outState);
    }

    private void saveFiltersState(Bundle outState) {
        outState.putSerializable(Key.FILTERS, displayedFilters);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = prepareDialog(getFilters(savedInstanceState));
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    private Filters getFilters(Bundle savedInstanceState) {
        return (savedInstanceState == null)
                ? Filters.getCurrent(getActivity())
                : (Filters) savedInstanceState.getSerializable(Key.FILTERS);
    }

    private FiltersDialog prepareDialog(Filters filters) {
        return new FiltersDialog(getActivity(), filters, new Callbacks() {
            @Override
            public void onFiltersDone() {
                shareFilters();
                callbacks.onFiltersDone();
                dismiss();
            }

            @Override
            public void onFiltersCancel() {
                callbacks.onFiltersCancel();
                dismiss();
            }
        });
    }

    private void shareFilters() {
        SharedPreferences sharedPreferences = FiltersSharedPreferencesProvider.get(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        getDisplayedFilters().writeTo(editor);
        editor.commit();
    }
}
