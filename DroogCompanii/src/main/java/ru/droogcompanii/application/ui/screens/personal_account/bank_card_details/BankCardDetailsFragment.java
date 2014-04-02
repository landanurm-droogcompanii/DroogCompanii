package ru.droogcompanii.application.ui.screens.personal_account.bank_card_details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

import ru.droogcompanii.application.data.personal_details.BankCard;
import ru.droogcompanii.application.util.StateManager;

/**
 * Created by ls on 24.02.14.
 */
public class BankCardDetailsFragment extends Fragment {

    private static class Key {
        public static final String BANK_CARD = "BANK_CARD";
    }


    private BankCard bankCard;


    private final StateManager STATE_MANAGER = new StateManager() {
        @Override
        public void initStateByDefault() {
            bankCard = extractBankCard(getArguments());
        }

        @Override
        public void restoreState(Bundle savedInstanceState) {
            bankCard = extractBankCard(savedInstanceState);
        }

        private BankCard extractBankCard(Bundle bundle) {
            return (BankCard) bundle.getSerializable(Key.BANK_CARD);
        }

        @Override
        public void saveState(Bundle outState) {
            outState.putSerializable(Key.BANK_CARD, (Serializable) bankCard);
        }
    };


    public static BankCardDetailsFragment newInstance(BankCard bankCard) {
        BankCardDetailsFragment fragment = new BankCardDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(Key.BANK_CARD, (Serializable) bankCard);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        STATE_MANAGER.initState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        STATE_MANAGER.saveState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        changeActivityTitle();
    }

    private void changeActivityTitle() {
        String newActivityTitle = bankCard.getTitle();
        getActivity().setTitle(newActivityTitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        BankCardDetailsViewHelper viewHelper = new BankCardDetailsViewHelper(inflater);
        viewHelper.display(bankCard);
        return viewHelper.getView();
    }
}
