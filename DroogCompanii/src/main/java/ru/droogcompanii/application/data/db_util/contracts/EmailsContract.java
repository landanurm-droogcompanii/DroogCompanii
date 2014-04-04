package ru.droogcompanii.application.data.db_util.contracts;

import android.provider.BaseColumns;

/**
 * Created by ls on 03.04.14.
 */
public class EmailsContract implements BaseColumns {
    public static final String TABLE_NAME = "emails";

    public static final String COLUMN_OWNER_ID = "owner_id";
    public static final String COLUMN_ADDRESS = "address";
}
