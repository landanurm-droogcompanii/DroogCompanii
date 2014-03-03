package ru.droogcompanii.application.ui.fragment.bank_card_details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.personal_details.BankCard;
import ru.droogcompanii.application.data.personal_details.CardTransaction;

/**
 * Created by ls on 24.02.14.
 */
public class BankCardDetailsViewHelper {

    private static final int LAYOUT_ID = R.layout.fragment_bank_card_details;


    private static interface ViewMaker {
        View make();
    }

    private final ViewMaker layoutViewMaker;
    private View rootView;

    public BankCardDetailsViewHelper(Context context, final LayoutInflater inflater) {
        layoutViewMaker = new ViewMaker() {
            @Override
            public View make() {
                return inflater.inflate(LAYOUT_ID, null);
            }
        };
    }

    public View getView() {
        makeViewIfNeed();
        return rootView;
    }

    private void makeViewIfNeed() {
        if (rootView == null) {
            rootView = layoutViewMaker.make();
        }
    }

    public void display(BankCard bankCard) {
        makeViewIfNeed();
        displayCardDetails(bankCard);
        displayTransactions(bankCard.getTransactions());
    }

    private void displayCardDetails(BankCard bankCard) {

    }

    private void displayTransactions(List<CardTransaction> transactions) {

    }
}
