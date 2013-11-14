package umuc.cmsc495.runlog;
 
import android.content.Context;
//import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class FragmentEnterRun extends Fragment {
    private RunsDataSource dataSource;
    private double distance;
 
    public static Fragment newInstance(Context context) {
        FragmentEnterRun f = new FragmentEnterRun();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_enterrun, null);

        Button mButton = (Button) root.findViewById(R.id.enterRun_button_submit);
        final EditText editTextRunDistance = (EditText) root.findViewById(R.id.enterRun_distance_val);
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    distance = Double.parseDouble(editTextRunDistance.getText().toString());
                } catch (final NullPointerException e) {
                    distance = 1;
                }

                dataSource.createRun(distance);
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