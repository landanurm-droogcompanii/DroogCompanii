package ru.droogcompanii.application.ui.fragment.search_result_list.adapter;

import android.content.Context;
import android.database.Cursor;

import ru.droogcompanii.application.data.db_util.CursorHandler;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersContracts;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersHierarchyReaderFromDatabase;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerCategory;
import ru.droogcompanii.application.util.Holder;

/**
 * Created by ls on 11.03.14.
 */
class PartnersCounter {

    public static int countPartnersInCategory(Context context, PartnerCategory category) {
        PartnersHierarchyReaderFromDatabase reader = new PartnersHierarchyReaderFromDatabase(context);
        String sql = "SELECT * FROM " + PartnersContracts.PartnersContract.TABLE_NAME + " WHERE " +
                PartnersContracts.PartnersContract.COLUMN_NAME_CATEGORY_ID + "=" + category.getId() + ";";
        final Holder<Integer> counterHolder = Holder.from(0);
        reader.handleCursorByQuery(sql, new CursorHandler() {
            @Override
            public void handle(Cursor cursor) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    counterHolder.value += 1;
                    cursor.moveToNext();
                }
            }
        });
        return counterHolder.value;
    }
}
