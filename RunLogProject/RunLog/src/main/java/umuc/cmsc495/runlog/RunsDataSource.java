/**
 * This class acts as the Data Access Object (DAO) for the RUNLOG Database.  It is used
 * for all access operations on the database.
 */


package umuc.cmsc495.runlog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RunsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_DATE,
            MySQLiteHelper.COLUMN_DISTANCE, MySQLiteHelper.COLUMN_DURATION };
    private Calendar calendar = Calendar.getInstance();
    private long today;

    public RunsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
        today = calendar.getTimeInMillis();
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /**
     *
     * @param distance
     * Distance of Run
     *
     * @param dateString
     * Date of Run
     * @param durationString
     * Duration of Run
     *
     * @return
     * Returns row of table
     */
    public RunLogTable createRun(double distance, String dateString, String durationString) {

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_DISTANCE, distance);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");


        SimpleDateFormat durationFormat = new SimpleDateFormat("HH:mm:ss");


        try {
            Date dateDate = dateFormat.parse(dateString);
            long dateLong = dateDate.getTime();

            Date durationDate = durationFormat.parse(durationString);
            long durationLong = durationDate.getTime();

            values.put(MySQLiteHelper.COLUMN_DATE, dateLong);
            values.put(MySQLiteHelper.COLUMN_DURATION, durationLong);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        long insertId = database.insert(MySQLiteHelper.TABLE_RUNLOG, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_RUNLOG,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        RunLogTable newRunLogTable = cursorToRunSession(cursor);
        cursor.close();
        return newRunLogTable;
    }

    /**
     * Deletes a row from the table
     *
     * @param id
     * Row ID to be deleted
     */
    public void deleteRun(long id) {
        database.delete(MySQLiteHelper.TABLE_RUNLOG, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    /**
     * Get all runs from the runlog table in Descending order by Date
     *
     * @return
     * List of rows from the runlog table
     */
    public List<RunLogTable> getAllRuns() {
        List<RunLogTable> runSessions = new ArrayList<RunLogTable>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_RUNLOG,
                allColumns, null, null, null, null, MySQLiteHelper.COLUMN_DATE + " DESC");


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RunLogTable runSession = cursorToRunSession(cursor);
            runSessions.add(runSession);
            cursor.moveToNext();
        }
        cursor.close();
        return runSessions;

    }

    /**
     * Calculates all run mileage from the runlog database
     *
     * @return
     * Return a String from a double formatted with a precision of 2 decimal places.
     */
    public String getLifetimeMileage() {
        Double total = 0.0;
        String selectQuery = "SELECT SUM(" + MySQLiteHelper.COLUMN_DISTANCE + ") FROM " +
                MySQLiteHelper.TABLE_RUNLOG;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        return String.format("%.2f", total);
    }

    /**
     * Calculates all mileage from the current year
     *
     * @return
     * Return a String from a double formatted with a precision of 2 decimal places
     */
    public String getYearlyMileage() {
        //long today;
        long firstDayOfYear;
        Double total = 0.0;

        //today = calendar.getTimeInMillis();
        calendar.set(Calendar.getInstance().get(Calendar.YEAR), 0, 1 );
        firstDayOfYear = calendar.getTimeInMillis();

        String selectQuery = "SELECT SUM(" + MySQLiteHelper.COLUMN_DISTANCE + ") FROM " +
            MySQLiteHelper.TABLE_RUNLOG  + " where " + MySQLiteHelper.COLUMN_DATE +
            " between " + firstDayOfYear + " and " + this.today + ";";

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        return String.format("%.2f", total);
    }

    /**
     * Calculates all mileage from the current month
     *
     * @return
     * Return a String from a double formatted with a precision of 2 decimal places
     */
    public String getMonthlyMileage() {

        long firstDayOfMonth;
        Double total = 0.0;
        Calendar cal = Calendar.getInstance();

        //Clear calendar and get first day of week
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        firstDayOfMonth = cal.getTimeInMillis();

        String selectQuery = "SELECT SUM(" + MySQLiteHelper.COLUMN_DISTANCE + ") FROM " +
            MySQLiteHelper.TABLE_RUNLOG  + " where " + MySQLiteHelper.COLUMN_DATE +
            " between " + firstDayOfMonth + " and " + this.today + ";";

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        return String.format("%.2f", total);
    }

    /**
     * Calculate all mileage form the current week
     *
     * @return
     * Return a String from a double formatted with a precision of 2 decimal places
     */
    public String getWeeklyMileage() {

        long firstDayOfWeek;
        Double total = 0.0;
        Calendar cal = Calendar.getInstance();

        //Clear calendar and get first day of week
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 1);

        firstDayOfWeek = cal.getTimeInMillis();

        String selectQuery = "SELECT SUM(" + MySQLiteHelper.COLUMN_DISTANCE + ") FROM " +
            MySQLiteHelper.TABLE_RUNLOG  + " where " + MySQLiteHelper.COLUMN_DATE +
            " between " + firstDayOfWeek + " and " + this.today + ";";

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        return String.format("%.2f", total);
    }

    /**
     * Calculate all mileage from the current day
     *
     * @return
     * Return a String from a double formatted with a precision of 2 decimal places
     */
    public String getTodayMileage() {

        long startOfDay;
        Double total = 0.0;
        Calendar cal = Calendar.getInstance();

        //Clear calendar and get first day of week
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.HOUR_OF_DAY, 0);

        startOfDay = cal.getTimeInMillis();

        String selectQuery = "SELECT SUM(" + MySQLiteHelper.COLUMN_DISTANCE + ") FROM " +
            MySQLiteHelper.TABLE_RUNLOG  + " where " + MySQLiteHelper.COLUMN_DATE +
            " between " + startOfDay + " and " + this.today + ";";

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        return String.format("%.2f", total);
    }

    /**
     * Calculate the most mileage in a single month
     *
     * @return
     * Return a String from a double formatted with a precision of 2 decimal places
     */
    public String getMonthlyRecord() {
        Double total = 0.0;
        String selectQuery = "SELECT SUM(" + MySQLiteHelper.COLUMN_DISTANCE + ") FROM " +
                MySQLiteHelper.TABLE_RUNLOG + " GROUP BY strftime('%m-%Y', " + MySQLiteHelper.COLUMN_DATE +
                "/1000, 'unixepoch') ORDER BY SUM(" + MySQLiteHelper.COLUMN_DISTANCE + ") DESC limit 1;";

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        return String.format("%.2f", total);
    }

    /**
     * Calculate the most mileage in a single week
     *
     * @return
     * Return a String from a double formatted with a precision of 2 decimal places
     */
    public String getWeeklyRecord() {
        Double total = 0.0;
        String selectQuery = "SELECT SUM(" + MySQLiteHelper.COLUMN_DISTANCE + ") FROM " +
                MySQLiteHelper.TABLE_RUNLOG + " GROUP BY strftime('%W-%m-%Y', " + MySQLiteHelper.COLUMN_DATE +
                "/1000, 'unixepoch') ORDER BY SUM(" + MySQLiteHelper.COLUMN_DISTANCE + ") DESC limit 1;";

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        return String.format("%.2f", total);
    }

    /**
     * Calculate the most mileage in a single year
     *
     * @return
     * Return a String from a double formatted with a precision of 2 decimal places
     */
    public String getYearlyRecord() {
        Double total = 0.0;
        String selectQuery = "SELECT SUM(" + MySQLiteHelper.COLUMN_DISTANCE + ") FROM " +
                MySQLiteHelper.TABLE_RUNLOG + " GROUP BY strftime('%Y', " + MySQLiteHelper.COLUMN_DATE +
                "/1000, 'unixepoch') ORDER BY SUM(" + MySQLiteHelper.COLUMN_DISTANCE + ") DESC limit 1;";

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        return String.format("%.2f", total);
    }

    /**
     * Calculate the most mileage in a single day
     *
     * @return
     * Return a String from a double formatted with a precision of 2 decimal places
     */
    public String getDailyRecord() {
        Double total = 0.0;
        String selectQuery = "SELECT SUM(" + MySQLiteHelper.COLUMN_DISTANCE + ") FROM " +
                MySQLiteHelper.TABLE_RUNLOG + " GROUP BY strftime('%j-%Y', " + MySQLiteHelper.COLUMN_DATE +
                "/1000, 'unixepoch') ORDER BY SUM(" + MySQLiteHelper.COLUMN_DISTANCE + ") DESC limit 1;";

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        return String.format("%.2f", total);
    }

    /**
     * Calculate the most mileage in a single run
     *
     * @return
     * Return a String from a double formatted with a precision of 2 decimal places
     */
    public String getRunRecord() {
        Double total = 0.0;
        String selectQuery = "SELECT " + MySQLiteHelper.COLUMN_DISTANCE + " FROM " +
                MySQLiteHelper.TABLE_RUNLOG + " ORDER BY " +
                MySQLiteHelper.COLUMN_DISTANCE + " DESC limit 1;";

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        return String.format("%.2f", total);
    }

    /**
     * Calculate total mileage from the previous week and return 110% of that value
     * to create a goal for the current week
     *
     * @return
     * Return a String from a double formatted with a precision of 2 decimal places
     */
    public String getWeeklyGoal() {

        long firstDayOfLastWeek;
        long firstDayOfThisWeek;
        Double increase = 1.10;
        Double total = 0.0;
        Calendar cal = Calendar.getInstance();

        //Clear calendar and get first day of week
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());


        firstDayOfThisWeek = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        cal.add(Calendar.DAY_OF_WEEK, 1);
        firstDayOfLastWeek = cal.getTimeInMillis();

        String selectQuery = "SELECT SUM(" + MySQLiteHelper.COLUMN_DISTANCE + ") FROM " +
            MySQLiteHelper.TABLE_RUNLOG  + " where " + MySQLiteHelper.COLUMN_DATE +
            " between " + firstDayOfLastWeek + " and " + firstDayOfThisWeek + ";";

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        total = (total * increase);
        return String.format("%.2f", total);
    }

    /**
     * Calculate total mileage from the previous month and return 110% of that value
     * to create a goal for the current month
     *
     * @return
     * Return a String from a double formatted with a precision of 2 decimal places
     */
    public String getMonthlyGoal() {

        long firstDayOfLastMonth;
        long lastDayOfLastMonth;
        Double increase = 1.10;
        Double total = 0.0;
        Calendar cal = Calendar.getInstance();

        //Clear calendar and get first day of Month
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.set(Calendar.DAY_OF_MONTH, -1);

        lastDayOfLastMonth = cal.getTimeInMillis();

        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        firstDayOfLastMonth = cal.getTimeInMillis();

        String selectQuery = "SELECT SUM(" + MySQLiteHelper.COLUMN_DISTANCE + ") FROM " +
            MySQLiteHelper.TABLE_RUNLOG  + " where " + MySQLiteHelper.COLUMN_DATE +
            " between " + firstDayOfLastMonth + " and " + lastDayOfLastMonth + ";";

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }

        total = (total * increase);
        return String.format("%.2f", total);
    }

    /**
     *
     * @param cursor
     * Cursor that accesses the result set returned by a database query
     *
     * @return
     * Returns a row from the runlog table
     */
    private RunLogTable cursorToRunSession(Cursor cursor) {

        RunLogTable runSession = new RunLogTable();
        runSession.setId(cursor.getLong(0));
        runSession.setDate(cursor.getLong(1));
        runSession.setDistance(cursor.getFloat(2));
        runSession.setDuration(cursor.getLong(3));

        return runSession;
    }
}
