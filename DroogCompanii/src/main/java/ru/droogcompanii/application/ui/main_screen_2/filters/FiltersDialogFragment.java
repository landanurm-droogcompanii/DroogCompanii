package ru.droogcompanii.application.ui.main_screen_2.filters;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import ru.droogcompanii.application.ui.main_screen_2.filters.filters_impl.Filters;
import ru.droogcompanii.application.ui.main_screen_2.filters.filters_impl.FiltersSharedPreferencesProvider;

/**
 * Created by ls on 25.03.14.
 */
public class FiltersDialogFragment extends DialogFragment {

    public static interface Callbacks {
        void onDone();
        void onClear();
    }

    private static class Key {
        public static final String FILTERS = "FILTERS" + FiltersDialogFragment.class.getName();
    }


    public static void show(FragmentActivity fragmentActivity) {
        DialogFragment dialogFragment = new FiltersDialogFragment();
        String tag = dialogFragment.getClass().getName();
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        dialogFragment.show(fragmentManager, tag);
    }


    private static final Callbacks DUMMY_CALLBACKS = new Callbacks() {
        @Override
        public void onDone() {
            // do nothing
        }

        @Override
        public void onClear() {
            // do nothing
        }
    };


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
        displayedFilters = getDisplayedFilters();
    }

    private Filters getDisplayedFilters() {
        return Filters.getReadedFrom(getContentView());
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
        Filters filters = (savedInstanceState == null)
                ? Filters.getCurrentFilters(getActivity())
                : (Filters) savedInstanceState.getSerializable(Key.FILTERS);
        dialog = prepareDialog(filters);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    private FiltersDialog prepareDialog(Filters filters) {
        return new FiltersDialog(getActivity(), filters, new Callbacks() {
            @Override
            public void onDone() {
                shareFilters();
                callbacks.onDone();
                dismiss();
            }

            @Override
            public void onClear() {
                callbacks.onClear();
                dismiss();
            }
        });
    }

    private void shareFilters() {
        Filters filters = Filters.getReadedFrom(getContentView());
        SharedPreferences sharedPreferences = FiltersSharedPreferencesProvider.get(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        filters.writeTo(editor);
        editor.commit();
    }

    private View getContentView() {
        return dialog.getContentView();
    }
}
