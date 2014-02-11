package ru.droogcompanii.application.data.db_util.offers;

import android.provider.BaseColumns;

/**
 * Created by ls on 11.02.14.
 */
public class OffersContract implements BaseColumns {
    public static final String TABLE_NAME = "offers";

    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_PARTNER_ID = "partner_id";
    public static final String COLUMN_NAME_FROM = "from_date";
    public static final String COLUMN_NAME_TO = "to_date";
    public static final String COLUMN_NAME_SHORT_DESCRIPTION = "short_description";
    public static final String COLUMN_NAME_FULL_DESCRIPTION = "full_description";
    public static final String COLUMN_NAME_IMAGE_URL = "image_url";
}
