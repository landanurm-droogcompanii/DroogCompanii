package ru.droogcompanii.application.ui.util.caller;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 03.02.14.
 */
public class MultiPhonesCallerDialogFragment extends DialogFragment {

    private List<String> phones;
    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        Bundle bundle = (savedInstanceState == null) ? getArguments() : savedInstanceState;
        phones = (List<String>) bundle.getSerializable(Keys.phones);
        title = bundle.getString(Keys.title);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.phones, (Serializable) phones);
        outState.putString(Keys.title, title);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new MultiPhonesCallerDialog(getActivity(), phones);
        dialog.setTitle(title);
        return dialog;
    }

}
