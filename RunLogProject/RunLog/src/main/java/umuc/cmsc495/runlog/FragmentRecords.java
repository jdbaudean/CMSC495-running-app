/**
 *  Fragment that displays Yearly, Monthly, Weekly, Daily, and single run record.
 *  The records are reported by sql queries defined in the RunsDataSource class.
 */

package umuc.cmsc495.runlog;
 
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentRecords extends Fragment {

    private RunsDataSource dataSource;

    public static Fragment newInstance(Context context) {
        FragmentRecords f = new FragmentRecords();
 
        return f;
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_records, null);

        // Create TextViews for each text box
        TextView textRecordsMonth = (TextView) root.findViewById(R.id.records_month_val);
        TextView textRecordsWeek = (TextView) root.findViewById(R.id.records_week_val);
        TextView textRecordsDay = (TextView) root.findViewById(R.id.records_day_val);
        TextView textRecordsRun = (TextView) root.findViewById(R.id.records_run_val);
        TextView textRecordsYear = (TextView) root.findViewById(R.id.records_year_val);

        // Populate each text box with the results from the given database query
        textRecordsMonth.setText(dataSource.getMonthlyRecord());
        textRecordsWeek.setText(dataSource.getWeeklyRecord());
        textRecordsDay.setText(dataSource.getDailyRecord());
        textRecordsRun.setText(dataSource.getRunRecord());
        textRecordsYear.setText(dataSource.getYearlyRecord());


        return root;
    }

        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a DAO
        dataSource = new RunsDataSource(getActivity());
        dataSource.open();

    }
}