package ru.droogcompanii.application.data.db_util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.io.Serializable;
import java.util.List;

import ru.droogcompanii.application.util.SerializationUtils;
import ru.droogcompanii.application.data.DroogCompaniiXmlParser;
import ru.droogcompanii.application.data.data_structure.Partner;
import ru.droogcompanii.application.data.data_structure.PartnerCategory;
import ru.droogcompanii.application.data.data_structure.PartnerPoint;
import ru.droogcompanii.application.data.db_util.DroogCompaniiContracts.PartnerCategoriesContract;
import ru.droogcompanii.application.data.db_util.DroogCompaniiContracts.PartnerPointsContract;
import ru.droogcompanii.application.data.db_util.DroogCompaniiContracts.PartnersContract;

/**
 * Created by Leonid on 09.12.13.
 */
public class WriterToDatabase {
    private final Context context;
    private SQLiteDatabase db;

    public WriterToDatabase(Context context) {
        this.context = context;
    }

    public void write(DroogCompaniiXmlParser.ParsedData parsedData) {
        DroogCompaniiDbHelper dbHelper = new DroogCompaniiDbHelper(context);
        db = dbHelper.getWritableDatabase();
        clearOldData();
        writePartnerCategories(parsedData.partnerCategories);
        writePartners(parsedData.partners);
        writePartnerPoints(parsedData.partnerPoints);
        db.close();
        db = null;
        dbHelper.close();
    }

    private void clearOldData() {
        db.delete(PartnerCategoriesContract.TABLE_NAME, null, null);
        db.delete(PartnersContract.TABLE_NAME, null, null);
        db.delete(PartnerPointsContract.TABLE_NAME, null, null);
    }

    private void writePartnerCategories(List<PartnerCategory> partnerCategories) {
        for (PartnerCategory each : partnerCategories) {
            writePartnerCategory(each);
        }
    }

    private void writePartnerCategory(PartnerCategory partnerCategory) {
        String sql = "INSERT INTO " + PartnerCategoriesContract.TABLE_NAME + " (" +
                         PartnerCategoriesContract.COLUMN_NAME_ID + ", " +
                         PartnerCategoriesContract.COLUMN_NAME_TITLE +
                     ") VALUES(?,?)";
        SQLiteStatement insertStatement = db.compileStatement(sql);
        insertStatement.clearBindings();
        insertStatement.bindLong(1, partnerCategory.id);
        insertStatement.bindString(2, partnerCategory.title);
        insertStatement.executeInsert();
    }

    private void writePartners(List<Partner> partners) {
        for (Partner each : partners) {
            writePartner(each);
        }
    }

    private void writePartner(Partner partner) {
        String sql = "INSERT INTO " + PartnersContract.TABLE_NAME + " (" +
                         PartnersContract.COLUMN_NAME_ID + ", " +
                         PartnersContract.COLUMN_NAME_TITLE + ", " +
                         PartnersContract.COLUMN_NAME_FULL_TITLE + ", " +
                         PartnersContract.COLUMN_NAME_SALE_TYPE + ", " +
                         PartnersContract.COLUMN_NAME_CATEGORY_ID +
                     ") VALUES(?,?,?,?,?)";
        SQLiteStatement insertStatement = db.compileStatement(sql);
        insertStatement.clearBindings();
        insertStatement.bindLong(1, partner.id);
        insertStatement.bindString(2, partner.title);
        insertStatement.bindString(3, partner.fullTitle);
        insertStatement.bindString(4, partner.saleType);
        insertStatement.bindLong(5, partner.categoryId);
        insertStatement.executeInsert();
    }

    private void writePartnerPoints(List<PartnerPoint> partnerPoints) {
        for (PartnerPoint each : partnerPoints) {
            writePartnerPoint(each);
        }
    }

    private void writePartnerPoint(PartnerPoint partnerPoint) {
        String sql = "INSERT INTO " + PartnerPointsContract.TABLE_NAME + " (" +
                         PartnerPointsContract.COLUMN_NAME_TITLE + ", " +
                         PartnerPointsContract.COLUMN_NAME_ADDRESS + ", " +
                         PartnerPointsContract.COLUMN_NAME_LONGITUDE + ", " +
                         PartnerPointsContract.COLUMN_NAME_LATITUDE + ", " +
                         PartnerPointsContract.COLUMN_NAME_PAYMENT_METHODS + ", " +
                         PartnerPointsContract.COLUMN_NAME_PHONES + ", " +
                         PartnerPointsContract.COLUMN_NAME_WORKING_HOURS + ", " +
                         PartnerPointsContract.COLUMN_NAME_PARTNER_ID +
                     ") VALUES(?,?,?,?,?,?,?,?)";
        SQLiteStatement insertStatement = db.compileStatement(sql);
        insertStatement.clearBindings();
        insertStatement.bindString(1, partnerPoint.title);
        insertStatement.bindString(2, partnerPoint.address);
        insertStatement.bindDouble(3, partnerPoint.longitude);
        insertStatement.bindDouble(4, partnerPoint.latitude);
        insertStatement.bindString(5, partnerPoint.paymentMethods);
        insertStatement.bindBlob(6, SerializationUtils.serialize((Serializable) partnerPoint.phones));
        insertStatement.bindBlob(7, SerializationUtils.serialize(partnerPoint.workingHours));
        insertStatement.bindLong(8, partnerPoint.partnerId);
        insertStatement.executeInsert();
    }
}
