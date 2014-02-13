package ru.droogcompanii.application.util.caller_helper;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.ui.helpers.Caller;
import ru.droogcompanii.application.util.Keys;

/**
 * Created by ls on 12.02.14.
 */
public class CallerHelper {

    private final FragmentActivity activity;

    public CallerHelper(FragmentActivity activity) {
        this.activity = activity;
    }

    public void initPhoneButton(View phoneButton, final PartnerPoint partnerPoint) {
        if (partnerPoint.getPhones().isEmpty()) {
            phoneButton.setVisibility(View.INVISIBLE);
        } else {
            phoneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onPhoneButtonClick(partnerPoint);
                }
            });
        }
    }

    public void onPhoneButtonClick(PartnerPoint partnerPoint) {
        List<String> phones = partnerPoint.getPhones();
        if (phones.size() == 1) {
            String phone = phones.get(0);
            Caller.call(activity, phone);
        } else {
            showMultiPhonesDialogFragment(partnerPoint);
        }
    }

    private void showMultiPhonesDialogFragment(PartnerPoint partnerPoint) {
        DialogFragment dialogFragment = new MultiPhonesCallerDialogFragment();
        dialogFragment.setArguments(prepareArguments(partnerPoint));
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        dialogFragment.show(fragmentManager, "MultiPhonesCallerDialogFragment");
    }

    private static Bundle prepareArguments(PartnerPoint partnerPoint) {
        Bundle args = new Bundle();
        args.putString(Keys.title, partnerPoint.getTitle());
        args.putSerializable(Keys.phones, (Serializable) partnerPoint.getPhones());
        return args;
    }
}
