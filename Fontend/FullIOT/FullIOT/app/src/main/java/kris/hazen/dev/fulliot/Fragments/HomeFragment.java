package kris.hazen.dev.fulliot.Fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kris.hazen.dev.fulliot.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static int int_items = 2 ;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);


        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return rootView;
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 : return new InfoFragment();
                case 1 : return new WateringFragment();
            }
            return null;
        }
        @Override
        public int getCount() {
            return int_items;
        }
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "Info";
                case 1 :
                    return "Water";
            }
            return null;
        }
    }


}
