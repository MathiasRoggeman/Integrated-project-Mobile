package be.ap.edu.owa_app.Calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.ap.edu.owa_app.R;

/**
 * Created by isa_l on 12-01-18.
 */

class Tab2Events extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_events, container, false);


        return rootView;
    }
}