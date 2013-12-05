package umuc.cmsc495.runlog;
 
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FragmentHistory extends ListFragment {
    private RunsDataSource dataSource;
    ArrayAdapter<RunLogTable> adapter;
    ListView listView;
    Set<Integer> checkedIds;
    List<RunLogTable> runLogTables;
    List<CheckedTextView> checkedTextViews;

    public static Fragment newInstance(Context context) {
        FragmentHistory f = new FragmentHistory();
 
        return f;
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_history, null);
        List<RunLogTable> values = dataSource.getAllRuns();

        adapter = new ArrayAdapter<RunLogTable>(getActivity(), android.R.layout.simple_list_item_checked, values);
        checkedIds = new HashSet<Integer>();
        runLogTables = new ArrayList<RunLogTable>();
        checkedTextViews = new ArrayList<CheckedTextView>();

        listView = new ListView(getActivity());
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        setListAdapter(adapter);


        Button mButton = (Button) root.findViewById(R.id.deleteRun);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Remove from the database
                for (Integer ids : checkedIds) {
                    dataSource.deleteRun(adapter.getItem(ids).getId());
                }

                // Remove from the adapter
                for (RunLogTable runLogTable : runLogTables) {
                    adapter.remove(runLogTable);
                }

                for (CheckedTextView checkedTextView : checkedTextViews) {
                    if (checkedTextView.isChecked())
                        checkedTextView.toggle();
                }

                adapter.notifyDataSetChanged();


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

    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {
        super.onListItemClick(l, view, position, id);
        CheckedTextView textView = (CheckedTextView) view;
        textView.getId();

        if (!textView.isChecked()) {
            checkedIds.add(position);
            runLogTables.add(adapter.getItem(position));
            checkedTextViews.add(textView);
        }

        if (textView.isChecked()) {
            checkedIds.remove(position);
            runLogTables.remove(adapter.getItem(position));
            checkedTextViews.remove(textView);
        }
        textView.setChecked(!textView.isChecked());

     }
}