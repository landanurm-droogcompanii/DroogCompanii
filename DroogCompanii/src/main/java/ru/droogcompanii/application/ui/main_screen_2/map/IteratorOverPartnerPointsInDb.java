package ru.droogcompanii.application.ui.main_screen_2.map;

import android.database.Cursor;

import java.util.HashSet;
import java.util.Set;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.data.db_util.CursorHandler;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerHierarchyContracts;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersHierarchyReaderFromDatabase;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPointImpl;
import ru.droogcompanii.application.ui.main_screen_2.filters_dialog.filters.Filter;
import ru.droogcompanii.application.ui.main_screen_2.filters_dialog.filters.Filters;
import ru.droogcompanii.application.util.OnEachHandler;
import ru.droogcompanii.application.util.StringsCombiner;

/**
 * Created by ls on 21.03.14.
 */
class IteratorOverPartnerPointsInDb {

    private static final PartnerHierarchyContracts.PartnerPointsContract
            PARTNER_POINTS = new PartnerHierarchyContracts.PartnerPointsContract();

    private final PartnersHierarchyReaderFromDatabase READER =
            new PartnersHierarchyReaderFromDatabase(DroogCompaniiApplication.getContext());

    private final String where;
    private final Filters filters;
    private final Filter filter;

    public IteratorOverPartnerPointsInDb(Filters currentFilters, String where) {
        this.where = where;
        this.filters = currentFilters;
        this.filter = currentFilters.getActiveFilter();
    }

    public void forEachUsingFilters(final OnEachHandler<PartnerPoint> onEachHandler) {
        READER.handleCursorByQuery(prepareSql(), new CursorHandler() {
            @Override
            public void handle(Cursor cursor) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    PartnerPoint partnerPoint = readPartnerPoint(cursor);
                    if (filter.isPassedThroughFilter(partnerPoint, cursor)) {
                        onEachHandler.onEach(partnerPoint);
                    }
                    cursor.moveToNext();
                }
            }
        });
    }

    public void forEach(final OnEachHandler<PartnerPoint> onEachHandler) {
        READER.handleCursorByQuery(prepareSql(), new CursorHandler() {
            @Override
            public void handle(Cursor cursor) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    PartnerPoint partnerPoint = readPartnerPoint(cursor);
                    onEachHandler.onEach(partnerPoint);
                    cursor.moveToNext();
                }
            }
        });
    }

    private String prepareSql() {
        return "SELECT " +
                prepareColumnList() +
                " FROM " + PARTNER_POINTS.TABLE_NAME + " " +
                where + ";";
    }

    private String prepareColumnList() {
        Set<String> columns = new HashSet<String>();
        columns.add(PARTNER_POINTS.COLUMN_NAME_ID);
        columns.add(PARTNER_POINTS.COLUMN_NAME_LATITUDE);
        columns.add(PARTNER_POINTS.COLUMN_NAME_LONGITUDE);
        columns.addAll(filters.getColumnsOfPartnerPoint());
        return StringsCombiner.combine(columns, ", ");
    }

    private PartnerPoint readPartnerPoint(Cursor cursor) {
        int idColumnIndex = cursor.getColumnIndexOrThrow(PARTNER_POINTS.COLUMN_NAME_ID);
        int latitudeColumnIndex = cursor.getColumnIndexOrThrow(PARTNER_POINTS.COLUMN_NAME_LATITUDE);
        int longitudeColumnIndex = cursor.getColumnIndexOrThrow(PARTNER_POINTS.COLUMN_NAME_LONGITUDE);
        PartnerPointImpl partnerPoint = new PartnerPointImpl();
        partnerPoint.id = cursor.getInt(idColumnIndex);
        partnerPoint.latitude = cursor.getDouble(latitudeColumnIndex);
        partnerPoint.longitude = cursor.getDouble(longitudeColumnIndex);
        return partnerPoint;
    }
}
