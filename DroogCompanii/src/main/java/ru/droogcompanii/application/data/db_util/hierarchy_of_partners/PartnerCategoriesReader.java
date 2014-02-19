package ru.droogcompanii.application.data.db_util.hierarchy_of_partners;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersContracts.PartnerCategoriesContract;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategoryImpl;
import ru.droogcompanii.application.util.StringsCombiner;

/**
 * Created by Leonid on 17.12.13.
 */
public class PartnerCategoriesReader extends BasePartnersReaderFromDatabase {

    private int idColumnIndex;
    private int titleColumnIndex;

    public PartnerCategoriesReader(Context context) {
        super(context);
    }

    public List<PartnerCategory> getPartnerCategories() {
        return getPartnerCategoriesFromDatabase("");
    }

    private List<PartnerCategory> getPartnerCategoriesFromDatabase(String where) {
        String sql = "SELECT * FROM " + PartnerCategoriesContract.TABLE_NAME + " " + where + ";";
        initDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{});
        List<PartnerCategory> partnerCategories = getPartnerCategoriesFromCursor(cursor);
        cursor.close();
        closeDatabase();
        return partnerCategories;
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
        PartnerCategoryImpl partnerCategory = new PartnerCategoryImpl();
        partnerCategory.id = cursor.getInt(idColumnIndex);
        partnerCategory.title = cursor.getString(titleColumnIndex);
        return partnerCategory;
    }

    public PartnerCategory getPartnerCategoryOf(Partner partner) {
        List<PartnerCategory> partnerCategories = getPartnerCategoriesFromDatabase(
                " WHERE " + PartnerCategoriesContract.COLUMN_NAME_ID + " = " + partner.getCategoryId()
        );
        if (partnerCategories.isEmpty()) {
            throw new IllegalArgumentException("partner with illegal category id");
        } else if (partnerCategories.size() > 1) {
            throw new IllegalStateException(
                    "The database has several categories with the same id:  " +
                            StringsCombiner.combine(partnerCategories, ", ")
            );
        }
        return partnerCategories.get(0);
    }
}
