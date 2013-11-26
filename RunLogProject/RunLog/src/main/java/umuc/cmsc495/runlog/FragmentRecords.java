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

        TextView textRecordsMonth = (TextView) root.findViewById(R.id.records_month_val);
        TextView textRecordsWeek = (TextView) root.findViewById(R.id.records_week_val);
        TextView textRecordsDay = (TextView) root.findViewById(R.id.records_day_val);
        TextView textRecordsRun = (TextView) root.findViewById(R.id.records_run_val);
        TextView textRecordsYear = (TextView) root.findViewById(R.id.records_year_val);

        textRecordsMonth.setText(Double.toString(dataSource.getMonthlyRecord()));
        textRecordsWeek.setText(Double.toString(dataSource.getWeeklyRecord()));
        textRecordsDay.setText(Double.toString(dataSource.getDailyRecord()));
        textRecordsRun.setText(Double.toString(dataSource.getRunRecord()));
        textRecordsYear.setText(Double.toString(dataSource.getYearlyRecord()));


        return root;
    }

        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataSource = new RunsDataSource(getActivity());
        dataSource.open();

    }
}