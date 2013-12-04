package umuc.cmsc495.runlog;

import java.lang.Override;import java.lang.String;import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jd on 11/11/13.
 */
public class RunLogTable {

    private long id;
    private double distance;
    private long date;
    private long duration;


    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }


    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        Date date = new Date(this.date);

        SimpleDateFormat durationFormat = new SimpleDateFormat("HH:mm:ss");
        Date durationDate = new Date(this.duration);
        return dateFormat.format(date) + "     " + String.format("%.2f", distance) + "     " + durationFormat.format(durationDate);
    }
}
