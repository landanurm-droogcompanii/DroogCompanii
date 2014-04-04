package ru.droogcompanii.application.data.db_util.hierarchy_of_partners;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.Collection;

import ru.droogcompanii.application.data.db_util.contracts.EmailsContract;
import ru.droogcompanii.application.data.db_util.contracts.PartnerCategoriesContract;
import ru.droogcompanii.application.data.db_util.contracts.PartnerPointsContract;
import ru.droogcompanii.application.data.db_util.contracts.PartnersContract;
import ru.droogcompanii.application.data.db_util.contracts.PhonesContract;
import ru.droogcompanii.application.data.db_util.contracts.WebSitesContract;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.xml_parser.partners_xml_parser.PartnersXmlParser;

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
            db.close();
            db = null;
            dbHelper.close();
        }
    }

    private void tryExecuteTransaction(PartnersXmlParser.ParsedData parsedData) {
        clearOldData();
        writePartnerCategories(parsedData.partnerCategories);
        writePartners(parsedData.partners);
        writePartnerPoints(parsedData.partnerPoints);
    }

    private void clearOldData() {
        for (String tableName : PartnerHierarchyDbHelper.TABLE_NAMES) {
            db.delete(tableName, null, null);
        }
    }

    private void writePartnerCategories(Collection<PartnerCategory> partnerCategories) {
        for (PartnerCategory each : partnerCategories) {
            writePartnerCategory(each);
        }
    }

    private void writePartnerCategory(PartnerCategory partnerCategory) {
        String sql = "INSERT INTO " + PartnerCategoriesContract.TABLE_NAME + " (" +
                         PartnerCategoriesContract.COLUMN_ID + ", " +
                         PartnerCategoriesContract.COLUMN_TITLE + ", " +
                         PartnerCategoriesContract.COLUMN_TEXT_SEARCH_IN +
                     ") VALUES(?,?,?)";
        SQLiteStatement insertStatement = db.compileStatement(sql);
        insertStatement.clearBindings();
        insertStatement.bindLong(1, partnerCategory.getId());
        insertStatement.bindString(2, partnerCategory.getTitle());
        insertStatement.bindString(3, TextSearchInMaker.make(partnerCategory));
        insertStatement.executeInsert();
    }

    private void writePartners(Collection<Partner> partners) {
        for (Partner each : partners) {
            writePartner(each);
        }
    }

    private void writePartner(Partner partner) {
        insertMainData(partner);
        insertOtherTablesData(partner);
    }

    private void insertMainData(Partner partner) {
        String sql = "INSERT INTO " + PartnersContract.TABLE_NAME + " (" +
                PartnersContract.COLUMN_ID + ", " +
                PartnersContract.COLUMN_TITLE + ", " +
                PartnersContract.COLUMN_FULL_TITLE + ", " +
                PartnersContract.COLUMN_DISCOUNT_TYPE + ", " +
                PartnersContract.COLUMN_DISCOUNT_SIZE + ", " +
                PartnersContract.COLUMN_CATEGORY_ID + ", " +
                PartnersContract.COLUMN_IMAGE_URL + ", " +
                PartnersContract.COLUMN_DESCRIPTION + ", " +
                PartnersContract.COLUMN_TEXT_SEARCH_IN +
                ") VALUES(?,?,?,?,?,?,?,?,?)";
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
        insertStatement.bindString(9, TextSearchInMaker.make(partner));
        insertStatement.executeInsert();
    }

    private void insertOtherTablesData(Partner partner) {
        int ownerId = partner.getId();
        for (String webSite : partner.getWebSites()) {
            insertWebSite(ownerId, webSite);
        }
        for (String email : partner.getEmails()) {
            insertEmail(ownerId, email);
        }
    }

    private void insertWebSite(int ownerId, String url) {
        String sql = "INSERT INTO " + WebSitesContract.TABLE_NAME + " (" +
                    WebSitesContract.COLUMN_OWNER_ID + ", " +
                    WebSitesContract.COLUMN_URL +
                ") VALUES(?,?)";
        SQLiteStatement insertStatement = db.compileStatement(sql);
        insertStatement.clearBindings();
        insertStatement.bindLong(1, ownerId);
        insertStatement.bindString(2, url);
        insertStatement.executeInsert();
    }

    private void insertEmail(int ownerId, String email) {
        String sql = "INSERT INTO " + EmailsContract.TABLE_NAME + " (" +
                EmailsContract.COLUMN_OWNER_ID + ", " +
                EmailsContract.COLUMN_ADDRESS +
                ") VALUES(?,?)";
        SQLiteStatement insertStatement = db.compileStatement(sql);
        insertStatement.clearBindings();
        insertStatement.bindLong(1, ownerId);
        insertStatement.bindString(2, email);
        insertStatement.executeInsert();
    }

    private void writePartnerPoints(Collection<PartnerPoint> partnerPoints) {
        for (PartnerPoint each : partnerPoints) {
            writePartnerPoint(each);
        }
    }

    private void writePartnerPoint(PartnerPoint partnerPoint) {
        insertMainData(partnerPoint);
        insertOtherTablesData(partnerPoint);
    }

    private void insertMainData(PartnerPoint partnerPoint) {
        String sql = "INSERT INTO " + PartnerPointsContract.TABLE_NAME + " (" +
                PartnerPointsContract.COLUMN_ID + ", " +
                PartnerPointsContract.COLUMN_TITLE + ", " +
                PartnerPointsContract.COLUMN_ADDRESS + ", " +
                PartnerPointsContract.COLUMN_LONGITUDE + ", " +
                PartnerPointsContract.COLUMN_LATITUDE + ", " +
                PartnerPointsContract.COLUMN_PAYMENT_METHODS + ", " +
                PartnerPointsContract.COLUMN_PARTNER_ID + ", " +
                PartnerPointsContract.COLUMN_TEXT_SEARCH_IN +
                ") VALUES(?,?,?,?,?,?,?,?)";
        SQLiteStatement insertStatement = db.compileStatement(sql);
        insertStatement.clearBindings();
        insertStatement.bindLong(1, partnerPoint.getId());
        insertStatement.bindString(2, partnerPoint.getTitle());
        insertStatement.bindString(3, partnerPoint.getAddress());
        insertStatement.bindDouble(4, partnerPoint.getLongitude());
        insertStatement.bindDouble(5, partnerPoint.getLatitude());
        insertStatement.bindString(6, partnerPoint.getPaymentMethods());
        insertStatement.bindLong(7, partnerPoint.getPartnerId());
        insertStatement.bindString(8, TextSearchInMaker.make(partnerPoint));
        insertStatement.executeInsert();
    }

    private void insertOtherTablesData(PartnerPoint partnerPoint) {
        for (String phone : partnerPoint.getPhones()) {
            insertPhone(partnerPoint.getId(), phone);
        }
        WorkingHoursDbUtils.write(db, partnerPoint.getId(), partnerPoint.getWorkingHours());
    }

    private void insertPhone(int ownerId, String phone) {
        String sql = "INSERT INTO " + PhonesContract.TABLE_NAME + " (" +
                PhonesContract.COLUMN_OWNER_ID + ", " +
                PhonesContract.COLUMN_NUMBER +
                ") VALUES(?,?)";
        SQLiteStatement insertStatement = db.compileStatement(sql);
        insertStatement.clearBindings();
        insertStatement.bindLong(1, ownerId);
        insertStatement.bindString(2, phone);
        insertStatement.executeInsert();
    }
}
