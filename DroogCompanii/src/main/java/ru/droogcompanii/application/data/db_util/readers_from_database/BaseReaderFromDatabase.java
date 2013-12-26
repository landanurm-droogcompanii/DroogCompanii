package ru.droogcompanii.application.data.db_util.readers_from_database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import ru.droogcompanii.application.data.db_util.DroogCompaniiDbHelper;

/**
 * Created by Leonid on 17.12.13.
 */
class BaseReaderFromDatabase {
    private final Context context;
    private DroogCompaniiDbHelper dbHelper;
    protected SQLiteDatabase db;

    BaseReaderFromDatabase(Context context) {
        this.context = context;
    }

    protected final void initDatabase() {
        dbHelper = new DroogCompaniiDbHelper(context);
        db = dbHelper.getReadableDatabase();
    }

    protected final void releaseDatabase() {
        db.close();
        db = null;
        dbHelper.close();
        dbHelper = null;
    }
}
