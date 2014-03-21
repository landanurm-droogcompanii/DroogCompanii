package ru.droogcompanii.application.ui.activity.main_screen_2.map;

import android.database.Cursor;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.data.db_util.CursorHandler;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerHierarchyContracts;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersHierarchyReaderFromDatabase;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPointImpl;
import ru.droogcompanii.application.util.OnEachHandler;
import ru.droogcompanii.application.util.OnEachIterable;

/**
 * Created by ls on 21.03.14.
 */
class IteratorOverPartnerPointsInDb implements OnEachIterable<PartnerPoint> {

    private static final PartnerHierarchyContracts.PartnerPointsContract
            PARTNER_POINTS = new PartnerHierarchyContracts.PartnerPointsContract();

    private final PartnersHierarchyReaderFromDatabase READER =
            new PartnersHierarchyReaderFromDatabase(DroogCompaniiApplication.getContext());

    private final String where;

    public IteratorOverPartnerPointsInDb(String where) {
        this.where = where;
    }

    @Override
    public void forEach(final OnEachHandler<PartnerPoint> onEachHandler) {
        READER.handleCursorByQuery(prepareSql(), new CursorHandler() {
            @Override
            public void handle(Cursor cursor) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    onEachHandler.onEach(readPartnerPoint(cursor));
                    cursor.moveToNext();
                }
            }
        });
    }

    private String prepareSql() {
        return "SELECT " +
                PARTNER_POINTS.COLUMN_NAME_ID + ", " +
                PARTNER_POINTS.COLUMN_NAME_LATITUDE + ", " +
                PARTNER_POINTS.COLUMN_NAME_LONGITUDE +
                " FROM " + PARTNER_POINTS.TABLE_NAME + " " +
                where + ";";
    }

    private static PartnerPoint readPartnerPoint(Cursor cursor) {
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
