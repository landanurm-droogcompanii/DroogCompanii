package ru.droogcompanii.application.data.db_util.offers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.droogcompanii.application.data.db_util.contracts.OffersContract;

/**
 * Created by ls on 11.02.14.
 */
public class OffersDbHelper extends SQLiteOpenHelper {

    private static final OffersContract CONTRACT = new OffersContract();

    private static final String INTEGER_TYPE = " INTEGER ";
    private static final String TEXT_TYPE = " TEXT ";
    private static final String PRIMARY_KEY_TYPE_AUTOINCREMENT = " PRIMARY KEY AUTOINCREMENT ";
    private static final String COMMA = ", ";
    private static final String NOT_NULL = " NOT_NULL ";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + CONTRACT.TABLE_NAME + " (" +
                    CONTRACT._ID + INTEGER_TYPE + PRIMARY_KEY_TYPE_AUTOINCREMENT + COMMA +
                    CONTRACT.COLUMN_ID + INTEGER_TYPE + NOT_NULL + COMMA +
                    CONTRACT.COLUMN_PARTNER_ID + INTEGER_TYPE + NOT_NULL + COMMA +
                    CONTRACT.COLUMN_FROM + INTEGER_TYPE + COMMA +
                    CONTRACT.COLUMN_TO + INTEGER_TYPE + COMMA +
                    CONTRACT.COLUMN_SHORT_DESCRIPTION + TEXT_TYPE + NOT_NULL + COMMA +
                    CONTRACT.COLUMN_FULL_DESCRIPTION + TEXT_TYPE + NOT_NULL + COMMA +
                    CONTRACT.COLUMN_IMAGE_URL + NOT_NULL + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + CONTRACT.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Offers.db";

    public OffersDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
