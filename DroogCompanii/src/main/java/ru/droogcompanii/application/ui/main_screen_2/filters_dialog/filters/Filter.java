package ru.droogcompanii.application.ui.main_screen_2.filters_dialog.filters;

import android.database.Cursor;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by ls on 26.03.14.
 */
public interface Filter {
    boolean isPassedThroughFilter(PartnerPoint partnerPoint, Cursor cursor);
}
