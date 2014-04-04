package ru.droogcompanii.application.data.db_util.contracts.working_hours;

import android.provider.BaseColumns;

/**
 * Created by ls on 03.04.14.
 */
public class HolidayContract implements BaseColumns {
    public static final String TABLE_NAME = "holiday_working_hours";

    public static final String COLUMN_POINT_ID = "point_id";
    public static final String COLUMN_DAY_OF_WEEK = "day_of_week";
    public static final String COLUMN_MESSAGE = "message";
}
