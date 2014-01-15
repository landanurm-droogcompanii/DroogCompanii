package ru.droogcompanii.application.data.db_util.readers_from_database;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.data_structure.Partner;
import ru.droogcompanii.application.data.data_structure.PartnerCategory;
import ru.droogcompanii.application.data.data_structure.PartnerPoint;
import ru.droogcompanii.application.data.db_util.DroogCompaniiContracts;

/**
 * Created by Leonid on 17.12.13.
 */
public class PartnersReader extends BaseReaderFromDatabase {

    private int idColumnIndex;
    private int titleColumnIndex;
    private int fullTitleColumnIndex;
    private int saleTypeColumnIndex;
    private int categoryIdColumnIndex;

    public PartnersReader(Context context) {
        super(context);
    }

    public List<Partner> getPartnersOf(PartnerCategory category) {
        String where = " WHERE " +
                DroogCompaniiContracts.PartnersContract.COLUMN_NAME_CATEGORY_ID + " = " + category.id;
        initDatabase();
        List<Partner> partners = getPartners(where);
        closeDatabase();
        return partners;
    }

    private List<Partner> getPartners(String where) {
        String sql = "SELECT * FROM " + DroogCompaniiContracts.PartnersContract.TABLE_NAME + where + " ;";
        Cursor cursor = db.rawQuery(sql, null);
        List<Partner> partners = getPartnersFromCursor(cursor);
        cursor.close();
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
        idColumnIndex = cursor.getColumnIndexOrThrow(DroogCompaniiContracts.PartnersContract.COLUMN_NAME_ID);
        titleColumnIndex = cursor.getColumnIndexOrThrow(DroogCompaniiContracts.PartnersContract.COLUMN_NAME_TITLE);
        fullTitleColumnIndex = cursor.getColumnIndexOrThrow(DroogCompaniiContracts.PartnersContract.COLUMN_NAME_FULL_TITLE);
        saleTypeColumnIndex = cursor.getColumnIndexOrThrow(DroogCompaniiContracts.PartnersContract.COLUMN_NAME_SALE_TYPE);
        categoryIdColumnIndex = cursor.getColumnIndexOrThrow(DroogCompaniiContracts.PartnersContract.COLUMN_NAME_CATEGORY_ID);
    }

    @SuppressWarnings("unchecked")
    private Partner getPartnerFromCursor(Cursor cursor) {
        int id = cursor.getInt(idColumnIndex);
        String title = cursor.getString(titleColumnIndex);
        String fullTitle = cursor.getString(fullTitleColumnIndex);
        String saleType = cursor.getString(saleTypeColumnIndex);
        int categoryId = cursor.getInt(categoryIdColumnIndex);
        return new Partner(id, title, fullTitle, saleType, categoryId);
    }

    public Partner getPartnerOf(PartnerPoint partnerPoint) {
        String where = " WHERE " +
                DroogCompaniiContracts.PartnersContract.COLUMN_NAME_ID + " = " + partnerPoint.partnerId;
        initDatabase();
        List<Partner> partner = getPartners(where);
        closeDatabase();
        if (partner.isEmpty()) {
            throw new IllegalArgumentException("No partners for partner point: " + partnerPoint);
        } else {
            return partner.get(0);
        }
    }
}
