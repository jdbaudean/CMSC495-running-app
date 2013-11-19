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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.List;

public class FragmentHistory extends ListFragment {
    private RunsDataSource dataSource;
    SimpleCursorAdapter adapter;
    ListView listView;
    long listId;

    public static Fragment newInstance(Context context) {
        FragmentHistory f = new FragmentHistory();
 
        return f;
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_history, null);

        Cursor cursor = dataSource.getAllRuns();
        //List<RunLogTable> values = dataSource.getAllRuns();

        //ArrayAdapter<RunLogTable> adapter = new ArrayAdapter<RunLogTable>(getActivity(), android.R.layout.simple_list_item_checked, values);
        adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_checked, cursor,
                new String[] { "distance" },
                new int[] {android.R.id.text1}, 0);

        //listView = new ListView(getActivity());
        //listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        setListAdapter(adapter);
        //getListView().setSelector(android.R.layout.simple_list_item_checked);
//        //listView = (ListView) root.findViewById(R.id.list_item);
//        listFragment = get
//        listView = getListAdapter();
        //listView.setAdapter(adapter);
        //listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);



        Button mButton = (Button) root.findViewById(R.id.deleteRun);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //long checkId = adapter.getItemId(0);
                long id = getSelectedItemId();

                dataSource.deleteRun(listId);
                //listView.clearChoices();
                Log.d("My Tag", "Delete button pressed " + listId);
                adapter.changeCursor(dataSource.getAllRuns());
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
        //listView.setItemChecked(position, true);
        listId = position;
        Log.d("My Tag", "OnListItemClick " + position);
        Log.d("My Tag", "getSelectedItemID" + getSelectedItemId() + "Position " + getSelectedItemPosition());
        Log.d("My Tag", "adapter.getItem " + adapter.getItem(position));

     }

}