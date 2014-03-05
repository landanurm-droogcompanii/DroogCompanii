package ru.droogcompanii.application.ui.util.caller;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ls on 03.02.14.
 */
public class MultiPhonesCallerDialogFragment extends DialogFragment {

    private static final String KEY_TITLE = "KEY_TITLE";
    private static final String KEY_PHONES = "KEY_PHONES";

    private List<String> phones;
    private String title;

    public static DialogFragment newInstance(String title, List<String> phones) {
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        args.putSerializable(KEY_PHONES, (Serializable) phones);
        DialogFragment dialogFragment = MultiPhonesCallerDialogFragment.newInstance(title, phones);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        Bundle bundle = (savedInstanceState == null) ? getArguments() : savedInstanceState;
        phones = (List<String>) bundle.getSerializable(KEY_PHONES);
        title = bundle.getString(KEY_TITLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_PHONES, (Serializable) phones);
        outState.putString(KEY_TITLE, title);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new MultiPhonesCallerDialog(getActivity(), phones);
        dialog.setTitle(title);
        return dialog;
    }
}
