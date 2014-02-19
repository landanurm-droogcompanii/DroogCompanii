package ru.droogcompanii.application.data.db_util.hierarchy_of_partners;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersContracts.PartnerPointsContract;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPointImpl;
import ru.droogcompanii.application.data.working_hours.WeekWorkingHours;
import ru.droogcompanii.application.util.SerializationUtils;

/**
 * Created by Leonid on 17.12.13.
 */
public class PartnerPointsReader extends BasePartnersReaderFromDatabase {

    private final Context context;

    private static class ColumnIndices {
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
        this.context = context;
    }

    public List<PartnerPoint> getPartnerPointsOf(PartnerCategory partnerCategory) {
        PartnersReader partnersReader = new PartnersReader(context);
        List<Partner> partners = partnersReader.getPartnersOf(partnerCategory);
        List<PartnerPoint> result = new ArrayList<PartnerPoint>();
        for (Partner partner : partners) {
            result.addAll(getPartnerPointsOf(partner));
        }
        return result;
    }

    public List<PartnerPoint> getPartnerPointsOf(Partner partner) {
        return getPartnerPointsFromDatabase(
                " WHERE " + PartnerPointsContract.COLUMN_NAME_PARTNER_ID + " = " + partner.getId()
        );
    }

    public List<PartnerPoint> getAllPartnerPoints() {
        return getPartnerPointsFromDatabase("");
    }

    private List<PartnerPoint> getPartnerPointsFromDatabase(String where) {
        initDatabase();

        String sql = "SELECT * FROM " + PartnerPointsContract.TABLE_NAME + where + " ;";
        Cursor cursor = db.rawQuery(sql, null);
        List<PartnerPoint> partnerPoints = getPartnerPointsFromCursor(cursor);
        cursor.close();

        closeDatabase();
        return partnerPoints;
    }

    public static List<PartnerPoint> getPartnerPointsFromCursor(Cursor cursor) {
        List<PartnerPoint> partnerPoints = new ArrayList<PartnerPoint>();
        ColumnIndices columnIndices = calculateColumnIndices(cursor);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            partnerPoints.add(getPartnerPointFromCursor(cursor, columnIndices));
            cursor.moveToNext();
        }
        return partnerPoints;
    }

    private static ColumnIndices calculateColumnIndices(Cursor cursor) {
        ColumnIndices columnIndices = new ColumnIndices();
        columnIndices.titleColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_TITLE);
        columnIndices.addressColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_ADDRESS);
        columnIndices.longitudeColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_LONGITUDE);
        columnIndices.latitudeColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_LATITUDE);
        columnIndices.paymentMethodsColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_PAYMENT_METHODS);
        columnIndices.phonesColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_PHONES);
        columnIndices.workingHoursColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_WORKING_HOURS);
        columnIndices.partnerIdColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_PARTNER_ID);
        return columnIndices;
    }

    private static PartnerPointImpl getPartnerPointFromCursor(Cursor cursor, ColumnIndices columnIndices) {
        PartnerPointImpl partnerPoint = new PartnerPointImpl();
        partnerPoint.title = cursor.getString(columnIndices.titleColumnIndex);
        partnerPoint.address = cursor.getString(columnIndices.addressColumnIndex);
        partnerPoint.longitude = cursor.getDouble(columnIndices.longitudeColumnIndex);
        partnerPoint.latitude = cursor.getDouble(columnIndices.latitudeColumnIndex);
        partnerPoint.paymentMethods = cursor.getString(columnIndices.paymentMethodsColumnIndex);
        byte[] phonesBlob = cursor.getBlob(columnIndices.phonesColumnIndex);
        partnerPoint.phones = (List<String>) SerializationUtils.deserialize(phonesBlob);
        byte[] weekWorkingHoursBlob = cursor.getBlob(columnIndices.workingHoursColumnIndex);
        partnerPoint.workingHours = (WeekWorkingHours) SerializationUtils.deserialize(weekWorkingHoursBlob);
        partnerPoint.partnerId = cursor.getInt(columnIndices.partnerIdColumnIndex);
        return partnerPoint;
    }
}
