package ru.droogcompanii.application.ui.screens.offer_list;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Leonid on 08.03.14.
 */
public class OfferTypeSpinnerAdapter extends ArrayAdapter<String> {

    private static class OfferTypeItems {
        private static final List<OfferType> ITEMS = Arrays.asList(
                OfferType.ALL,
                OfferType.SPECIAL,
                OfferType.ACTUAL,
                OfferType.PAST
        );

        public static OfferType getItem(int position) {
            return ITEMS.get(position);
        }

        public static List<String> getTitles(Context context) {
            List<String> titles = new ArrayList<String>(ITEMS.size());
            for (OfferType item : ITEMS) {
                String title = context.getString(item.getTitleId());
                titles.add(title);
            }
            return titles;
        }
    }


    public OfferTypeSpinnerAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1, OfferTypeItems.getTitles(context));
    }

    public static OfferType getOfferTypeByPosition(int position) {
        return OfferTypeItems.getItem(position);
    }
}
