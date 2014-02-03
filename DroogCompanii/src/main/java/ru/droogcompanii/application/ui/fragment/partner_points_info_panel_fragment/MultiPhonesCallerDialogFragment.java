package ru.droogcompanii.application.ui.fragment.partner_points_info_panel_fragment;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phones = extractPhones(savedInstanceState);
    }

    private List<String> extractPhones(Bundle savedInstanceState) {
        Bundle bundle = (savedInstanceState == null) ? getArguments() : savedInstanceState;
        return (List<String>) bundle.getSerializable(Keys.phones);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.phones, (Serializable) phones);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MultiPhonesCallerDialog(getActivity(), phones);
    }

}
