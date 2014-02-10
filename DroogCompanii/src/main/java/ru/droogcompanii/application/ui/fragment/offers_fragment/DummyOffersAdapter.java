package ru.droogcompanii.application.ui.fragment.offers_fragment;

import android.content.Context;

import java.util.List;

import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.ui.helpers.SimpleArrayAdapter;

/**
 * Created by ls on 10.02.14.
 */
public class DummyOffersAdapter extends SimpleArrayAdapter<Offer> {
    public DummyOffersAdapter(Context context, List<Offer> offers) {
        super(context, offers, new ItemToTitleConvertor<Offer>() {
            @Override
            public String getTitle(Offer item) {
                return item.toString();
            }
        });
    }
}
