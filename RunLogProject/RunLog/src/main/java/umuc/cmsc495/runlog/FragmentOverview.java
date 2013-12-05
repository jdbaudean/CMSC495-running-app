package umuc.cmsc495.runlog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FragmentOverview extends Fragment {

    private RunsDataSource dataSource;

    public static Fragment newInstance(Context context) {
        FragmentOverview f = new FragmentOverview();
 
        return f;
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_overview, null);

        TextView text = (TextView) root.findViewById(R.id.overview_lifetime_val);
        TextView textYearly = (TextView) root.findViewById(R.id.overview_year_val);
        TextView textWeekly = (TextView) root.findViewById(R.id.overview_week_val);
        TextView textMonthly = (TextView) root.findViewById(R.id.overview_month_val);
        TextView textToday = (TextView) root.findViewById(R.id.overview_today_val);

        text.setText(dataSource.getLifetimeMileage());
        textYearly.setText(dataSource.getYearlyMileage());
        textWeekly.setText(dataSource.getWeeklyMileage());
        textMonthly.setText(dataSource.getMonthlyMileage());
        textToday.setText(dataSource.getTodayMileage());



        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataSource = new RunsDataSource(getActivity());
        dataSource.open();

    }
 
}