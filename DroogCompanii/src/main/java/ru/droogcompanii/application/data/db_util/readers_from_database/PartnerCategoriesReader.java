package ru.droogcompanii.application.data.db_util.readers_from_database;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.db_util.DroogCompaniiContracts.PartnerCategoriesContract;

/**
 * Created by Leonid on 17.12.13.
 */
public class PartnerCategoriesReader extends BaseReaderFromDatabase {

    private int idColumnIndex;
    private int titleColumnIndex;

    public PartnerCategoriesReader(Context context) {
        super(context);
    }

    public List<PartnerCategory> getPartnerCategories() {
        initDatabase();
        List<PartnerCategory> partnerCategories = getPartnerCategoriesFromDatabase();
        closeDatabase();
        return partnerCategories;
    }

    private List<PartnerCategory> getPartnerCategoriesFromDatabase() {
        String sql = "SELECT * FROM " + PartnerCategoriesContract.TABLE_NAME + ";";
        Cursor cursor = db.rawQuery(sql, new String[]{});
        List<PartnerCategory> partnerCategoryTitles = getPartnerCategoriesFromCursor(cursor);
        cursor.close();
        return partnerCategoryTitles;
    }

    private List<PartnerCategory> getPartnerCategoriesFromCursor(Cursor cursor) {
        List<PartnerCategory> partnerCategories = new ArrayList<PartnerCategory>();
        calculateColumnIndices(cursor);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            PartnerCategory partnerCategory = getPartnerCategoryFromCursor(cursor);
            partnerCategories.add(partnerCategory);
            cursor.moveToNext();
        }
        return partnerCategories;
    }

    private void calculateColumnIndices(Cursor cursor) {
        idColumnIndex = cursor.getColumnIndexOrThrow(PartnerCategoriesContract.COLUMN_NAME_ID);
        titleColumnIndex = cursor.getColumnIndexOrThrow(PartnerCategoriesContract.COLUMN_NAME_TITLE);
    }

    private PartnerCategory getPartnerCategoryFromCursor(Cursor cursor) {
        int id = cursor.getInt(idColumnIndex);
        String title = cursor.getString(titleColumnIndex);
        return new PartnerCategory(id, title);
    }
}
