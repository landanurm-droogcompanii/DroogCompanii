package ru.droogcompanii.application.data.db_util.contracts;

import android.provider.BaseColumns;

/**
 * Created by ls on 03.04.14.
 */
public class PartnerPointsContract implements BaseColumns {
    public static final String TABLE_NAME = "partner_points";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_PAYMENT_METHODS = "payment_methods";
    public static final String COLUMN_PHONES = "phones";
    public static final String COLUMN_WORKING_HOURS = "working_hours";
    public static final String COLUMN_PARTNER_ID = "partner_id";
    public static final String COLUMN_TEXT_SEARCH_IN = "text_search_in";
}