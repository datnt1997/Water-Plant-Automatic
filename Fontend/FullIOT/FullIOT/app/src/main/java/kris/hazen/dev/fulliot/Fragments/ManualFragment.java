package kris.hazen.dev.fulliot.Fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;

import kris.hazen.dev.fulliot.R;
import rm.com.youtubeplayicon.PlayIconDrawable;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManualFragment extends Fragment {

    ImageView ivStart;
    Chronometer focus;
    boolean state = true;

    public ManualFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manual, container, false);
        focus = (Chronometer) rootView.findViewById(R.id.chronometer2);
        final ImageView iconView = (ImageView) rootView.findViewById(R.id.iv_start);

        focus.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h   = (int)(time /3600000);
                int m = (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000- m*60000)/1000 ;
                String hh = h < 10 ? "0"+h: h+"";
                String mm = m < 10 ? "0"+m: m+"";
                String ss = s < 10 ? "0"+s: s+"";
                cArg.setText(hh+":"+mm+":"+ss);
            }
        });

        final PlayIconDrawable play = PlayIconDrawable.builder()
                .withColor(getResources().getColor(R.color.accent))
                .withInterpolator(new FastOutSlowInInterpolator())
                .withDuration(300)
                .withInitialState(PlayIconDrawable.IconState.PLAY)
                .withAnimatorListener(new AnimatorListenerAdapter() {
                    @Override public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        Log.d("MainActivity", "animationFinished");
                    }
                })
                .withStateListener(new PlayIconDrawable.StateListener() {
                    @Override public void onStateChanged(PlayIconDrawable.IconState state) {
                        Log.d("MainActivity", "onStateChanged: " + state);
                    }
                })
                .into(iconView);

        iconView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                play.toggle(true);

                if(state){
                    focus.setBase(SystemClock.elapsedRealtime());
                    focus.start();
                    state = false;
                }else{
                    focus.stop();
                    state = true;
                }

            }
        });

        return rootView;
    }

}
