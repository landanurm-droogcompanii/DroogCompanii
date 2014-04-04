package ru.droogcompanii.application.data.db_util.hierarchy_of_partners;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Arrays;
import java.util.List;

import ru.droogcompanii.application.data.db_util.contracts.EmailsContract;
import ru.droogcompanii.application.data.db_util.contracts.PartnerCategoriesContract;
import ru.droogcompanii.application.data.db_util.contracts.PartnerPointsContract;
import ru.droogcompanii.application.data.db_util.contracts.PartnersContract;
import ru.droogcompanii.application.data.db_util.contracts.PhonesContract;
import ru.droogcompanii.application.data.db_util.contracts.WebSitesContract;
import ru.droogcompanii.application.data.db_util.contracts.working_hours.BusinessDayContract;
import ru.droogcompanii.application.data.db_util.contracts.working_hours.DayAndNightContract;
import ru.droogcompanii.application.data.db_util.contracts.working_hours.ExcludedTimeRangesContract;
import ru.droogcompanii.application.data.db_util.contracts.working_hours.HolidayContract;
import ru.droogcompanii.application.data.db_util.contracts.working_hours.IncludedTimeRangesContract;

/**
 * Created by Leonid on 09.12.13.
 */
public class PartnerHierarchyDbHelper extends SQLiteOpenHelper {

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
                    PartnerCategoriesContract.COLUMN_ID + INTEGER_TYPE + COMMA +
                    PartnerCategoriesContract.COLUMN_TITLE + TEXT_TYPE + NOT_NULL + COMMA +
                    PartnerCategoriesContract.COLUMN_TEXT_SEARCH_IN + TEXT_TYPE + NOT_NULL +
            " )";

    private static final String SQL_CREATE_PARTNERS_TABLE =
            "CREATE TABLE " + PartnersContract.TABLE_NAME + " (" +
                    PartnersContract._ID + INTEGER_TYPE + PRIMARY_KEY_TYPE_AUTOINCREMENT + COMMA +
                    PartnersContract.COLUMN_ID + INTEGER_TYPE + COMMA +
                    PartnersContract.COLUMN_TITLE + TEXT_TYPE + NOT_NULL + COMMA +
                    PartnersContract.COLUMN_FULL_TITLE + TEXT_TYPE + NOT_NULL + COMMA +
                    PartnersContract.COLUMN_DISCOUNT_TYPE + TEXT_TYPE + NOT_NULL + COMMA +
                    PartnersContract.COLUMN_DISCOUNT_SIZE + INTEGER_TYPE + NOT_NULL + COMMA +
                    PartnersContract.COLUMN_CATEGORY_ID + INTEGER_TYPE + NOT_NULL + COMMA +
                    PartnersContract.COLUMN_IMAGE_URL + TEXT_TYPE + NOT_NULL + COMMA +
                    PartnersContract.COLUMN_DESCRIPTION + TEXT_TYPE + NOT_NULL + COMMA +
                    PartnersContract.COLUMN_WEB_SITES + BLOB_TYPE + NOT_NULL + COMMA +
                    PartnersContract.COLUMN_EMAILS + BLOB_TYPE + NOT_NULL + COMMA +
                    PartnersContract.COLUMN_IS_FAVORITE + INTEGER_TYPE + NOT_NULL + COMMA +
                    PartnersContract.COLUMN_TEXT_SEARCH_IN + TEXT_TYPE + NOT_NULL +
            " )";

