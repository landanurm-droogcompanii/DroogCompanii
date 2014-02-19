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
public class PartnersReader extends BasePartnersReaderFromDatabase {

    private static final PartnersContracts.PartnersContract COLUMNS = new PartnersContracts.PartnersContract();

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
        String sql = "SELECT * FROM " + PartnersContracts.PartnersContract.TABLE_NAME + " " + correctedWhere + " ;";
        handleCursorByQuery(sql, cursorHandler);
    }

    public List<Partner> getPartnersOf(PartnerCategory category) {
        String where = " WHERE " +
                PartnersContracts.PartnersContract.COLUMN_NAME_CATEGORY_ID + " = " + category.getId();
        return getPartnersByCondition(where);
    }

    List<Partner> getPartnersByCondition(String where) {
        String sql = "SELECT * FROM " + PartnersContracts.PartnersContract.TABLE_NAME + where + " ;";

        initDatabase();

        Cursor cursor = db.rawQuery(sql, null);
        List<Partner> partners = getPartnersFromCursor(cursor);
        cursor.close();

        closeDatabase();

        return partners;
    }

    private List<Partner> getPartnersFromCursor(Cursor cursor) {
        List<Partner> partners = new ArrayList<Partner>();
        calculateColumnIndices(cursor);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            partners.add(getPartnerFromCursor(cursor));
            cursor.moveToNext();
        }
        return partners;
    }

    private void calculateColumnIndices(Cursor cursor) {
        idColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_NAME_ID);
        titleColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_NAME_TITLE);
        fullTitleColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_NAME_FULL_TITLE);
        discountTypeColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_NAME_DISCOUNT_TYPE);
        discountSizeColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_NAME_DISCOUNT_SIZE);
        categoryIdColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_NAME_CATEGORY_ID);
        imageUrlColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_NAME_IMAGE_URL);
        descriptionColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_NAME_DESCRIPTION);
        webSitesColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_NAME_WEB_SITES);
        emailsColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_NAME_EMAILS);
        isFavoriteColumnIndex = cursor.getColumnIndexOrThrow(COLUMNS.COLUMN_NAME_IS_FAVORITE);
    }

    @SuppressWarnings("unchecked")
    private Partner getPartnerFromCursor(Cursor cursor) {
        PartnerImpl partner = new PartnerImpl();
        partner.id = cursor.getInt(idColumnIndex);
        partner.title = cursor.getString(titleColumnIndex);
        partner.fullTitle = cursor.getString(fullTitleColumnIndex);
        partner.discountType = cursor.getString(discountTypeColumnIndex);
        partner.discountSize = cursor.getInt(discountSizeColumnIndex);
        partner.categoryId = cursor.getInt(categoryIdColumnIndex);
        partner.imageUrl = cursor.getString(imageUrlColumnIndex);
        partner.description = cursor.getString(descriptionColumnIndex);
        byte[] webSitesBlob = cursor.getBlob(webSitesColumnIndex);
        partner.webSites = (List<String>) SerializationUtils.deserialize(webSitesBlob);
        byte[] emailsBlob = cursor.getBlob(emailsColumnIndex);
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
                PartnersContracts.PartnersContract.COLUMN_NAME_ID + " = " + partnerId;
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
