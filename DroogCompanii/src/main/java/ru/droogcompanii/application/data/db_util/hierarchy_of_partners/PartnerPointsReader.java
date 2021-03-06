package ru.droogcompanii.application.data.db_util.hierarchy_of_partners;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.db_util.contracts.PartnerPointsContract;
import ru.droogcompanii.application.data.db_util.contracts.PhonesContract;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPointImpl;
import ru.droogcompanii.application.util.OnEachHandler;

/**
 * Created by Leonid on 17.12.13.
 */
public class PartnerPointsReader extends PartnersHierarchyReaderFromDatabase {

    private static class ColumnIndices {
        int idColumnIndex;
        int titleColumnIndex;
        int addressColumnIndex;
        int longitudeColumnIndex;
        int latitudeColumnIndex;
        int paymentMethodsColumnIndex;
        int phonesColumnIndex;
        int workingHoursColumnIndex;
        int partnerIdColumnIndex;
    }


    public PartnerPointsReader(Context context) {
        super(context);
    }

    public List<PartnerPoint> getPartnerPointsOf(Partner partner) {
        return getPartnerPointsByPartnerId(partner.getId());
    }

    public List<PartnerPoint> getPartnerPointsByPartnerId(int partnerId) {
        return getPartnerPointsByWhere(
                " WHERE " + PartnerPointsContract.COLUMN_PARTNER_ID + " = " + partnerId
        );
    }

    private List<PartnerPoint> getPartnerPointsByWhere(String where) {
        initDatabase();

        String sql = "SELECT * FROM " + PartnerPointsContract.TABLE_NAME + " " + where + " ;";
        Cursor cursor = db.rawQuery(sql, null);
        List<PartnerPoint> partnerPoints = getPartnerPointsFromCursor(
                cursor, new OnEachHandler<PartnerPointImpl>() {
                    @Override
                    public void onEach(PartnerPointImpl partnerPoint) {
                        partnerPoint.workingHours = WorkingHoursDbUtils.read(db, partnerPoint.getId());
                    }
                }
        );
        cursor.close();

        closeDatabase();
        return partnerPoints;
    }

    public static List<PartnerPoint> getPartnerPointsFromCursor(Cursor cursor) {
        return getPartnerPointsFromCursor(cursor, new OnEachHandler<PartnerPointImpl>() {

            @Override
            public void onEach(PartnerPointImpl each) {
                // do nothing
            }
        });
    }

    public static List<PartnerPoint> getPartnerPointsFromCursor(Cursor cursor,
                                               OnEachHandler<PartnerPointImpl> onEachHandler) {
        List<PartnerPoint> partnerPoints = new ArrayList<PartnerPoint>();
        ColumnIndices columnIndices = calculateColumnIndices(cursor);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            PartnerPointImpl partnerPoint = getPartnerPointFromCursor(cursor, columnIndices);
            onEachHandler.onEach(partnerPoint);
            partnerPoints.add(partnerPoint);
            cursor.moveToNext();
        }
        return partnerPoints;
    }

    private static ColumnIndices calculateColumnIndices(Cursor cursor) {
        ColumnIndices columnIndices = new ColumnIndices();
        columnIndices.idColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_ID);
        columnIndices.titleColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_TITLE);
        columnIndices.addressColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_ADDRESS);
        columnIndices.longitudeColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_LONGITUDE);
        columnIndices.latitudeColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_LATITUDE);
        columnIndices.paymentMethodsColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_PAYMENT_METHODS);
        columnIndices.phonesColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_PHONES);
        columnIndices.workingHoursColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_WORKING_HOURS);
        columnIndices.partnerIdColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_PARTNER_ID);
        return columnIndices;
    }

    private static PartnerPointImpl getPartnerPointFromCursor(Cursor cursor, ColumnIndices columnIndices) {
        PartnerPointImpl partnerPoint = new PartnerPointImpl();
        partnerPoint.id = cursor.getInt(columnIndices.idColumnIndex);
        partnerPoint.title = cursor.getString(columnIndices.titleColumnIndex);
        partnerPoint.address = cursor.getString(columnIndices.addressColumnIndex);
        partnerPoint.longitude = cursor.getDouble(columnIndices.longitudeColumnIndex);
        partnerPoint.latitude = cursor.getDouble(columnIndices.latitudeColumnIndex);
        partnerPoint.paymentMethods = cursor.getString(columnIndices.paymentMethodsColumnIndex);
        partnerPoint.phones = getPhonesOfPartnerPoint(partnerPoint.getId());
        partnerPoint.partnerId = cursor.getInt(columnIndices.partnerIdColumnIndex);
        return partnerPoint;
    }

    private static List<String> getPhonesOfPartnerPoint(int partnerPointId) {
        return ListOfStringsReader.readByOwnerId(partnerPointId, new ListOfStringsReader.Arguments() {
            @Override
            public String getTableName() {
                return PhonesContract.TABLE_NAME;
            }

            @Override
            public String getOwnerIdColumnName() {
                return PhonesContract.COLUMN_OWNER_ID;
            }

            @Override
            public String getRequiredColumnName() {
                return PhonesContract.COLUMN_NUMBER;
            }
        });
    }

    public PartnerPoint getPartnerPointById(int idOfPartnerPoint) {
        List<PartnerPoint> singlePartnerPoint = getPartnerPointsByWhere(
                "WHERE " + PartnerPointsContract.COLUMN_ID + "=" + idOfPartnerPoint
        );
        if (singlePartnerPoint.isEmpty()) {
            throw new IllegalArgumentException("There is no partner point with id: " + idOfPartnerPoint);
        }
        if (singlePartnerPoint.size() > 1) {
            throw new IllegalStateException(
                    "There is several partner points with id: " + idOfPartnerPoint + ". " +
                    "But <id> must be unique."
            );
        }
        return singlePartnerPoint.get(0);
    }
}
