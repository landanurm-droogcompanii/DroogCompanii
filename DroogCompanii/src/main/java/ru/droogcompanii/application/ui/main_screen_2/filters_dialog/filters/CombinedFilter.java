package ru.droogcompanii.application.ui.main_screen_2.filters_dialog.filters;

import android.database.Cursor;

import java.util.Collection;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by ls on 26.03.14.
 */
class CombinedFilter implements Filter {
    private final Collection<Filter> filters;

    public CombinedFilter(Collection<Filter> filters) {
        this.filters = filters;
    }

    @Override
    public boolean isPassedThroughFilter(PartnerPoint partnerPoint, Cursor cursor) {
        for (Filter each : filters) {
            if (!each.isPassedThroughFilter(partnerPoint, cursor)) {
                return false;
            }
        }
        return true;
    }
}