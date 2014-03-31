package ru.droogcompanii.application.ui.screens.main.filters_dialog.filters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import java.util.Collection;

import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerHierarchyContracts;

/**
 * Created by ls on 25.03.14.
 */
public interface FilterHelper {

    public static final PartnerHierarchyContracts.PartnerPointsContract
            PARTNER_POINTS = new PartnerHierarchyContracts.PartnerPointsContract();


    boolean isActive();
    Collection<String> getColumnsOfPartnerPoint();
    Filter getFilter();
    View inflateContentView(Context context);
    void displayOn(View view);
    void readFrom(View view);
    void readFrom(SharedPreferences sharedPreferences);
    void writeTo(SharedPreferences.Editor editor);
}
