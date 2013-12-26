package ru.droogcompanii.application.data.db_util.readers_from_database;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.data_structure.PartnerCategory;
import ru.droogcompanii.application.data.db_util.DroogCompaniiContracts.PartnerCategoriesContract;

/**
 * Created by Leonid on 17.12.13.
 */
public class PartnerCategoriesReader extends BaseReaderFromDatabase {

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
        int idColumnIndex = cursor.getColumnIndexOrThrow(PartnerCategoriesContract.COLUMN_NAME_ID);
        int titleColumnIndex = cursor.getColumnIndexOrThrow(PartnerCategoriesContract.COLUMN_NAME_TITLE);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(idColumnIndex);
            String title = cursor.getString(titleColumnIndex);
            partnerCategories.add(new PartnerCategory(id, title));
            cursor.moveToNext();
        }
        return partnerCategories;
    }
}
