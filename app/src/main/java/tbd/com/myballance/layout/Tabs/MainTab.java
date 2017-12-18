package tbd.com.myballance.layout.Tabs;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by todorbachvarov on 17.12.17.
 */

public abstract class MainTab extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(getLayoutId(), container, false);
        init(rootView);
        return rootView;
    }

    protected void init(View rootView){

    };
    protected  abstract int getLayoutId();
}
