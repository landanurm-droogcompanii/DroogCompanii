package ru.droogcompanii.application.data.db_util.contracts;

import android.provider.BaseColumns;

/**
 * Created by ls on 11.02.14.
 */
public class OffersContract implements BaseColumns {
    public static final String TABLE_NAME = "offers";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PARTNER_ID = "partner_id";
    public static final String COLUMN_FROM = "from_date";
    public static final String COLUMN_TO = "to_date";
    public static final String COLUMN_SHORT_DESCRIPTION = "short_description";
    public static final String COLUMN_FULL_DESCRIPTION = "full_description";
    public static final String COLUMN_IMAGE_URL = "image_url";
}
