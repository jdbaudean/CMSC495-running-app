package umuc.cmsc495.runlog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import android.content.DialogInterface;

public class FragmentEnterRun extends Fragment {
    private RunsDataSource dataSource;
    private double distance = 0;
    private String date;
    private String duration;
 
    public static Fragment newInstance(Context context) {
        FragmentEnterRun f = new FragmentEnterRun();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_enterrun, null);

        assert root != null;
        Button mButton = (Button) root.findViewById(R.id.enterRun_button_submit);
        final EditText editTextRunDistance = (EditText) root.findViewById(R.id.enterRun_distance_val);
        final EditText editTextRunDate = (EditText) root.findViewById(R.id.enterRun_date_val);
        final EditText editTextRunDuration = (EditText) root.findViewById(R.id.enterRun_duration_val);

        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    distance = Double.parseDouble(editTextRunDistance.getText().toString());
                    date = editTextRunDate.getText().toString();
                    duration = editTextRunDuration.getText().toString();
                } catch (final NullPointerException e) {
                    distance = 0;
                    date = "0";
                    duration = "0";
                }

                dataSource.createRun(distance, date, duration);
                Toast.makeText(getActivity(), "Run Entered", Toast.LENGTH_SHORT).show();
                editTextRunDate.setText("");
                editTextRunDistance.setText("");
                editTextRunDuration.setText("");
            }
        });

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataSource = new RunsDataSource(getActivity());
        dataSource.open();
    }

}