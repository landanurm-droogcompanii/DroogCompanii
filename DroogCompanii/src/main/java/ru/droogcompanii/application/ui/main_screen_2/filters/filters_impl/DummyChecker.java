package ru.droogcompanii.application.ui.main_screen_2.filters.filters_impl;

import android.database.Cursor;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by ls on 25.03.14.
 */
class DummyChecker implements Filter.Checker {

    private final boolean meet;

    public DummyChecker(boolean meet) {
        this.meet = meet;
    }

    @Override
    public boolean isMeet(PartnerPoint partnerPoint, Cursor cursor) {
        return meet;
    }
}
