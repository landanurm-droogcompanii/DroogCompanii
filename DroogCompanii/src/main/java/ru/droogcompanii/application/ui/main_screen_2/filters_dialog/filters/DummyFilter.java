package ru.droogcompanii.application.ui.main_screen_2.filters_dialog.filters;

import android.database.Cursor;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by ls on 25.03.14.
 */
class DummyFilter implements Filter {
    private final boolean meet;

    public DummyFilter(boolean meet) {
        this.meet = meet;
    }

    @Override
    public boolean isPassedThroughFilter(PartnerPoint partnerPoint, Cursor cursor) {
        return meet;
    }
}
