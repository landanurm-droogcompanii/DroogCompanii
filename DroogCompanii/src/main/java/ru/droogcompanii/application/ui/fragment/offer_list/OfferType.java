package ru.droogcompanii.application.ui.fragment.offer_list;

import ru.droogcompanii.application.R;

/**
 * Created by Leonid on 08.03.14.
 */
public enum OfferType {
    ALL(R.string.offer_type_all),
    SPECIAL(R.string.offer_type_special),
    ACTUAL(R.string.offer_type_actual),
    PAST(R.string.offer_type_past);


    private final int titleId;

    OfferType(int titleId) {
        this.titleId = titleId;
    }

    public int getTitleId() {
        return titleId;
    }
}
