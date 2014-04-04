package ru.droogcompanii.application.ui.screens.main.filters_dialog.filters;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by ls on 25.03.14.
 */
class DummyFilter implements Filter {
    private final boolean passed;

    public DummyFilter(boolean passed) {
        this.passed = passed;
    }

    @Override
    public boolean isPassedThroughFilter(PartnerPoint partnerPoint, Cursor cursor, SQLiteDatabase db) {
        return passed;
    }
}
