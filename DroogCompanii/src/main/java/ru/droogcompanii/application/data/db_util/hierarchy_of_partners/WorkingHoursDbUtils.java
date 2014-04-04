package ru.droogcompanii.application.data.db_util.hierarchy_of_partners;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import ru.droogcompanii.application.data.db_util.contracts.working_hours.BusinessDayContract;
import ru.droogcompanii.application.data.db_util.contracts.working_hours.DayAndNightContract;
import ru.droogcompanii.application.data.db_util.contracts.working_hours.ExcludedTimeRangesContract;
import ru.droogcompanii.application.data.db_util.contracts.working_hours.HolidayContract;
import ru.droogcompanii.application.data.db_util.contracts.working_hours.IncludedTimeRangesContract;
import ru.droogcompanii.application.data.db_util.contracts.working_hours.TimeRangesContract;
import ru.droogcompanii.application.data.time.TimeOfDay;
import ru.droogcompanii.application.data.time.TimeRange;
import ru.droogcompanii.application.data.working_hours.DateTimeConstants;
import ru.droogcompanii.application.data.working_hours.WeekWorkingHours;
import ru.droogcompanii.application.data.working_hours.WorkingHours;
import ru.droogcompanii.application.data.working_hours.WorkingHoursForEachDayOfWeek;
import ru.droogcompanii.application.data.working_hours.working_hours_impl.DayAndNightWorkingHours;
import ru.droogcompanii.application.data.working_hours.working_hours_impl.WorkingHoursOnBusinessDay;
import ru.droogcompanii.application.data.working_hours.working_hours_impl.WorkingHoursOnHoliday;

/**
 * Created by ls on 03.04.14.
 */
public class WorkingHoursDbUtils {

    private final SQLiteDatabase db;

    public static WeekWorkingHours read(SQLiteDatabase db, int partnerPointId) {
        return new WorkingHoursDbUtils(db).read(partnerPointId);
    }

    public static void write(SQLiteDatabase db, int partnerPointId, WeekWorkingHours weekWorkingHours) {
        new WorkingHoursDbUtils(db).write(partnerPointId, weekWorkingHours);
    }

    public WorkingHoursDbUtils(SQLiteDatabase db) {
        this.db = db;
    }

    public void write(int partnerPointId, WeekWorkingHours weekWorkingHours) {
        removeWeekWorkingHours(partnerPointId);
        for (int dayOfWeek : DateTimeConstants.getDaysOfWeek()) {
            WorkingHours workingHours = weekWorkingHours.getWorkingHoursOf(dayOfWeek);
            insertWorkingHours(partnerPointId, dayOfWeek, workingHours);
        }
    }

    private void insertWorkingHours(int partnerPointId, int dayOfWeek, WorkingHours workingHours) {
        if (workingHours instanceof WorkingHoursOnBusinessDay) {
            insertWorkingHoursOnBusinessDay(partnerPointId, dayOfWeek, (WorkingHoursOnBusinessDay) workingHours);
        } else if (workingHours instanceof WorkingHoursOnHoliday) {
            insertWorkingHoursOnHoliday(partnerPointId, dayOfWeek, (WorkingHoursOnHoliday) workingHours);
        } else if (workingHours instanceof DayAndNightWorkingHours) {
            insertDayAndNightWorkingHours(partnerPointId, dayOfWeek, (DayAndNightWorkingHours) workingHours);
        } else {
            throw new IllegalArgumentException("Unknown working hours type: " + workingHours.getClass().getName());
        }
    }

    private void insertWorkingHoursOnBusinessDay(int partnerPointId, int dayOfWeek, WorkingHoursOnBusinessDay workingHours) {
        String sql = "INSERT INTO " + BusinessDayContract.TABLE_NAME + " (" +
                BusinessDayContract.COLUMN_POINT_ID + ", " +
                BusinessDayContract.COLUMN_DAY_OF_WEEK +
                ") VALUES(?,?)";
        SQLiteStatement insertStatement = db.compileStatement(sql);
        insertStatement.clearBindings();
        insertStatement.bindLong(1, partnerPointId);
        insertStatement.bindLong(2, dayOfWeek);
        long workingHoursId = insertStatement.executeInsert();

        insertIncludedTimeRanges(workingHoursId, workingHours);
        insertExcludedTimeRanges(workingHoursId, workingHours);
    }

