package ru.droogcompanii.application.ui.screens.main.filters_dialog.filters;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
    public boolean isPassedThroughFilter(PartnerPoint partnerPoint, Cursor cursor, SQLiteDatabase db) {
        for (Filter each : filters) {
            if (!each.isPassedThroughFilter(partnerPoint, cursor, db)) {
                return false;
            }
        }
        return true;
    }
}