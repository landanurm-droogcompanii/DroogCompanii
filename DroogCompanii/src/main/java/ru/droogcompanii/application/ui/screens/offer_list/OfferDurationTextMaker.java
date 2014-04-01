package ru.droogcompanii.application.ui.screens.offer_list;

import android.content.Context;

import java.util.Calendar;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.offers.CalendarRange;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.util.CalendarUtils;

/**
 * Created by ls on 01.04.14.
 */
class OfferDurationTextMaker {
    private final Context context;

    public OfferDurationTextMaker(Context context) {
        this.context = context;
    }

    public String makeFrom(Offer offer) {
        if (offer.isSpecial()) {
            return context.getString(R.string.special_offer);
        }
        CalendarRange duration = offer.getDuration();
        return makeFrom(duration.from(), duration.to());
    }

    private String makeFrom(Calendar from, Calendar to) {
        String textFrom = CalendarUtils.convertToString(from);
        String textTo = CalendarUtils.convertToString(to);
        return (context.getString(R.string.offer_duration_before_from) + textFrom +
                context.getString(R.string.offer_duration_before_to) + textTo +
                context.getString(R.string.offer_duration_after_to));
    }
}
