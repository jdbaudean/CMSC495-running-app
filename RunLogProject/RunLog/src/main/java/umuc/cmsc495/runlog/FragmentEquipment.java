/**
 *  Fragment that displays and enters Equipment used for running.
 *  This feature has not yet been developed.
 */

package umuc.cmsc495.runlog;
 
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class FragmentEquipment extends Fragment {
 
    public static Fragment newInstance(Context context) {
        FragmentEquipment f = new FragmentEquipment();
 
        return f;
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_equipment, null);
        Toast.makeText(getActivity(), "Feature has not yet been implemented", Toast.LENGTH_LONG).show();
        return root;
    }
 
}