    private static final String SQL_CREATE_PARTNER_POINTS_TABLE =
            "CREATE TABLE " + PartnerPointsContract.TABLE_NAME + " (" +
                    PartnerPointsContract._ID + INTEGER_TYPE + PRIMARY_KEY_TYPE_AUTOINCREMENT + COMMA +
                    PartnerPointsContract.COLUMN_ID + INTEGER_TYPE + COMMA +
                    PartnerPointsContract.COLUMN_TITLE + TEXT_TYPE + NOT_NULL + COMMA +
                    PartnerPointsContract.COLUMN_ADDRESS + TEXT_TYPE + NOT_NULL + COMMA +
                    PartnerPointsContract.COLUMN_LONGITUDE + REAL_TYPE + NOT_NULL + COMMA +
                    PartnerPointsContract.COLUMN_LATITUDE + REAL_TYPE + NOT_NULL + COMMA +
                    PartnerPointsContract.COLUMN_PAYMENT_METHODS + TEXT_TYPE + NOT_NULL + COMMA +
                    PartnerPointsContract.COLUMN_PHONES + BLOB_TYPE + NOT_NULL + COMMA +
                    PartnerPointsContract.COLUMN_WORKING_HOURS + BLOB_TYPE + NOT_NULL + COMMA +
                    PartnerPointsContract.COLUMN_PARTNER_ID + INTEGER_TYPE + NOT_NULL + COMMA +
                    PartnerPointsContract.COLUMN_TEXT_SEARCH_IN + TEXT_TYPE + NOT_NULL +
            " )";

    private static final String SQL_CREATE_WEBSITES_TABLE =
            "CREATE TABLE " + WebSitesContract.TABLE_NAME + " (" +
                    WebSitesContract._ID + INTEGER_TYPE + PRIMARY_KEY_TYPE_AUTOINCREMENT + COMMA +
                    WebSitesContract.COLUMN_OWNER_ID + INTEGER_TYPE + NOT_NULL + COMMA +
                    WebSitesContract.COLUMN_URL + TEXT_TYPE + NOT_NULL +
            " )";

    private static final String SQL_CREATE_EMAILS_TABLE =
            "CREATE TABLE " + EmailsContract.TABLE_NAME + " (" +
                    EmailsContract._ID + INTEGER_TYPE + PRIMARY_KEY_TYPE_AUTOINCREMENT + COMMA +
                    EmailsContract.COLUMN_OWNER_ID + INTEGER_TYPE + NOT_NULL + COMMA +
                    EmailsContract.COLUMN_ADDRESS + TEXT_TYPE + NOT_NULL +
            " )";

    private static final String SQL_CREATE_PHONES_TABLE =
            "CREATE TABLE " + PhonesContract.TABLE_NAME + " (" +
                    PhonesContract._ID + INTEGER_TYPE + PRIMARY_KEY_TYPE_AUTOINCREMENT + COMMA +
                    PhonesContract.COLUMN_OWNER_ID + INTEGER_TYPE + NOT_NULL + COMMA +
                    PhonesContract.COLUMN_NUMBER + TEXT_TYPE + NOT_NULL +
            " )";

    private static final String SQL_CREATE_DAY_AND_NIGHT_WORKING_HOURS_TABLE =
            "CREATE TABLE " + DayAndNightContract.TABLE_NAME + " (" +
                    DayAndNightContract._ID + INTEGER_TYPE + PRIMARY_KEY_TYPE_AUTOINCREMENT + COMMA +
                    DayAndNightContract.COLUMN_POINT_ID + INTEGER_TYPE + NOT_NULL + COMMA +
                    DayAndNightContract.COLUMN_DAY_OF_WEEK + INTEGER_TYPE + NOT_NULL + COMMA +
                    DayAndNightContract.COLUMN_MESSAGE + TEXT_TYPE + NOT_NULL +
            " )";

    private static final String SQL_CREATE_HOLIDAY_WORKING_HOURS_TABLE =
            "CREATE TABLE " + HolidayContract.TABLE_NAME + " (" +
                    HolidayContract._ID + INTEGER_TYPE + PRIMARY_KEY_TYPE_AUTOINCREMENT + COMMA +
                    HolidayContract.COLUMN_POINT_ID + INTEGER_TYPE + NOT_NULL + COMMA +
                    HolidayContract.COLUMN_DAY_OF_WEEK + INTEGER_TYPE + NOT_NULL + COMMA +
                    HolidayContract.COLUMN_MESSAGE + TEXT_TYPE + NOT_NULL +
            " )";

