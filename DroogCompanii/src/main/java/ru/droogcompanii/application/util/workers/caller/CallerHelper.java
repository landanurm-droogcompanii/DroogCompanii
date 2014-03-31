package ru.droogcompanii.application.util.workers.caller;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

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
                    call(partnerPoint.getTitle(), partnerPoint.getPhones());
                }
            });
        }
    }

    public void call(String title, List<String> phones) {
        showMultiPhonesDialogFragment(title, phones);
    }

    private void showMultiPhonesDialogFragment(String title, List<String> phones) {
        DialogFragment dialogFragment = MultiPhonesCallerDialogFragment.newInstance(title, phones);
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        dialogFragment.show(fragmentManager, "MultiPhonesCallerDialogFragment");
    }
}
