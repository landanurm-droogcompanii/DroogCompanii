package ru.droogcompanii.application.data.db_util.readers_from_database;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.util.SerializationUtils;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.working_hours.WeekWorkingHours;
import ru.droogcompanii.application.data.db_util.DroogCompaniiContracts.PartnerPointsContract;

/**
 * Created by Leonid on 17.12.13.
 */
public class PartnerPointsReader extends BaseReaderFromDatabase {

    private final Context context;

    private int titleColumnIndex;
    private int addressColumnIndex;
    private int longitudeColumnIndex;
    private int latitudeColumnIndex;
    private int paymentMethodsColumnIndex;
    private int phonesColumnIndex;
    private int workingHoursColumnIndex;
    private int partnerIdColumnIndex;


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
                " WHERE " + PartnerPointsContract.COLUMN_NAME_PARTNER_ID + " = " + partner.id
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

    private List<PartnerPoint> getPartnerPointsFromCursor(Cursor cursor) {
        List<PartnerPoint> partnerPoints = new ArrayList<PartnerPoint>();
        calculateColumnIndices(cursor);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            partnerPoints.add(getPartnerPointFromCursor(cursor));
            cursor.moveToNext();
        }
        return partnerPoints;
    }

    private void calculateColumnIndices(Cursor cursor) {
        titleColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_TITLE);
        addressColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_ADDRESS);
        longitudeColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_LONGITUDE);
        latitudeColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_LATITUDE);
        paymentMethodsColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_PAYMENT_METHODS);
        phonesColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_PHONES);
        workingHoursColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_WORKING_HOURS);
        partnerIdColumnIndex = cursor.getColumnIndexOrThrow(PartnerPointsContract.COLUMN_NAME_PARTNER_ID);
    }

    private PartnerPoint getPartnerPointFromCursor(Cursor cursor) {
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