    private static final String SQL_CREATE_BUSINESS_DAY_WORKING_HOURS_TABLE =
            "CREATE TABLE " + BusinessDayContract.TABLE_NAME + " (" +
                    BusinessDayContract._ID + INTEGER_TYPE + PRIMARY_KEY_TYPE_AUTOINCREMENT + COMMA +
                    BusinessDayContract.COLUMN_POINT_ID + INTEGER_TYPE + NOT_NULL + COMMA +
                    BusinessDayContract.COLUMN_DAY_OF_WEEK + INTEGER_TYPE + NOT_NULL +
            " )";

    private static final String SQL_CREATE_INCLUDED_RANGES_TABLE =
            "CREATE TABLE " + IncludedTimeRangesContract.TABLE_NAME + " (" +
                    IncludedTimeRangesContract._ID + INTEGER_TYPE + PRIMARY_KEY_TYPE_AUTOINCREMENT + COMMA +
                    IncludedTimeRangesContract.COLUMN_WORKING_HOURS_ID + INTEGER_TYPE + NOT_NULL + COMMA +
                    IncludedTimeRangesContract.COLUMN_FROM + INTEGER_TYPE + NOT_NULL + COMMA +
                    IncludedTimeRangesContract.COLUMN_TO + INTEGER_TYPE + NOT_NULL +
            " )";

    private static final String SQL_CREATE_EXCLUDED_RANGES_TABLE =
            "CREATE TABLE " + ExcludedTimeRangesContract.TABLE_NAME + " (" +
                    ExcludedTimeRangesContract._ID + INTEGER_TYPE + PRIMARY_KEY_TYPE_AUTOINCREMENT + COMMA +
                    ExcludedTimeRangesContract.COLUMN_WORKING_HOURS_ID + INTEGER_TYPE + NOT_NULL + COMMA +
                    ExcludedTimeRangesContract.COLUMN_FROM + INTEGER_TYPE + NOT_NULL + COMMA +
                    ExcludedTimeRangesContract.COLUMN_TO + INTEGER_TYPE + NOT_NULL +
            " )";


    private static final List<String> SQL_CREATE_TABLES = Arrays.asList(
            SQL_CREATE_PARTNER_CATEGORIES_TABLE,
            SQL_CREATE_PARTNERS_TABLE,
            SQL_CREATE_PARTNER_POINTS_TABLE,
            SQL_CREATE_WEBSITES_TABLE,
            SQL_CREATE_EMAILS_TABLE,
            SQL_CREATE_PHONES_TABLE,
            SQL_CREATE_DAY_AND_NIGHT_WORKING_HOURS_TABLE,
            SQL_CREATE_HOLIDAY_WORKING_HOURS_TABLE,
            SQL_CREATE_BUSINESS_DAY_WORKING_HOURS_TABLE,
            SQL_CREATE_INCLUDED_RANGES_TABLE,
            SQL_CREATE_EXCLUDED_RANGES_TABLE
    );

    public static final List<String> TABLE_NAMES = Arrays.asList(
            PartnerCategoriesContract.TABLE_NAME,
            PartnersContract.TABLE_NAME,
            PartnerPointsContract.TABLE_NAME,
            WebSitesContract.TABLE_NAME,
            EmailsContract.TABLE_NAME,
            PhonesContract.TABLE_NAME,
            DayAndNightContract.TABLE_NAME,
            HolidayContract.TABLE_NAME,
            BusinessDayContract.TABLE_NAME,
            IncludedTimeRangesContract.TABLE_NAME,
            ExcludedTimeRangesContract.TABLE_NAME
    );


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Partners.db";

    public PartnerHierarchyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    private void createTables(SQLiteDatabase db) {
        for (String sqlCreateTable : SQL_CREATE_TABLES) {
            db.execSQL(sqlCreateTable);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTables(db);
        createTables(db);
    }

    private void deleteTables(SQLiteDatabase db) {
        for (String sqlDeleteTable : TABLE_NAMES) {
            db.execSQL("DROP TABLE IF EXISTS " + sqlDeleteTable);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
