package ru.droogcompanii.application.data.db_util.hierarchy_of_partners;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.DroogCompaniiApplication;
import ru.droogcompanii.application.data.db_util.CursorHandler;

/**
 * Created by ls on 03.04.14.
 */
public class ListOfStringsReader {

    public static interface Arguments {
        String getTableName();
        String getOwnerIdColumnName();
        String getRequiredColumnName();
    }

    public static List<String> readByOwnerId(int ownerId, Arguments arguments) {
        Context appContext = DroogCompaniiApplication.getContext();
        PartnersHierarchyReaderFromDatabase reader = new PartnersHierarchyReaderFromDatabase(appContext);

        final String requiredColumnName = arguments.getRequiredColumnName();
        String sql = "SELECT " + requiredColumnName + " FROM " + arguments.getTableName() +
                " WHERE " + arguments.getOwnerIdColumnName() + "=" + ownerId + ";";
        final List<String> result = new ArrayList<String>();
        reader.handleCursorByQuery(sql, new CursorHandler() {
            @Override
            public void handle(Cursor cursor) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    
                    int indexOfRequiredColumn = cursor.getColumnIndexOrThrow(requiredColumnName);
                    String each = cursor.getString(indexOfRequiredColumn);
                    result.add(each);

                    cursor.moveToNext();
                }
            }
        });
        return result;
    }
}
