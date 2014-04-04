package ru.droogcompanii.application.data.db_util.contracts.working_hours;

import android.provider.BaseColumns;

/**
 * Created by ls on 03.04.14.
 */
public class TimeRangesContract implements BaseColumns {

    public static final String COLUMN_WORKING_HOURS_ID = "working_hours_id";
    public static final String COLUMN_FROM = "from_time";
    public static final String COLUMN_TO = "to_time";
}
