package ru.droogcompanii.application.data.db_util.hierarchy_of_partners;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.io.Serializable;
import java.util.Collection;

import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerHierarchyContracts.PartnerCategoriesContract;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerHierarchyContracts.PartnerPointsContract;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnerHierarchyContracts.PartnersContract;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.xml_parser.partners_xml_parser.PartnersXmlParser;
import ru.droogcompanii.application.util.SerializationUtils;

/**
 * Created by Leonid on 09.12.13.
 */
public class PartnerHierarchyWriterToDatabase {
    private final Context context;
    private SQLiteDatabase db;

    public PartnerHierarchyWriterToDatabase(Context context) {
        this.context = context;
    }

    public void write(PartnersXmlParser.ParsedData parsedData) {
        PartnerHierarchyDbHelper dbHelper = new PartnerHierarchyDbHelper(context);
        db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            tryExecuteTransaction(parsedData);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();
        db = null;
        dbHelper.close();
    }

    private void tryExecuteTransaction(PartnersXmlParser.ParsedData parsedData) {
        clearOldData();
        writePartnerCategories(parsedData.partnerCategories);
        writePartners(parsedData.partners);
        writePartnerPoints(parsedData.partnerPoints);
    }

    private void clearOldData() {
        db.delete(PartnerCategoriesContract.TABLE_NAME, null, null);
        db.delete(PartnersContract.TABLE_NAME, null, null);
        db.delete(PartnerPointsContract.TABLE_NAME, null, null);
    }

    private void writePartnerCategories(Collection<PartnerCategory> partnerCategories) {
        for (PartnerCategory each : partnerCategories) {
            writePartnerCategory(each);
        }
    }

    private void writePartnerCategory(PartnerCategory partnerCategory) {
        String sql = "INSERT INTO " + PartnerCategoriesContract.TABLE_NAME + " (" +
                         PartnerCategoriesContract.COLUMN_ID + ", " +
                         PartnerCategoriesContract.COLUMN_TITLE +
                     ") VALUES(?,?)";
        SQLiteStatement insertStatement = db.compileStatement(sql);
        insertStatement.clearBindings();
        insertStatement.bindLong(1, partnerCategory.getId());
        insertStatement.bindString(2, partnerCategory.getTitle());
        insertStatement.executeInsert();
    }

    private void writePartners(Collection<Partner> partners) {
        for (Partner each : partners) {
            writePartner(each);
        }
    }

    private void writePartner(Partner partner) {
        String sql = "INSERT INTO " + PartnersContract.TABLE_NAME + " (" +
                         PartnersContract.COLUMN_ID + ", " +
                         PartnersContract.COLUMN_TITLE + ", " +
                         PartnersContract.COLUMN_FULL_TITLE + ", " +
                         PartnersContract.COLUMN_DISCOUNT_TYPE + ", " +
                         PartnersContract.COLUMN_DISCOUNT_SIZE + ", " +
                         PartnersContract.COLUMN_CATEGORY_ID + ", " +
                         PartnersContract.COLUMN_IMAGE_URL + ", " +
                         PartnersContract.COLUMN_DESCRIPTION + ", " +
                         PartnersContract.COLUMN_WEB_SITES + ", " +
                         PartnersContract.COLUMN_EMAILS +
                     ") VALUES(?,?,?,?,?,?,?,?,?,?)";
        SQLiteStatement insertStatement = db.compileStatement(sql);
        insertStatement.clearBindings();
        insertStatement.bindLong(1, partner.getId());
        insertStatement.bindString(2, partner.getTitle());
        insertStatement.bindString(3, partner.getFullTitle());
        insertStatement.bindString(4, partner.getDiscountType());
        insertStatement.bindLong(5, partner.getDiscountSize());
        insertStatement.bindLong(6, partner.getCategoryId());
        insertStatement.bindString(7, partner.getImageUrl());
        insertStatement.bindString(8, partner.getDescription());
        insertStatement.bindBlob(9, SerializationUtils.serialize((Serializable) partner.getWebSites()));
        insertStatement.bindBlob(10, SerializationUtils.serialize((Serializable) partner.getEmails()));
        insertStatement.executeInsert();
    }

    private void writePartnerPoints(Collection<PartnerPoint> partnerPoints) {
        for (PartnerPoint each : partnerPoints) {
            writePartnerPoint(each);
        }
    }

    private void writePartnerPoint(PartnerPoint partnerPoint) {
        String sql = "INSERT INTO " + PartnerPointsContract.TABLE_NAME + " (" +
                         PartnerPointsContract.COLUMN_ID + ", " +
                         PartnerPointsContract.COLUMN_TITLE + ", " +
                         PartnerPointsContract.COLUMN_ADDRESS + ", " +
                         PartnerPointsContract.COLUMN_LONGITUDE + ", " +
                         PartnerPointsContract.COLUMN_LATITUDE + ", " +
                         PartnerPointsContract.COLUMN_PAYMENT_METHODS + ", " +
                         PartnerPointsContract.COLUMN_PHONES + ", " +
                         PartnerPointsContract.COLUMN_WORKING_HOURS + ", " +
                         PartnerPointsContract.COLUMN_PARTNER_ID +
                     ") VALUES(?,?,?,?,?,?,?,?,?)";
        SQLiteStatement insertStatement = db.compileStatement(sql);
        insertStatement.clearBindings();
        insertStatement.bindLong(1, partnerPoint.getId());
        insertStatement.bindString(2, partnerPoint.getTitle());
        insertStatement.bindString(3, partnerPoint.getAddress());
        insertStatement.bindDouble(4, partnerPoint.getLongitude());
        insertStatement.bindDouble(5, partnerPoint.getLatitude());
        insertStatement.bindString(6, partnerPoint.getPaymentMethods());
        insertStatement.bindBlob(7, SerializationUtils.serialize((Serializable) partnerPoint.getPhones()));
        insertStatement.bindBlob(8, SerializationUtils.serialize(partnerPoint.getWorkingHours()));
        insertStatement.bindLong(9, partnerPoint.getPartnerId());
        insertStatement.executeInsert();
    }
}
