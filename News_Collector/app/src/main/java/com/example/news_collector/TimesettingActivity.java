package com.example.news_collector;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TimesettingActivity extends AppCompatActivity {

    /*
    timepicker_setting
    button_timesave
    recyclerview_alarm_time
     */
    ArrayList<recyclerviewItem_time_setting>    mDatalist;
    Adapter_time_setting                        adapterTimeSetting;
    int hour, minute;
    String time;
    ArrayList<String> pre_set;
    RecyclerView    recyclerView_alarm_time;
    Set<String> emptySet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("onCreate: ", "Start!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_time);


        Button          button_timesave;
        final TimePicker      timePicker_setting;

        button_timesave         = findViewById(R.id.button_timesave);
        timePicker_setting      = findViewById(R.id.timepicker_setting);
        recyclerView_alarm_time = findViewById(R.id.recyclerview_alarm_time);
        recyclerView_alarm_time.setLayoutManager(new LinearLayoutManager(this));
        mDatalist = new ArrayList<recyclerviewItem_time_setting>();

        SharedPreferences shared = getSharedPreferences("time_setting", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();

        // Pre_push alarm time setting
        final Set<String> pre_time_set = shared.getStringSet("time_set", null);

        if(!(pre_time_set == null)) {
            pre_set = new ArrayList<String>(pre_time_set);
            Log.i("pre_set", pre_set.toString());
            set_pretime(pre_set);
        }

        // load pre_Time Setting


        adapterTimeSetting  = new Adapter_time_setting(mDatalist);
        recyclerView_alarm_time.setAdapter(adapterTimeSetting);


        // If user setting time and press save button, display saving time in recyclerview
        button_timesave.setOnClickListener(new View.OnClickListener() {


            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                recyclerviewItem_time_setting timeData = new recyclerviewItem_time_setting();

                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
                    hour = timePicker_setting.getHour();
                    minute = timePicker_setting.getMinute();
                } else{
                    hour =  timePicker_setting.getCurrentHour();
                    minute = timePicker_setting.getCurrentMinute();
                }

                timeData.setHour(hour);
                timeData.setMinute(minute);
                mDatalist.add(timeData);


                adapterTimeSetting.notifyDataSetChanged();


            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("onPause: ", "Start!");
        super.onBackPressed();

        // Save push alarm time to SharedPreference using Recyclerview data set
        SharedPreferences shared = getSharedPreferences("time_setting", MODE_PRIVATE);
        Set<String> set = new HashSet<String>();
        SharedPreferences.Editor editor = shared.edit();

        for(int i=0; i<mDatalist.size(); i++){
            time = mDatalist.get(i).getHour() + "_" + mDatalist.get(i).getMinute();
            set.add(time);

        }

        editor.putStringSet("time_set", set);
        editor.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("onResume: ", "Start!");
    }

    // Function of setting recyclerview data
    protected void set_pretime(ArrayList<String> time_list){
        String time_tmp;
        String[] split_time;
        mDatalist = new ArrayList<recyclerviewItem_time_setting>();
        recyclerView_alarm_time = findViewById(R.id.recyclerview_alarm_time);

        adapterTimeSetting  = new Adapter_time_setting(mDatalist);
        recyclerView_alarm_time.setAdapter(adapterTimeSetting);
        AlarmClock clock = new AlarmClock();

        recyclerviewItem_time_setting timeData = new recyclerviewItem_time_setting();

        for(int i=0; i < time_list.size(); i++) {

            timeData = new recyclerviewItem_time_setting();
            time_tmp = time_list.get(i);
            split_time = time_tmp.split("_");

            timeData.setHour(Integer.parseInt(split_time[0]));
            timeData.setMinute(Integer.parseInt(split_time[1]));
            mDatalist.add(timeData);

        }
        adapterTimeSetting.notifyDataSetChanged();
    }

}
