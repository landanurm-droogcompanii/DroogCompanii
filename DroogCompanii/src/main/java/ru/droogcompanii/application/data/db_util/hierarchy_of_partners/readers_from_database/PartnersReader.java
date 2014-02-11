package ru.droogcompanii.application.data.db_util.hierarchy_of_partners.readers_from_database;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersContracts;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;

/**
 * Created by Leonid on 17.12.13.
 */
public class PartnersReader extends BasePartnersReaderFromDatabase {

    private int idColumnIndex;
    private int titleColumnIndex;
    private int fullTitleColumnIndex;
    private int discountTypeColumnIndex;
    private int discountColumnIndex;
    private int categoryIdColumnIndex;

    public PartnersReader(Context context) {
        super(context);
    }

    public List<Partner> getPartnersOf(PartnerCategory category) {
        String where = " WHERE " +
                PartnersContracts.PartnersContract.COLUMN_NAME_CATEGORY_ID + " = " + category.id;
        return getPartners(where);
    }

    private List<Partner> getPartners(String where) {
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
        idColumnIndex = cursor.getColumnIndexOrThrow(PartnersContracts.PartnersContract.COLUMN_NAME_ID);
        titleColumnIndex = cursor.getColumnIndexOrThrow(PartnersContracts.PartnersContract.COLUMN_NAME_TITLE);
        fullTitleColumnIndex = cursor.getColumnIndexOrThrow(PartnersContracts.PartnersContract.COLUMN_NAME_FULL_TITLE);
        discountTypeColumnIndex = cursor.getColumnIndexOrThrow(PartnersContracts.PartnersContract.COLUMN_NAME_DISCOUNT_TYPE);
        discountColumnIndex = cursor.getColumnIndexOrThrow(PartnersContracts.PartnersContract.COLUMN_NAME_DISCOUNT);
        categoryIdColumnIndex = cursor.getColumnIndexOrThrow(PartnersContracts.PartnersContract.COLUMN_NAME_CATEGORY_ID);
    }

    @SuppressWarnings("unchecked")
    private Partner getPartnerFromCursor(Cursor cursor) {
        int id = cursor.getInt(idColumnIndex);
        String title = cursor.getString(titleColumnIndex);
        String fullTitle = cursor.getString(fullTitleColumnIndex);
        String discountType = cursor.getString(discountTypeColumnIndex);
        int discount = cursor.getInt(discountColumnIndex);
        int categoryId = cursor.getInt(categoryIdColumnIndex);
        return new Partner(id, title, fullTitle, discountType, discount, categoryId);
    }

    public Partner getPartnerOf(PartnerPoint partnerPoint) {
        String where = " WHERE " +
                PartnersContracts.PartnersContract.COLUMN_NAME_ID + " = " + partnerPoint.partnerId;
        List<Partner> partner = getPartners(where);
        if (partner.isEmpty()) {
            throw new IllegalArgumentException("No partners for partner point: " + partnerPoint);
        }
        return partner.get(0);
    }

    public List<Partner> getAllPartners() {
        return getPartners("");
    }
}
