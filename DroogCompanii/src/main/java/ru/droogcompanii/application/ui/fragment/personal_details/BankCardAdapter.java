package ru.droogcompanii.application.ui.fragment.personal_details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.personal_details.BankCard;

/**
 * Created by ls on 24.02.14.
 */
public class BankCardAdapter extends ArrayAdapter<BankCard> {
    private static final int ROW_LAYOUT_RESOURCE_ID = R.layout.view_bank_card_list_item;

    public BankCardAdapter(Context context, List<BankCard> cards) {
        super(context, ROW_LAYOUT_RESOURCE_ID, cards);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            itemView = layoutInflater.inflate(ROW_LAYOUT_RESOURCE_ID, null);
        }
        fill(itemView, getItem(position));
        return itemView;
    }

    private void fill(View itemView, BankCard item) {
        TextView titleOfBankCard = (TextView) itemView.findViewById(R.id.titleOfBankCard);
        titleOfBankCard.setText(item.getTitle());
    }

}
