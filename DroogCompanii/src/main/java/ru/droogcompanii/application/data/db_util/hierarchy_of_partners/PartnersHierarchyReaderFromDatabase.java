package ru.droogcompanii.application.data.db_util.hierarchy_of_partners;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import ru.droogcompanii.application.data.db_util.BaseReaderFromDatabase;
import ru.droogcompanii.application.data.db_util.CursorHandler;

/**
 * Created by Leonid on 17.12.13.
 */
public class PartnersHierarchyReaderFromDatabase extends BaseReaderFromDatabase {

    public PartnersHierarchyReaderFromDatabase(Context context) {
        super(context);
    }

    public void handleCursorByQuery(String sql, CursorHandler cursorHandler) {
        initDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        cursorHandler.handle(cursor);

        cursor.close();
        closeDatabase();
    }

    @Override
    protected SQLiteOpenHelper prepareDbHelper(Context context) {
        return new PartnersDbHelper(context);
    }
}
