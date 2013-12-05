package umuc.cmsc495.runlog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;


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

        editTextRunDistance.setText("");
        editTextRunDate.setText("");
        editTextRunDuration.setText("");

        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isDateValid(editTextRunDate.getText().toString())) {
                    editTextRunDate.setError("Date Format is MM-DD-YYYY");
                    Log.d("My Tag", "Date Error");
                } else if (editTextRunDistance.getText().toString().length() == 0) {
                    editTextRunDistance.setError("Please Enter Run Distance");
                    Log.d("My Tag", "Run Distance error");
                } else if (!isDurationValid(editTextRunDuration.getText().toString())) {
                    editTextRunDuration.setError("Duration format is HH:MM:SS");
                    Log.d("My Tag", "Duration error");
                } else {

                    distance = Double.parseDouble(editTextRunDistance.getText().toString());

                    date = editTextRunDate.getText().toString();
                    duration = editTextRunDuration.getText().toString();

                    dataSource.createRun(distance, date, duration);
                    Toast.makeText(getActivity(), "Run Entered", Toast.LENGTH_SHORT).show();
                    editTextRunDate.setText("");
                    editTextRunDistance.setText("");
                    editTextRunDuration.setText("");
                }
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

    public static boolean isDateValid(String text) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        dateFormat.setLenient(false);

        if (text == null || !text.matches("\\d{2}-\\d{2}-\\d{4}"))
            return false;

        try {
            dateFormat.parse(text);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }

    public static boolean isDurationValid(String text) {
        return (text == null || !text.matches("\\d{2}:\\d{2}:\\d{2}"));
    }

}