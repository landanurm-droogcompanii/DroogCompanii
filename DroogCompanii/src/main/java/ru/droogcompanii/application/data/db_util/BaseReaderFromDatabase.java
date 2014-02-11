package ru.droogcompanii.application.data.db_util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ls on 11.02.14.
 */
public abstract class BaseReaderFromDatabase {
    private final Context context;
    private SQLiteOpenHelper dbHelper;
    protected SQLiteDatabase db;

    protected BaseReaderFromDatabase(Context context) {
        this.context = context;
    }

    protected final void initDatabase() {
        dbHelper = prepareDbHelper(context);
        db = dbHelper.getReadableDatabase();
    }

    protected abstract SQLiteOpenHelper prepareDbHelper(Context context);

    protected final void closeDatabase() {
        db.close();
        db = null;
        dbHelper.close();
        dbHelper = null;
    }
}
