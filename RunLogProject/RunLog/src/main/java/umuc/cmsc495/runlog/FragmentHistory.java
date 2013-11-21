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
    //SimpleCursorAdapter adapter;
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

//        Cursor cursor = dataSource.getAllRuns();
//        adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_expandable_list_item_1, cursor,
//                new String[] { "distance" },
//                new int[] {android.R.id.text1});

        List<RunLogTable> values = dataSource.getAllRuns();

        //ArrayAdapter<RunLogTable> adapter = new ArrayAdapter<RunLogTable>(getActivity(), android.R.layout.simple_list_item_checked, values);
        adapter = new ArrayAdapter<RunLogTable>(getActivity(), android.R.layout.simple_list_item_checked, values);
        checkedIds = new HashSet<Integer>();
        runLogTables = new ArrayList<RunLogTable>();
        checkedTextViews = new ArrayList<CheckedTextView>();

        listView = new ListView(getActivity());
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

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

                for (Integer ids : checkedIds) {
                    //dataSource.deleteRun(adapter.getItem(ids).getId());
                }

                for (RunLogTable runLogTable : runLogTables) {
                    adapter.remove(runLogTable);
                }


                checkedIds.clear();
                //dataSource.deleteRun(listid);
                //listView.clearChoices();

                //adapter.changeCursor(dataSource.getAllRuns());
                //adapter.remove(adapter.getItem((int) listId));
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

        //listView.setItemChecked(position, true);


        Log.d("My Tag", "adapter position: " + adapter.getItem(position).getId());
        //Log.d("My Tag", "adapter position: " + checkedIds);
        //listid = adapter.getItem(position).getId();
        if (!textView.isChecked()) {
            //checkedIds.add(adapter.getItem(position).getId());
            checkedIds.add(position);
            runLogTables.add(adapter.getItem(position));
        }

        if (textView.isChecked()) {
            //checkedIds.remove(adapter.getItem(position).getId());
            checkedIds.remove(position);
            runLogTables.remove(adapter.getItem(position));
        }
        textView.setChecked(!textView.isChecked());
        //textView.toggle();

        //Log.d("My Tag", "adapter.getItem " + adapter.getItemId(id));

     }

}