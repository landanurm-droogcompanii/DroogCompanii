package ru.droogcompanii.application.ui.screens.main.map.notifier_location_service;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import ru.droogcompanii.application.util.ui.fragment.FragmentUtils;

/**
 * Created by ls on 31.03.14.
 */
public class NotifierLocationServiceDialogFragment extends DialogFragment implements NotifierLocationServiceDialog.Callbacks {

    public static void showIfNeed(FragmentActivity fragmentActivity) {
        boolean doNotShowAgain = FlagDoNotShowAgain.read(fragmentActivity);
        if (doNotShowAgain) {
            return;
        }
        DialogFragment dialogFragment = new NotifierLocationServiceDialogFragment();
        FragmentUtils.showDialogFragment(dialogFragment, fragmentActivity);
    }

    private NotifierLocationServiceDialog dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new NotifierLocationServiceDialog(getActivity(), this);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onNeedToCloseDialog() {
        dismiss();
    }
}