    private void insertIncludedTimeRanges(long workingHoursId, WorkingHoursOnBusinessDay workingHours) {
        for (TimeRange each : workingHours.getIncludedRanges()) {
            insertTimeRange(IncludedTimeRangesContract.TABLE_NAME, workingHoursId, each);
        }
    }

    private void insertExcludedTimeRanges(long workingHoursId, WorkingHoursOnBusinessDay workingHours) {
        for (TimeRange each : workingHours.getExcludedRanges()) {
            insertTimeRange(ExcludedTimeRangesContract.TABLE_NAME, workingHoursId, each);
        }
    }

    private void insertTimeRange(String tableName, long workingHoursId, TimeRange timeRange) {
        String sql = "INSERT INTO " + tableName + " (" +
                TimeRangesContract.COLUMN_WORKING_HOURS_ID + ", " +
                TimeRangesContract.COLUMN_FROM + ", " +
                TimeRangesContract.COLUMN_TO +
                ") VALUES(?,?,?)";
        SQLiteStatement insertStatement = db.compileStatement(sql);
        insertStatement.clearBindings();
        insertStatement.bindLong(1, workingHoursId);
        insertStatement.bindLong(2, timeRange.from().toMinutesOfDay());
        insertStatement.bindLong(3, timeRange.to().toMinutesOfDay());
        insertStatement.executeInsert();
    }

    private void insertWorkingHoursOnHoliday(int partnerPointId, int dayOfWeek, WorkingHoursOnHoliday workingHours) {
        String sql = "INSERT INTO " + HolidayContract.TABLE_NAME + " (" +
                HolidayContract.COLUMN_POINT_ID + ", " +
                HolidayContract.COLUMN_DAY_OF_WEEK + ", " +
                HolidayContract.COLUMN_MESSAGE +
                ") VALUES(?,?,?)";
        SQLiteStatement insertStatement = db.compileStatement(sql);
        insertStatement.clearBindings();
        insertStatement.bindLong(1, partnerPointId);
        insertStatement.bindLong(2, dayOfWeek);
        insertStatement.bindString(3, workingHours.toString());
        insertStatement.executeInsert();
    }

    private void insertDayAndNightWorkingHours(int partnerPointId, int dayOfWeek, DayAndNightWorkingHours workingHours) {
        String sql = "INSERT INTO " + DayAndNightContract.TABLE_NAME + " (" +
                DayAndNightContract.COLUMN_POINT_ID + ", " +
                DayAndNightContract.COLUMN_DAY_OF_WEEK + ", " +
                DayAndNightContract.COLUMN_MESSAGE +
                ") VALUES(?,?,?)";
        SQLiteStatement insertStatement = db.compileStatement(sql);
        insertStatement.clearBindings();
        insertStatement.bindLong(1, partnerPointId);
        insertStatement.bindLong(2, dayOfWeek);
        insertStatement.bindString(3, workingHours.toString());
        insertStatement.executeInsert();
    }

    public void removeWeekWorkingHours(int partnerPointId) {
        removeDayAndNightWorkingHours(partnerPointId);
        removeHolidayWorkingHours(partnerPointId);
        removeWorkingHoursOnBusinessDay(partnerPointId);
    }

    private void removeDayAndNightWorkingHours(int partnerPointId) {
        String where = DayAndNightContract.COLUMN_POINT_ID + " = ?";
        String[] args = { String.valueOf(partnerPointId) };
        db.delete(DayAndNightContract.TABLE_NAME, where, args);
    }

    private void removeHolidayWorkingHours(int partnerPointId) {
        String where = HolidayContract.COLUMN_POINT_ID + " = ?";
        String[] args = { String.valueOf(partnerPointId) };
        db.delete(HolidayContract.TABLE_NAME, where, args);
    }

