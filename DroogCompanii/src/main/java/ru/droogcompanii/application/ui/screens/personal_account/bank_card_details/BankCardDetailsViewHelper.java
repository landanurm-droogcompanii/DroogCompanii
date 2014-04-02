package ru.droogcompanii.application.ui.screens.personal_account.bank_card_details;

import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.personal_details.BankCard;
import ru.droogcompanii.application.data.personal_details.CardTransaction;
import ru.droogcompanii.application.util.ui.view.SimpleLayoutInflater;

/**
 * Created by ls on 24.02.14.
 */
public class BankCardDetailsViewHelper {

    private static final int LAYOUT_ID = R.layout.fragment_bank_card_details;

    private final SimpleLayoutInflater inflater;
    private View rootView;

    public BankCardDetailsViewHelper(LayoutInflater inflater) {
        this.inflater = new SimpleLayoutInflater(inflater);
    }

    public View getView() {
        prepareViewIfNeed();
        return rootView;
    }

    private void prepareViewIfNeed() {
        if (rootView == null) {
            rootView = inflater.inflate(LAYOUT_ID);
        }
    }

    public void display(BankCard bankCard) {
        prepareViewIfNeed();
        displayCardDetails(bankCard);
        displayTransactions(bankCard.getTransactions());
    }

    private void displayCardDetails(BankCard bankCard) {

    }

    private void displayTransactions(List<CardTransaction> transactions) {

    }
}
