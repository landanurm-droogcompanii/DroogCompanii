package ru.droogcompanii.application.ui.screens.main.filters_dialog.filters;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by ls on 26.03.14.
 */
public interface Filter {
    boolean isPassedThroughFilter(PartnerPoint partnerPoint, Cursor cursor, SQLiteDatabase db);
}
