package ru.droogcompanii.application.ui.fragment.personal_details;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.droogcompanii.application.data.personal_details.PersonalDetails;
import ru.droogcompanii.application.data.personal_details.bank_card.BankCard;
import ru.droogcompanii.application.ui.activity.personal_account.DetailsReceiver;
import ru.droogcompanii.application.ui.activity.personal_account.PersonalAccountActivity;

/**
 * Created by ls on 21.02.14.
 */
public class PersonalDetailsFragment extends Fragment implements DetailsReceiver {

    private static final OnBankCardSelectedListener DUMMY_ON_BANK_CARD_SELECTED_LISTENER =
            new OnBankCardSelectedListener() {
        @Override
        public void onBankCardSelected(BankCard bankCard) {
            // do nothing
        }
    };

    private PersonalDetailsViewHelper detailsViewHelper;
    private OnBankCardSelectedListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (OnBankCardSelectedListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = DUMMY_ON_BANK_CARD_SELECTED_LISTENER;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        detailsViewHelper = new PersonalDetailsViewHelper(inflater, new OnBankCardSelectedListener() {
            @Override
            public void onBankCardSelected(BankCard bankCard) {
                listener.onBankCardSelected(bankCard);
            }
        });
        return detailsViewHelper.getView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setDefaultActivityTitle();

        requestDetails();
    }

    private void setDefaultActivityTitle() {
        PersonalAccountActivity activity = (PersonalAccountActivity) getActivity();
        activity.setDefaultTitle();
    }

    private void requestDetails() {
        PersonalAccountActivity activity = (PersonalAccountActivity) getActivity();
        activity.requestDetails();
    }

    @Override
    public void onReceiveDetails(PersonalDetails details) {
        detailsViewHelper.displayDetails(details);
    }
}
