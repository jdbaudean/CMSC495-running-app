package umuc.cmsc495.runlog;
 
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentGoals extends Fragment {
    private RunsDataSource dataSource;

    public static Fragment newInstance(Context context) {
        FragmentGoals f = new FragmentGoals();
 
        return f;
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_goals, null);

        TextView textGoalsWeekly = (TextView) root.findViewById(R.id.goals_weekly_val);
        TextView textGoalsMonthly = (TextView) root.findViewById(R.id.goals_monthly_val);

        textGoalsWeekly.setText(dataSource.getWeeklyGoal());
        textGoalsMonthly.setText(dataSource.getMonthlyGoal());

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataSource = new RunsDataSource(getActivity());
        dataSource.open();

    }
 
}