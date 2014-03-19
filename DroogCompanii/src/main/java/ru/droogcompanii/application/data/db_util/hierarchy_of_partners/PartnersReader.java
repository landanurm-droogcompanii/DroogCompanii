package ru.droogcompanii.application.data.db_util.hierarchy_of_partners;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.db_util.CursorHandler;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerImpl;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.util.SerializationUtils;

/**
 * Created by Leonid on 17.12.13.
 */
public class PartnersReader extends PartnersHierarchyReaderFromDatabase {

    private static final PartnerHierarchyContracts.PartnersContract COLUMNS = new PartnerHierarchyContracts.PartnersContract();

    private static class ColumnIndices {
        int idColumnIndex;
        int titleColumnIndex;
        int fullTitleColumnIndex;
        int discountTypeColumnIndex;
        int discountSizeColumnIndex;
        int categoryIdColumnIndex;
        int imageUrlColumnIndex;
        int descriptionColumnIndex;
        int webSitesColumnIndex;
        int emailsColumnIndex;
        int isFavoriteColumnIndex;
    }

    private int idColumnIndex;
    private int titleColumnIndex;
    private int fullTitleColumnIndex;
    private int discountTypeColumnIndex;
    private int discountSizeColumnIndex;
    private int categoryIdColumnIndex;
    private int imageUrlColumnIndex;
    private int descriptionColumnIndex;
    private int webSitesColumnIndex;
    private int emailsColumnIndex;
    private int isFavoriteColumnIndex;


    public PartnersReader(Context context) {
        super(context);
    }

    public void handleCursorByCondition(String where, CursorHandler cursorHandler) {
        String correctedWhere = ((where == null || where.isEmpty()) ? "" : where);
        String sql = "SELECT * FROM " + PartnerHierarchyContracts.PartnersContract.TABLE_NAME + " " + correctedWhere + " ;";
        handleCursorByQuery(sql, cursorHandler);
    }

    public List<Partner> getPartnersOf(PartnerCategory category) {
        String where = " WHERE " +
                PartnerHierarchyContracts.PartnersContract.COLUMN_NAME_CATEGORY_ID + " = " + category.getId();
        return getPartnersByCondition(where);
    }

    List<Partner> getPartnersByCondition(String where) {
        String sql = "SELECT * FROM " + PartnerHierarchyContracts.PartnersContract.TABLE_NAME + where + " ;";

        initDatabase();

        Cursor cursor = db.rawQuery(sql, null);
        List<Partner> partners = getPartnersFromCursor(cursor);
        cursor.close();

        closeDatabase();

        return partners;
    }

    public static List<Partner> getPartnersFromCursor(Cursor cursor) {
        List<Partner> partners = new ArrayList<Partner>();
        ColumnIndices columnIndices = calculateColumnIndices(cursor);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            partners.add(getPartnerFromCursor(cursor, columnIndices));
            cursor.moveToNext();
        }
        return partners;
    }

    private static ColumnIndices calculateColumnIndices(Cursor cursor) {
        ColumnIndices columnIndices = new ColumnIndices();
        columnIndices.idColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_NAME_ID);
        columnIndices.titleColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_NAME_TITLE);
        columnIndices.fullTitleColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_NAME_FULL_TITLE);
        columnIndices.discountTypeColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_NAME_DISCOUNT_TYPE);
        columnIndices.discountSizeColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_NAME_DISCOUNT_SIZE);
        columnIndices.categoryIdColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_NAME_CATEGORY_ID);
        columnIndices.imageUrlColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_NAME_IMAGE_URL);
        columnIndices.descriptionColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_NAME_DESCRIPTION);
        columnIndices.webSitesColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_NAME_WEB_SITES);
        columnIndices.emailsColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_NAME_EMAILS);
        columnIndices.isFavoriteColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_NAME_IS_FAVORITE);
        return columnIndices;
    }

    @SuppressWarnings("unchecked")
    private static Partner getPartnerFromCursor(Cursor cursor, ColumnIndices columnIndices) {
        PartnerImpl partner = new PartnerImpl();
        partner.id = cursor.getInt(columnIndices.idColumnIndex);
        partner.title = cursor.getString(columnIndices.titleColumnIndex);
        partner.fullTitle = cursor.getString(columnIndices.fullTitleColumnIndex);
        partner.discountType = cursor.getString(columnIndices.discountTypeColumnIndex);
        partner.discountSize = cursor.getInt(columnIndices.discountSizeColumnIndex);
        partner.categoryId = cursor.getInt(columnIndices.categoryIdColumnIndex);
        partner.imageUrl = cursor.getString(columnIndices.imageUrlColumnIndex);
        partner.description = cursor.getString(columnIndices.descriptionColumnIndex);
        byte[] webSitesBlob = cursor.getBlob(columnIndices.webSitesColumnIndex);
        partner.webSites = (List<String>) SerializationUtils.deserialize(webSitesBlob);
        byte[] emailsBlob = cursor.getBlob(columnIndices.emailsColumnIndex);
        partner.emails = (List<String>) SerializationUtils.deserialize(emailsBlob);
        return partner;
    }

    public Partner getPartnerOf(PartnerPoint partnerPoint) {
        return getPartnerById(partnerPoint.getPartnerId());
    }

    public Partner getPartnerOf(Offer offer) {
        return getPartnerById(offer.getPartnerId());
    }

    public Partner getPartnerById(int partnerId) {
        String where = " WHERE " +
                PartnerHierarchyContracts.PartnersContract.COLUMN_NAME_ID + " = " + partnerId;
        List<Partner> partners = getPartnersByCondition(where);
        if (partners.isEmpty()) {
            throw new IllegalArgumentException("There is no partners with id: " + partnerId);
        }
        return partners.get(0);
    }

    public List<Partner> getAllPartners() {
        return getPartnersByCondition("");
    }
}
