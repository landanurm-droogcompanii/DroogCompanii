package ru.droogcompanii.application.ui.fragment.bank_card_details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

import ru.droogcompanii.application.data.personal_details.bank_card.BankCard;
import ru.droogcompanii.application.ui.activity.personal_account.PersonalAccountActivity;

/**
 * Created by ls on 24.02.14.
 */
public class BankCardDetailsFragment extends Fragment {

    private static final String KEY_BANK_CARD = PersonalAccountActivity.KEY_BANK_CARD;

    private BankCard bankCard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            initStateByDefault();
        } else {
            restoreState(savedInstanceState);
        }
        changeActivityTitle();
    }

    private void initStateByDefault() {
        bankCard = extractBankCard(getArguments());
    }

    private void restoreState(Bundle savedInstanceState) {
        bankCard = extractBankCard(savedInstanceState);
    }

    private static BankCard extractBankCard(Bundle bundle) {
        return (BankCard) bundle.getSerializable(KEY_BANK_CARD);
    }

    private void changeActivityTitle() {
        String newActivityTitle = bankCard.getTitle();
        getActivity().setTitle(newActivityTitle);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveStateInto(outState);
    }

    private void saveStateInto(Bundle outState) {
        outState.putSerializable(KEY_BANK_CARD, (Serializable) bankCard);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BankCardDetailsViewHelper viewHelper = new BankCardDetailsViewHelper(getActivity(), inflater);
        viewHelper.display(bankCard);
        return viewHelper.getView();
    }
}
