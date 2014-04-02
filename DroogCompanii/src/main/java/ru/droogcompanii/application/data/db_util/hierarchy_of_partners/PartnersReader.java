package ru.droogcompanii.application.data.db_util.hierarchy_of_partners;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.db_util.CursorHandler;
import ru.droogcompanii.application.data.db_util.Where;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerImpl;
import ru.droogcompanii.application.util.Holder;
import ru.droogcompanii.application.util.SerializationUtils;

/**
 * Created by Leonid on 17.12.13.
 */
public class PartnersReader extends PartnersHierarchyReaderFromDatabase {

    private static final PartnerHierarchyContracts.PartnersContract
            COLUMNS = new PartnerHierarchyContracts.PartnersContract();

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


    public PartnersReader(Context context) {
        super(context);
    }

    public List<Partner> getPartnersByCondition(String conditionToReceivePartners) {
        List<Partner> noPartners = new ArrayList<Partner>();
        final Holder<List<Partner>> resultHolder = Holder.from(noPartners);
        handleCursorByCondition(conditionToReceivePartners, new CursorHandler() {
            @Override
            public void handle(Cursor cursor) {
                resultHolder.value = getPartnersFromCursor(cursor);
            }
        });
        return resultHolder.value;
    }

    public void handleCursorByCondition(String condition, CursorHandler cursorHandler) {
        String where = Where.byCondition(condition);
        String sql = "SELECT * FROM " + PartnerHierarchyContracts.PartnersContract.TABLE_NAME + " " + where + " ;";
        handleCursorByQuery(sql, cursorHandler);
    }

    public List<Partner> getPartnersOf(PartnerCategory category) {
        String where = " WHERE " +
                PartnerHierarchyContracts.PartnersContract.COLUMN_CATEGORY_ID + " = " + category.getId();
        return getPartnersByWhere(where);
    }

    List<Partner> getPartnersByWhere(String where) {
        String sql = "SELECT * FROM " +
                PartnerHierarchyContracts.PartnersContract.TABLE_NAME + " " + where + " ;";

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
        columnIndices.idColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_ID);
        columnIndices.titleColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_TITLE);
        columnIndices.fullTitleColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_FULL_TITLE);
        columnIndices.discountTypeColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_DISCOUNT_TYPE);
        columnIndices.discountSizeColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_DISCOUNT_SIZE);
        columnIndices.categoryIdColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_CATEGORY_ID);
        columnIndices.imageUrlColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_IMAGE_URL);
        columnIndices.descriptionColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_DESCRIPTION);
        columnIndices.webSitesColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_WEB_SITES);
        columnIndices.emailsColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_EMAILS);
        columnIndices.isFavoriteColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_IS_FAVORITE);
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

    public Partner getPartnerByPointId(int partnerPointId) {
        final PartnerHierarchyContracts.PartnersContract
                PARTNERS = new PartnerHierarchyContracts.PartnersContract();
        final PartnerHierarchyContracts.PartnerPointsContract
                PARTNER_POINTS = new PartnerHierarchyContracts.PartnerPointsContract();
        List<Partner> partners = getPartnersByWhere(
            "WHERE " + PARTNERS.TABLE_NAME + "." + PARTNERS.COLUMN_ID + " IN (" +
                "SELECT " + PARTNER_POINTS.TABLE_NAME + "." + PARTNER_POINTS.COLUMN_PARTNER_ID +
                " FROM " + PARTNER_POINTS.TABLE_NAME +
                " WHERE " + PARTNER_POINTS.TABLE_NAME + "." + PARTNER_POINTS.COLUMN_ID + "=" + partnerPointId +
            ")"
        );
        if (partners.size() != 1) {
            throw new IllegalStateException("Every PartnerPoint should be associated with only ONE Partner");
        }
        return partners.get(0);
    }

    public Partner getPartnerById(int partnerId) {
        String where = " WHERE " +
                PartnerHierarchyContracts.PartnersContract.COLUMN_ID + " = " + partnerId;
        List<Partner> partners = getPartnersByWhere(where);
        if (partners.isEmpty()) {
            throw new IllegalArgumentException("There is no partners with id: " + partnerId);
        }
        return partners.get(0);
    }
}
