package kris.hazen.dev.fulliot.Fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;

import kris.hazen.dev.fulliot.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WateringFragment extends Fragment {

    Switch mSwitch;
    FrameLayout mframe;
    Fragment fragment = new ManualFragment();

    public WateringFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_watering, container, false);

        mSwitch = (Switch) rootView.findViewById(R.id.swAuto);
        mframe = (FrameLayout) rootView.findViewById(R.id.modeFrame);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.modeFrame, fragment);
        fragmentTransaction.commit();

        mSwitch.setChecked(false);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    fragment = new AutomaticFragment();
                }else {
                    fragment = new ManualFragment();
                }
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN );
                fragmentTransaction.replace(R.id.modeFrame, fragment);
                fragmentTransaction.commit();
            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
