package ru.droogcompanii.application.ui.screens.personal_account.personal_details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.personal_details.AccountOwner;
import ru.droogcompanii.application.data.personal_details.BankCard;
import ru.droogcompanii.application.util.ui.view.SimpleLayoutInflater;

/**
 * Created by ls on 21.02.14.
 */
public class PersonalDetailsViewHelper {

    private final OnBankCardSelectedListener onBankCardSelectedListener;
    private final SimpleLayoutInflater inflater;
    private View detailsView;
    private View rootView;


    public PersonalDetailsViewHelper(final LayoutInflater inflater, OnBankCardSelectedListener listener) {
        this.inflater = new SimpleLayoutInflater(inflater);
        onBankCardSelectedListener = listener;
    }

    public View getView() {
        if (rootView == null) {
            prepareView();
        }
        return rootView;
    }

    private void prepareView() {
        rootView = inflater.inflate(R.layout.fragment_personal_details);
        connectDetailsViewToRoot();
    }

    public void connectDetailsViewToRoot() {
        ViewGroup containerOfDetails = (ViewGroup) rootView.findViewById(R.id.containerOfPersonalDetails);
        containerOfDetails.removeAllViews();
        detailsView = inflater.inflate(R.layout.view_personal_details);
        containerOfDetails.addView(detailsView);
    }

    public void displayDetails(AccountOwner accountOwner) {
        setTextDetails(accountOwner);
        setCards(accountOwner);
    }

    private void setTextDetails(AccountOwner accountOwner) {
        setText(R.id.firstName, accountOwner.getFirstName());
        setText(R.id.lastName, accountOwner.getLastName());
    }

    private void setCards(AccountOwner accountOwner) {
        Context context = detailsView.getContext();
        List<BankCard> cards = accountOwner.getBankCards();
        final BankCardAdapter adapter = new BankCardAdapter(context, cards);
        ListView cardsListView = (ListView) detailsView.findViewById(R.id.cardListView);
        cardsListView.setAdapter(adapter);
        cardsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                BankCard card = adapter.getItem(position);
                onBankCardSelectedListener.onBankCardSelected(card);
            }
        });
    }

    private void setText(int textViewId, String text) {
        TextView textView = (TextView) findView(textViewId);
        textView.setText(text);
    }

    private View findView(int id) {
        return detailsView.findViewById(id);
    }
}
