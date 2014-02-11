package ru.droogcompanii.application.data.db_util.hierarchy_of_partners;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersContracts.PartnerCategoriesContract;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersContracts.PartnerPointsContract;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersContracts.PartnersContract;

/**
 * Created by Leonid on 09.12.13.
 */
public class PartnersDbHelper extends SQLiteOpenHelper {

    private static final String INTEGER_TYPE = " INTEGER ";
    private static final String TEXT_TYPE = " TEXT ";
    private static final String REAL_TYPE = " REAL ";
    private static final String BLOB_TYPE = " BLOB ";
    private static final String PRIMARY_KEY_TYPE_AUTOINCREMENT = " PRIMARY KEY AUTOINCREMENT ";
    private static final String COMMA = ", ";
    private static final String NOT_NULL = " NOT_NULL ";

    private static final String SQL_CREATE_PARTNER_CATEGORIES_TABLE =
            "CREATE TABLE " + PartnerCategoriesContract.TABLE_NAME + " (" +
                    PartnerCategoriesContract._ID + INTEGER_TYPE + PRIMARY_KEY_TYPE_AUTOINCREMENT + COMMA +
                    PartnerCategoriesContract.COLUMN_NAME_ID + INTEGER_TYPE + COMMA +
                    PartnerCategoriesContract.COLUMN_NAME_TITLE + TEXT_TYPE + NOT_NULL +
            " )";

    private static final String SQL_CREATE_PARTNERS_TABLE =
            "CREATE TABLE " + PartnersContract.TABLE_NAME + " (" +
                    PartnersContract._ID + INTEGER_TYPE + PRIMARY_KEY_TYPE_AUTOINCREMENT + COMMA +
                    PartnersContract.COLUMN_NAME_ID + INTEGER_TYPE + COMMA +
                    PartnersContract.COLUMN_NAME_TITLE  + TEXT_TYPE + NOT_NULL + COMMA +
                    PartnersContract.COLUMN_NAME_FULL_TITLE + TEXT_TYPE + NOT_NULL + COMMA +
                    PartnersContract.COLUMN_NAME_DISCOUNT_TYPE + TEXT_TYPE + NOT_NULL + COMMA +
                    PartnersContract.COLUMN_NAME_DISCOUNT + INTEGER_TYPE + NOT_NULL + COMMA +
                    PartnersContract.COLUMN_NAME_CATEGORY_ID + INTEGER_TYPE + NOT_NULL +
            " )";

    private static final String SQL_CREATE_PARTNER_POINTS_TABLE =
            "CREATE TABLE " + PartnerPointsContract.TABLE_NAME + " (" +
                    PartnerPointsContract._ID + INTEGER_TYPE + PRIMARY_KEY_TYPE_AUTOINCREMENT + COMMA +
                    PartnerPointsContract.COLUMN_NAME_TITLE + TEXT_TYPE + NOT_NULL + COMMA +
                    PartnerPointsContract.COLUMN_NAME_ADDRESS + TEXT_TYPE + NOT_NULL + COMMA +
                    PartnerPointsContract.COLUMN_NAME_LONGITUDE + REAL_TYPE + NOT_NULL + COMMA +
                    PartnerPointsContract.COLUMN_NAME_LATITUDE + REAL_TYPE + NOT_NULL + COMMA +
                    PartnerPointsContract.COLUMN_NAME_PAYMENT_METHODS + TEXT_TYPE + NOT_NULL + COMMA +
                    PartnerPointsContract.COLUMN_NAME_PHONES + BLOB_TYPE + NOT_NULL + COMMA +
                    PartnerPointsContract.COLUMN_NAME_WORKING_HOURS + BLOB_TYPE + NOT_NULL + COMMA +
                    PartnerPointsContract.COLUMN_NAME_PARTNER_ID + INTEGER_TYPE + NOT_NULL +
            " )";

    private static final String SQL_DELETE_PARTNER_CATEGORIES_TABLE =
            "DROP TABLE IF EXISTS " + PartnerCategoriesContract.TABLE_NAME;

    private static final String SQL_DELETE_PARTNERS_TABLE =
            "DROP TABLE IF EXISTS " + PartnersContract.TABLE_NAME;

    private static final String SQL_DELETE_PARTNER_POINTS_TABLE =
            "DROP TABLE IF EXISTS " + PartnerPointsContract.TABLE_NAME;


    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Partners.db";

    public PartnersDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PARTNER_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_PARTNERS_TABLE);
        db.execSQL(SQL_CREATE_PARTNER_POINTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_PARTNER_CATEGORIES_TABLE);
        db.execSQL(SQL_DELETE_PARTNERS_TABLE);
        db.execSQL(SQL_DELETE_PARTNER_POINTS_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