    private void removeWorkingHoursOnBusinessDay(int partnerPointId) {
        removeTimeRanges(IncludedTimeRangesContract.TABLE_NAME, partnerPointId);
        removeTimeRanges(ExcludedTimeRangesContract.TABLE_NAME, partnerPointId);

        String where = BusinessDayContract.COLUMN_POINT_ID + " = ?";
        String[] args = { String.valueOf(partnerPointId) };
        db.delete(BusinessDayContract.TABLE_NAME, where, args);
    }

    private void removeTimeRanges(String tableName, int partnerPointId) {
        db.execSQL(
                "DELETE FROM " + tableName + " WHERE " + TimeRangesContract.COLUMN_WORKING_HOURS_ID + " IN ( " +
                    "SELECT " + BusinessDayContract._ID + " FROM " + BusinessDayContract.TABLE_NAME +
                        " WHERE " + BusinessDayContract.COLUMN_POINT_ID + " = " + partnerPointId +
                " )"
        );
    }

    public WeekWorkingHours read(int partnerPointId) {
        WorkingHoursForEachDayOfWeek workingHoursForEachDayOfWeek = new WorkingHoursForEachDayOfWeek();

        readDayAndNightWorkingHours(partnerPointId, workingHoursForEachDayOfWeek);
        readHolidayWorkingHours(partnerPointId, workingHoursForEachDayOfWeek);
        readWorkingHoursOnBusinessDay(partnerPointId, workingHoursForEachDayOfWeek);

        return new WeekWorkingHours(workingHoursForEachDayOfWeek);
    }

    private void readWorkingHoursOnBusinessDay(int partnerPointId,
                                   WorkingHoursForEachDayOfWeek workingHoursForEachDayOfWeek) {

        String sql = "SELECT " + BusinessDayContract._ID + ", " + BusinessDayContract.COLUMN_DAY_OF_WEEK +
                " FROM " + BusinessDayContract.TABLE_NAME +
                " WHERE " + BusinessDayContract.COLUMN_POINT_ID + " = " + partnerPointId + ";";

        Cursor cursor = db.rawQuery(sql, null);

        int indexId = cursor.getColumnIndexOrThrow(BusinessDayContract._ID);
        int indexDayOfWeek = cursor.getColumnIndexOrThrow(BusinessDayContract.COLUMN_DAY_OF_WEEK);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            long workingHoursId = cursor.getLong(indexId);
            int dayOfWeek = cursor.getInt(indexDayOfWeek);

            readWorkingHoursOnBusinessDayByWorkingHoursId(
                    workingHoursId, dayOfWeek, workingHoursForEachDayOfWeek);

            cursor.moveToNext();
        }

