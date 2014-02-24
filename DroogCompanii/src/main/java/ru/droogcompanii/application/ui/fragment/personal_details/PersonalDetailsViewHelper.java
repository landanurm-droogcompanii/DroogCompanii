package ru.droogcompanii.application.ui.fragment.personal_details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.personal_details.PersonalDetails;
import ru.droogcompanii.application.data.personal_details.bank_card.BankCard;

/**
 * Created by ls on 21.02.14.
 */
public class PersonalDetailsViewHelper {


    private static interface ViewMaker {
        View make(int layoutId);
    }

    private final OnBankCardSelectedListener onBankCardSelectedListener;
    private final ViewMaker viewMaker;
    private View detailsView;
    private View rootView;


    public PersonalDetailsViewHelper(final LayoutInflater inflater, OnBankCardSelectedListener listener) {
        viewMaker = new ViewMaker() {
            @Override
            public View make(int layoutId) {
                return inflater.inflate(layoutId, null);
            }
        };
        onBankCardSelectedListener = listener;
    }

    public View getView() {
        if (rootView == null) {
            prepareView();
        }
        return rootView;
    }

    private void prepareView() {
        rootView = viewMaker.make(R.layout.fragment_personal_details);
        connectDetailsViewToRoot();
    }

    public void connectDetailsViewToRoot() {
        ViewGroup containerOfDetails = (ViewGroup) rootView.findViewById(R.id.containerOfPersonalDetails);
        containerOfDetails.removeAllViews();
        detailsView = viewMaker.make(R.layout.view_personal_details);
        containerOfDetails.addView(detailsView);
    }

    public void displayDetails(PersonalDetails personalDetails) {
        setTextDetails(personalDetails);
        setCards(personalDetails);
    }

    private void setTextDetails(PersonalDetails personalDetails) {
        setText(R.id.firstName, personalDetails.getFirstName());
        setText(R.id.lastName, personalDetails.getLastName());
    }

    private void setCards(PersonalDetails personalDetails) {
        Context context = detailsView.getContext();
        List<BankCard> cards = personalDetails.getCards();
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
