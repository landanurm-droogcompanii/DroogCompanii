package ru.droogcompanii.application.data.db_util.readers_from_database;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.util.SerializationUtils;
import ru.droogcompanii.application.data.data_structure.Partner;
import ru.droogcompanii.application.data.data_structure.PartnerPoint;
import ru.droogcompanii.application.data.data_structure.working_hours.WeekWorkingHours;
import ru.droogcompanii.application.data.db_util.DroogCompaniiContracts.PartnerPointsContract;

/**
 * Created by Leonid on 17.12.13.
 */
public class PartnerPointsReader extends BaseReaderFromDatabase {

    public PartnerPointsReader(Context context) {
        super(context);
    }

    public List<PartnerPoint> getPartnerPointsOf(Partner partner) {
        initDatabase();
        List<PartnerPoint> partnerPoints = getPartnerPointsFromDatabase(partner.id);
        releaseDatabase();
        return partnerPoints;
    }

    private List<PartnerPoint> getPartnerPointsFromDatabase(int partnerId) {
        String sql = "SELECT * FROM " + PartnerPointsContract.TABLE_NAME +
                     " WHERE " + PartnerPointsContract.COLUMN_NAME_PARTNER_ID + " = ? ;";
        String[] selectionArgs = { String.valueOf(partnerId) };
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        List<PartnerPoint> partnerPoints = getPartnerPointsFromCursor(cursor);
        cursor.close();
        return partnerPoints;
    }

    private List<PartnerPoint> getPartnerPointsFromCursor(Cursor cursor) {
        List<PartnerPoint> partnerPoints = new ArrayList<PartnerPoint>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            partnerPoints.add(getPartnerPointFromCursor(cursor));
            cursor.moveToNext();
        }
        return partnerPoints;
    }

    private PartnerPoint getPartnerPointFromCursor(Cursor cursor) {
        int titleColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_TITLE);
        int addressColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_ADDRESS);
        int longitudeColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_LONGITUDE);
        int latitudeColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_LATITUDE);
        int paymentMethodsColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_PAYMENT_METHODS);
        int phonesColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_PHONES);
        int workingHoursColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_WORKING_HOURS);
        int partnerIdColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_PARTNER_ID);

        String title = cursor.getString(titleColumnIndex);
        String address = cursor.getString(addressColumnIndex);
        double longitude = cursor.getDouble(longitudeColumnIndex);
        double latitude = cursor.getDouble(latitudeColumnIndex);
        String paymentMethods = cursor.getString(paymentMethodsColumnIndex);
        byte[] phonesBlob = cursor.getBlob(phonesColumnIndex);
        List<String> phones = (List<String>) SerializationUtils.deserialize(phonesBlob);
        byte[] weekWorkingHoursBlob = cursor.getBlob(workingHoursColumnIndex);
        WeekWorkingHours weekWorkingHours =
                (WeekWorkingHours) SerializationUtils.deserialize(weekWorkingHoursBlob);
        int partnerId = cursor.getInt(partnerIdColumnIndex);

        return new PartnerPoint(title, address, phones, weekWorkingHours,
                                paymentMethods, longitude, latitude, partnerId);
    }
}
