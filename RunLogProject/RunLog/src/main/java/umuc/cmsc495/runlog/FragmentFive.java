package umuc.cmsc495.runlog;
 
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentFive extends Fragment {
 
    public static Fragment newInstance(Context context) {
        FragmentFive f = new FragmentFive();
 
        return f;
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_five, null);
        return root;
    }
 
}