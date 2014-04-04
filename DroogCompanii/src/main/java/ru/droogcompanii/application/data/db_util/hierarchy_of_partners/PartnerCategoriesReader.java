package ru.droogcompanii.application.data.db_util.hierarchy_of_partners;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.db_util.contracts.*;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategoryImpl;
import ru.droogcompanii.application.util.StringsCombiner;

/**
 * Created by Leonid on 17.12.13.
 */
public class PartnerCategoriesReader extends PartnersHierarchyReaderFromDatabase {

    private static class ColumnIndices {
        int idColumnIndex;
        int titleColumnIndex;
    }

    public PartnerCategoriesReader(Context context) {
        super(context);
    }

    public List<PartnerCategory> getAllPartnerCategories() {
        return getPartnerCategoriesByWhere("");
    }

    private List<PartnerCategory> getPartnerCategoriesByWhere(String where) {
        String sql = "SELECT * FROM " + PartnerCategoriesContract.TABLE_NAME + " " + where + " " + getDefaultOrder() + ";";
        initDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{});
        List<PartnerCategory> partnerCategories = getPartnerCategoriesFromCursor(cursor);
        cursor.close();
        closeDatabase();
        return partnerCategories;
    }

    public static String getDefaultOrder() {
        return "ORDER BY " + PartnerCategoriesContract.COLUMN_TITLE;
    }

    public static List<PartnerCategory> getPartnerCategoriesFromCursor(Cursor cursor) {
        List<PartnerCategory> partnerCategories = new ArrayList<PartnerCategory>();
        ColumnIndices columnIndices = calculateColumnIndices(cursor);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            PartnerCategory partnerCategory = getPartnerCategoryFromCursor(cursor, columnIndices);
            partnerCategories.add(partnerCategory);
            cursor.moveToNext();
        }
        return partnerCategories;
    }

    private static ColumnIndices calculateColumnIndices(Cursor cursor) {
        ColumnIndices columnIndices = new ColumnIndices();
        columnIndices.idColumnIndex = cursor.getColumnIndexOrThrow(PartnerCategoriesContract.COLUMN_ID);
        columnIndices.titleColumnIndex = cursor.getColumnIndexOrThrow(PartnerCategoriesContract.COLUMN_TITLE);
        return columnIndices;
    }

    private static PartnerCategory getPartnerCategoryFromCursor(Cursor cursor, ColumnIndices columnIndices) {
        PartnerCategoryImpl partnerCategory = new PartnerCategoryImpl();
        partnerCategory.id = cursor.getInt(columnIndices.idColumnIndex);
        partnerCategory.title = cursor.getString(columnIndices.titleColumnIndex);
        return partnerCategory;
    }

    public PartnerCategory getPartnerCategoryOf(Partner partner) {
        List<PartnerCategory> partnerCategories = getPartnerCategoriesByWhere(
                " WHERE " + PartnerCategoriesContract.COLUMN_ID + " = " + partner.getCategoryId()
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
