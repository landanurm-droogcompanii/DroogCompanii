package ru.droogcompanii.application.ui.main_screen_2.filters.filters_impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.View;

import java.util.Set;

import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerHierarchyContracts;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by ls on 25.03.14.
 */
public interface Filter {

    public static final PartnerHierarchyContracts.PartnerPointsContract
            PARTNER_POINTS = new PartnerHierarchyContracts.PartnerPointsContract();


    public static interface Checker {
        boolean isMeet(PartnerPoint partnerPoint, Cursor cursor);
    }

    boolean isActivated();
    Checker getChecker();
    Set<String> getColumnsOfPartnerPoint();
    void readFrom(SharedPreferences sharedPreferences);
    void writeTo(SharedPreferences.Editor editor);
    View inflateContentView(Context context);
    void displayOn(View view);
    void readFrom(View view);
}
