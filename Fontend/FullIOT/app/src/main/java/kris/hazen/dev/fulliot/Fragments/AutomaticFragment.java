package kris.hazen.dev.fulliot.Fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Switch;

import com.nex3z.togglebuttongroup.button.CircularToggle;

import java.util.ArrayList;

import kris.hazen.dev.fulliot.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AutomaticFragment extends Fragment{

    Spinner planType, planAge;
    Switch aSwitchSetting;
    LinearLayout layoutSetting, layoutHumidity, layoutSchodule;
    CheckBox ckHumidity, ckSchedule;
    EditText etStopTime;
    CircularToggle ctMon, ctTue, ctWed, ctThu, ctFri, ctSat, ctSun;
    ArrayList<CircularToggle> circleList = new ArrayList<CircularToggle>();
    EditText edHumiStart, edHumiStop;
    NumberPicker np;
    public AutomaticFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_automatic, container, false);
        aSwitchSetting = (Switch) rootView.findViewById(R.id.sw_setting);
        layoutSetting = (LinearLayout) rootView.findViewById(R.id.setting_layout);
        layoutHumidity = (LinearLayout) rootView.findViewById(R.id.layout_humidity_setting);
        layoutSchodule = (LinearLayout) rootView.findViewById(R.id.layout_schedule_setting);
        ckHumidity = (CheckBox) rootView.findViewById(R.id.ck_humidity);
        ckSchedule = (CheckBox) rootView.findViewById(R.id.ck_schedule);
        etStopTime = (EditText) rootView.findViewById(R.id.edTimeToStop);
        edHumiStart = (EditText) rootView.findViewById(R.id.edStsrtTime);
        edHumiStop = (EditText) rootView.findViewById(R.id.edStopTime);

        ctMon = (CircularToggle) rootView.findViewById(R.id.mon);
        ctTue = (CircularToggle) rootView.findViewById(R.id.tue);
        ctWed = (CircularToggle) rootView.findViewById(R.id.wed);
        ctThu = (CircularToggle) rootView.findViewById(R.id.thu);
        ctFri = (CircularToggle) rootView.findViewById(R.id.fri);
        ctSat = (CircularToggle) rootView.findViewById(R.id.sat);
        ctSun = (CircularToggle) rootView.findViewById(R.id.sun);

        circleList.add(ctMon);
        circleList.add(ctTue);
        circleList.add(ctWed);
        circleList.add(ctThu);
        circleList.add(ctFri);
        circleList.add(ctSat);
        circleList.add(ctSun);

        aSwitchSetting.setChecked(true);
        aSwitchSetting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setViewAndChildrenEnabled(layoutSetting, true);
                }else {
                    setViewAndChildrenEnabled(layoutSetting, false);
                }
            }
        });

        ckHumidity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setViewAndChildrenEnabled(layoutHumidity, true);
                }else{
                    setViewAndChildrenEnabled(layoutHumidity, false);
                }
            }
        });

        ckSchedule.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setViewAndChildrenEnabled(layoutSchodule, true);
                    for(int i=0; i<circleList.size(); i++){
                        if(circleList.get(i).isChecked()){
                            circleList.get(i).setCheckedImageDrawable(getResources().getDrawable(R.drawable.circle));
                            circleList.get(i).setTextColor(getResources().getColor(R.color.icons));
                        }else{
                            circleList.get(i).setTextColor(getResources().getColor(R.color.primary_text));
                        }
                    }
                }else{
                    setViewAndChildrenEnabled(layoutSchodule, false);
                    for(int i=0; i<circleList.size(); i++){
                        if(circleList.get(i).isChecked()){
                            circleList.get(i).setCheckedImageDrawable(getResources().getDrawable(R.drawable.circle_disable));
                            circleList.get(i).setTextColor(getResources().getColor(R.color.icons));
                        }else{
                            circleList.get(i).setCheckedImageDrawable(getResources().getDrawable(R.drawable.circle));
                            circleList.get(i).setTextColor(getResources().getColor(R.color.disable));
                        }
                    }
                }
            }
        });

        edHumiStop.setFocusable(false);
        edHumiStart.setFocusable(false);

        edHumiStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)
                        getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View npView = inflater.inflate(R.layout.number_picker_dialog_layout, null);
                np = (NumberPicker) npView.findViewById(R.id.numberPicker);
                np.setMaxValue(100);
                np.setMinValue(0);
                if(!edHumiStart.getText().toString().equals("")){
                    np.setValue(Integer.parseInt(edHumiStart.getText().toString()));
                }
                AlertDialog.Builder aler =  new AlertDialog.Builder(getContext());
                aler.setView(npView);
                aler.setTitle("Set humidity");
                aler.setPositiveButton("Set",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        edHumiStart.setText(np.getValue()+"");
                                    }
                                });
                aler.setNegativeButton(R.string.dialog_cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                });

                aler.show();
            }
        });

        edHumiStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)
                        getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View npView = inflater.inflate(R.layout.number_picker_dialog_layout, null);
                np = (NumberPicker) npView.findViewById(R.id.numberPicker);
                np.setMaxValue(100);
                np.setMinValue(0);
                if(!edHumiStop.getText().toString().equals("")){
                    np.setValue(Integer.parseInt(edHumiStop.getText().toString()));
                }
                AlertDialog.Builder aler =  new AlertDialog.Builder(getContext());
                aler.setView(npView);
                aler.setTitle("Set humidity");
                aler.setPositiveButton("Set",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                edHumiStop.setText(np.getValue()+"");
                            }
                        });
                aler.setNegativeButton(R.string.dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });

                aler.show();
            }
        });


        etStopTime.setFocusable(false);
        etStopTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                java.util.Calendar c = java.util.Calendar.getInstance();
                int year = c.get(java.util.Calendar.YEAR);
                int month = c.get(java.util.Calendar.MONTH);
                int date = c.get(java.util.Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                etStopTime.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                            }
                        }
                        , year, month, date);
                dialog.show();
            }
        });

        return rootView;

    }

    private static void setViewAndChildrenEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setViewAndChildrenEnabled(child, enabled);
            }
        }
    }






}
