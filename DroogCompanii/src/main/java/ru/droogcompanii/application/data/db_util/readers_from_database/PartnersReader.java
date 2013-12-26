package ru.droogcompanii.application.data.db_util.readers_from_database;

import android.content.Context;
import android.database.Cursor;

import ru.droogcompanii.application.data.data_structure.Partner;
import ru.droogcompanii.application.data.data_structure.PartnerCategory;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.db_util.DroogCompaniiContracts;

/**
 * Created by Leonid on 17.12.13.
 */
public class PartnersReader extends BaseReaderFromDatabase {

    public PartnersReader(Context context) {
        super(context);
    }

    public List<Partner> getPartners(PartnerCategory category) {
        initDatabase();
        List<Partner> partners = getPartnersByCategoryIdFromDatabase(category.id);
        closeDatabase();
        return partners;
    }

    private List<Partner> getPartnersByCategoryIdFromDatabase(int categoryId) {
        String sql = "SELECT * FROM " + DroogCompaniiContracts.PartnersContract.TABLE_NAME +
                " WHERE " + DroogCompaniiContracts.PartnersContract.COLUMN_NAME_CATEGORY_ID + " = ? ;";
        String[] selectionArgs = { String.valueOf(categoryId) };
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        List<Partner> partners = getPartnersFromCursor(cursor);
        cursor.close();
        return partners;
    }

    private List<Partner> getPartnersFromCursor(Cursor cursor) {
        List<Partner> partners = new ArrayList<Partner>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            partners.add(getPartnerFromCursor(cursor));
            cursor.moveToNext();
        }
        return partners;
    }

    @SuppressWarnings("unchecked")
    private Partner getPartnerFromCursor(Cursor cursor) {
        int idColumnIndex = cursor.getColumnIndexOrThrow(DroogCompaniiContracts.PartnersContract.COLUMN_NAME_ID);
        int titleColumnIndex = cursor.getColumnIndexOrThrow(DroogCompaniiContracts.PartnersContract.COLUMN_NAME_TITLE);
        int fullTitleColumnIndex = cursor.getColumnIndexOrThrow(DroogCompaniiContracts.PartnersContract.COLUMN_NAME_FULL_TITLE);
        int saleTypeColumnIndex = cursor.getColumnIndexOrThrow(DroogCompaniiContracts.PartnersContract.COLUMN_NAME_SALE_TYPE);
        int categoryIdColumnIndex = cursor.getColumnIndexOrThrow(DroogCompaniiContracts.PartnersContract.COLUMN_NAME_CATEGORY_ID);

        int id = cursor.getInt(idColumnIndex);
        String title = cursor.getString(titleColumnIndex);
        String fullTitle = cursor.getString(fullTitleColumnIndex);
        String saleType = cursor.getString(saleTypeColumnIndex);
        int categoryId = cursor.getInt(categoryIdColumnIndex);

        return new Partner(id, title, fullTitle, saleType, categoryId);
    }
}
