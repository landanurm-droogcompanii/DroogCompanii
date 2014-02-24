package ru.droogcompanii.application.ui.fragment.bank_card_details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.personal_details.bank_card.BankCard;
import ru.droogcompanii.application.data.personal_details.bank_card.CardOwner;
import ru.droogcompanii.application.data.personal_details.bank_card.CardTransaction;

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

    public void fill(BankCard bankCard) {
        makeViewIfNeed();
        setOwner(bankCard.getOwner());
        setTransactions(bankCard.getTransactions());
    }

    private void setTransactions(List<CardTransaction> transactions) {

    }

    private void setOwner(CardOwner owner) {

    }
}
