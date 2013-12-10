/**
 *  Fragment that displays all entries from the RunLogTable and allows
 *  for multiple selects and deletes.  The entries are sorted by date
 */

package umuc.cmsc495.runlog;
 
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FragmentHistory extends ListFragment {
    private RunsDataSource dataSource;
    ArrayAdapter<RunLogTable> adapter;
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

        checkedIds = new HashSet<Integer>();
        runLogTables = new ArrayList<RunLogTable>();
        checkedTextViews = new ArrayList<CheckedTextView>();

        // Creates a list of all runs in the database
        List<RunLogTable> values = dataSource.getAllRuns();

        // Adds the list to the adapter
        adapter = new ArrayAdapter<RunLogTable>(getActivity(), android.R.layout.simple_list_item_checked, values);

        // Adds the adapter to the List and displays it on the page
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

                // Remove from the list of checked views.  Without this the checkbox for the
                // next item would be checked once the adapter is reset
                for (CheckedTextView checkedTextView : checkedTextViews) {
                    if (checkedTextView.isChecked())
                        checkedTextView.toggle();
                }

                // Notify the adapter that the data set has changed and certain objects have been
                // removed from the adapter.  Without this the deleted items would still
                // show on the page
                adapter.notifyDataSetChanged();


            }
        });

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a DAO
        dataSource = new RunsDataSource(getActivity());
        dataSource.open();

    }

    /**
     *
     * @param l
     * ListView that contains the items with checkboxes
     *
     * @param view
     * View is the row in the database pertaining to the box that is
     * checked or unchecked
     *
     * @param position
     * position in the list
     *
     * @param id
     * id of the view
     */
    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {
        super.onListItemClick(l, view, position, id);
        CheckedTextView textView = (CheckedTextView) view;
        textView.getId();

        /**
         *  If the check box is not currently checked the position is added to the
         *  checkedIds list, the adapter object is added to the runLogTables list,
         *  and the specific view is added to the checkedTextViews list.
         *
         *  If the check box is checked then those items are removed from their corresponding
         *  lists
         *
         *  The checkedTextViews list is used to keep track of which views are checked
         *  so that they can be unchecked when the item is deleted
        */
        if (!textView.isChecked()) {
            checkedIds.add(position);
            runLogTables.add(adapter.getItem(position));
            checkedTextViews.add(textView);
        } else {
            checkedIds.remove(position);
            runLogTables.remove(adapter.getItem(position));
            checkedTextViews.remove(textView);
        }
        textView.setChecked(!textView.isChecked());

     }
}