        cursor.close();
    }


    interface TimeRangeIncluderExcluder {
        void includeOrExclude(WorkingHoursOnBusinessDay workingHoursOnBusinessDay, TimeRange timeRange);
    }

    private void readWorkingHoursOnBusinessDayByWorkingHoursId(
            long workingHoursId, int dayOfWeek, WorkingHoursForEachDayOfWeek workingHoursForEachDayOfWeek) {

        readTimeRanges(
                IncludedTimeRangesContract.TABLE_NAME,
                workingHoursId,
                dayOfWeek,
                workingHoursForEachDayOfWeek,
                new TimeRangeIncluderExcluder() {
                    @Override
                    public void includeOrExclude(WorkingHoursOnBusinessDay workingHoursOnBusinessDay, TimeRange timeRange) {
                        workingHoursOnBusinessDay.include(timeRange);
                    }
                }
        );
        readTimeRanges(
                ExcludedTimeRangesContract.TABLE_NAME,
                workingHoursId,
                dayOfWeek,
                workingHoursForEachDayOfWeek,
                new TimeRangeIncluderExcluder() {
                    @Override
                    public void includeOrExclude(WorkingHoursOnBusinessDay workingHoursOnBusinessDay, TimeRange timeRange) {
                        workingHoursOnBusinessDay.exclude(timeRange);
                    }
                }
        );
    }

    private void readTimeRanges(String tableName, long workingHoursId, int dayOfWeek,
                                WorkingHoursForEachDayOfWeek workingHoursForEachDayOfWeek,
                                TimeRangeIncluderExcluder timeRangeIncluderExcluder) {

        String sql = "SELECT " + TimeRangesContract.COLUMN_FROM + ", " + TimeRangesContract.COLUMN_TO +
                " FROM " + tableName +
                " WHERE " + TimeRangesContract.COLUMN_WORKING_HOURS_ID + " = " + workingHoursId + ";";

        Cursor cursor = db.rawQuery(sql, null);

        int indexFrom = cursor.getColumnIndexOrThrow(TimeRangesContract.COLUMN_FROM);
        int indexTo = cursor.getColumnIndexOrThrow(TimeRangesContract.COLUMN_TO);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            int from = cursor.getInt(indexFrom);
            int to = cursor.getInt(indexTo);

            WorkingHoursOnBusinessDay workingHours =
                    (WorkingHoursOnBusinessDay) workingHoursForEachDayOfWeek.get(dayOfWeek);

            if (workingHours == null) {
                workingHours = new WorkingHoursOnBusinessDay();
                workingHoursForEachDayOfWeek.set(dayOfWeek, workingHours);
            }

            TimeRange timeRange =
                    WorkingHoursOnBusinessDay.createTimeRange(
                                                TimeOfDay.fromMinutesOfDay(from),
                                                TimeOfDay.fromMinutesOfDay(to)
            );

            timeRangeIncluderExcluder.includeOrExclude(workingHours, timeRange);

            cursor.moveToNext();
        }

        cursor.close();
    }

    private void readHolidayWorkingHours(int partnerPointId,
                                   WorkingHoursForEachDayOfWeek workingHoursForEachDayOfWeek) {

        String sql = "SELECT " + HolidayContract.COLUMN_DAY_OF_WEEK + ", " + HolidayContract.COLUMN_MESSAGE +
                     " FROM " + HolidayContract.TABLE_NAME +
                     " WHERE " + HolidayContract.COLUMN_POINT_ID + " = " + partnerPointId + ";";

        Cursor cursor = db.rawQuery(sql, null);

        int indexDayOfWeek = cursor.getColumnIndexOrThrow(HolidayContract.COLUMN_DAY_OF_WEEK);
        int indexMessage = cursor.getColumnIndexOrThrow(HolidayContract.COLUMN_MESSAGE);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            int dayOfWeek = cursor.getInt(indexDayOfWeek);
            String message = cursor.getString(indexMessage);
            WorkingHours workingHours = new WorkingHoursOnHoliday(message);

            workingHoursForEachDayOfWeek.set(dayOfWeek, workingHours);

            cursor.moveToNext();
        }

        cursor.close();
    }

    private void readDayAndNightWorkingHours(int partnerPointId,
                                   WorkingHoursForEachDayOfWeek workingHoursForEachDayOfWeek) {

        String sql = "SELECT " + DayAndNightContract.COLUMN_DAY_OF_WEEK + ", " + DayAndNightContract.COLUMN_MESSAGE +
                " FROM " + DayAndNightContract.TABLE_NAME +
                " WHERE " + DayAndNightContract.COLUMN_POINT_ID + " = " + partnerPointId + ";";

        Cursor cursor = db.rawQuery(sql, null);

        int indexDayOfWeek = cursor.getColumnIndexOrThrow(DayAndNightContract.COLUMN_DAY_OF_WEEK);
        int indexMessage = cursor.getColumnIndexOrThrow(DayAndNightContract.COLUMN_MESSAGE);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            int dayOfWeek = cursor.getInt(indexDayOfWeek);
            String message = cursor.getString(indexMessage);
            WorkingHours workingHours = new DayAndNightWorkingHours(message);

            workingHoursForEachDayOfWeek.set(dayOfWeek, workingHours);

            cursor.moveToNext();
        }

        cursor.close();
    }

}
