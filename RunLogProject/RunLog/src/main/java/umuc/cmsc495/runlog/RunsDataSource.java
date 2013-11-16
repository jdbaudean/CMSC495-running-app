package umuc.cmsc495.runlog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jd on 11/11/13.
 */
public class RunsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_DISTANCE, MySQLiteHelper.COLUMN_DATE, MySQLiteHelper.COLUMN_DURATION };

    public RunsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

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

    public void deleteRun(RunLogTable runSession) {
        long id = runSession.getId();
        System.out.println("Run deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_RUNLOG, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<RunLogTable> getAllRuns() {
        List<RunLogTable> runSessions = new ArrayList<RunLogTable>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_RUNLOG,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RunLogTable runSession = cursorToRunSession(cursor);
            runSessions.add(runSession);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return runSessions;
    }

    public double getLifetimeMileage() {
        Double total = 0.0;
        String selectQuery = "SELECT SUM(" + MySQLiteHelper.COLUMN_DISTANCE + ") FROM " +
                MySQLiteHelper.TABLE_RUNLOG;

        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        return total;
    }

    private RunLogTable cursorToRunSession(Cursor cursor) {
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //Date date = new Date(cursor.getLong(2));
        //String dateString = dateFormat.format(date);

        RunLogTable runSession = new RunLogTable();
        runSession.setId(cursor.getLong(0));
        runSession.setDistance(cursor.getFloat(1));
        runSession.setDate(cursor.getLong(2));
        runSession.setDuration(cursor.getLong(3));
        //comment.setDate(dateString);
        //comment.setDistance(dateString);
        return runSession;
    }
}
