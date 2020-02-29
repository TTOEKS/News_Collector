package com.example.news_collector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;



public class MainActivity extends AppCompatActivity {

    // TextView user_name
    // RecyclerView news_data set/get class, Adapter/Viewholder
    ArrayList<recyclerviewItem_news_data> dataList_data;
    ArrayList<recyclerviewItem_news_topic> dataList_topic;
    Adapter_news_data                   dataAdapter;
    Adapter_news_topic                  topicAdapter;

    final static String TAG = "MainActivity";
    String user_name;
    ImageView imageview_setting;
    TextView textview_user_name;
    Boolean f_politics, f_economy, f_IT, f_security, f_science, f_global;
    RecyclerView recyclerview_news_topic, recyclerview_news_data;
    JSONArray jsonArray_Politics = new JSONArray();
    JSONArray jsonArray_Economy = new JSONArray();
    JSONArray jsonArray_IT = new JSONArray();
    JSONArray jsonArray_Security = new JSONArray();
    JSONArray jsonArray_Science = new JSONArray();
    JSONArray jsonArray_Global = new JSONArray();
    HashMap<String, Boolean> favorite_list = new HashMap<String, Boolean>();
    HashMap<String, Boolean> interesting = new HashMap<String, Boolean>();
    private BackClickHandler backClickHandler;
    String alarm_time;
    Random rand = new Random();
    ArrayList<String> Topic_list = new ArrayList<String>();
    ArrayList<String> pre_set;
    String[] split_time;
    private Set<String> pre_time_set ;
    AlarmManager am;
    PendingIntent sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("MainActivity ", "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        imageview_setting       = findViewById(R.id.imageview_setting);
        recyclerview_news_topic  = (RecyclerView)findViewById(R.id.recyclerview_news_topic);
        recyclerview_news_data   = (RecyclerView)findViewById(R.id.recyclerview_news_data);
        recyclerview_news_topic.setLayoutManager(new LinearLayoutManager(this));



        dataList_data = new ArrayList<recyclerviewItem_news_data>();
        dataList_topic = new ArrayList<recyclerviewItem_news_topic>();

        topicAdapter = new Adapter_news_topic(dataList_topic);
        recyclerview_news_topic.setAdapter(topicAdapter);

        // set favorite data
        SharedPreferences shared = getSharedPreferences("favorite", MODE_PRIVATE);

        f_politics = shared.getBoolean("정치", false);
        f_economy = shared.getBoolean("경제", false);
        f_IT = shared.getBoolean("IT", false);
        f_security = shared.getBoolean("보안", false);
        f_science = shared.getBoolean("기술 / 과학", false);
        f_global = shared.getBoolean("세계", false);



        // set favorite list
        favorite_list.put("정치", f_politics);
        favorite_list.put("경제", f_economy);
        favorite_list.put("IT", f_IT);
        favorite_list.put("보안", f_security);
        favorite_list.put("기술 / 과학", f_science);
        favorite_list.put("세계", f_global);



        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        SharedPreferences pre_shared = PreferenceManager.getDefaultSharedPreferences(this);
        interesting.put("Politics", !pre_shared.getBoolean("key_politics", false));
        interesting.put("Economy", !pre_shared.getBoolean("key_economy", false));
        interesting.put("IT", !pre_shared.getBoolean("key_IT", false));
        interesting.put("Security", !pre_shared.getBoolean("key_security", false));
        interesting.put("Science", !pre_shared.getBoolean("key_science", false));
        interesting.put("Global", !pre_shared.getBoolean("key_global", false));


        /*
            푸시 알람 사용    : key_push_alarm
            푸시 알람 설정    : key_push_time
            알람 소리         : key_alarm_sound
            저녁 알람 허용    : key_night_alarm

         */
        alarm_time = pre_shared.getString("key_push_time", "24");



        try {
            // Set News Data from Loading Activity
            jsonArray_Politics = new JSONArray(bundle.getString("jsonArray_Politics"));
            jsonArray_Economy = new JSONArray(bundle.getString("jsonArray_Economy"));
            jsonArray_IT = new JSONArray(bundle.getString("jsonArray_IT"));
            jsonArray_Security = new JSONArray(bundle.getString("jsonArray_Security"));
            jsonArray_Science = new JSONArray(bundle.getString("jsonArray_Science"));
            jsonArray_Global = new JSONArray(bundle.getString("jsonArray_Global"));



            Log.i("jsonArray", jsonArray_Politics.toString());

            // set news Data into RecyclerView
            setDataList("정치", jsonArray_Politics, f_politics, interesting.get("Politics"));
            setDataList("경제", jsonArray_Economy, f_economy, interesting.get("Economy"));
            setDataList("IT", jsonArray_IT, f_IT, interesting.get("IT"));
            setDataList("보안", jsonArray_Security, f_security, interesting.get("Security"));
            setDataList("기술 / 과학", jsonArray_Science, f_science, interesting.get("Science"));
            setDataList("세계", jsonArray_Global, f_global, interesting.get("Global"));



        } catch (JSONException e){
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }

        // Show Prference page
        imageview_setting.setClickable(true);
        imageview_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent = new Intent(MainActivity.this, preferencesActivity.class);
                startActivity(intent);
            }
        });

        // Back Button Handler
        backClickHandler = new BackClickHandler(this);


        Log.i("Topic_Data", Topic_list.get(rand.nextInt(Topic_list.size())));
    }   // END of onCreate()

    // set Push Alarm Reservation Function
    public void setAlarm(Context context, int hour, int minute){
        final String TAG = "setAlarm_Function";
        AlarmManager am = (AlarmManager)getSystemService(context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        Intent intent = new Intent(context, BroadcastD.class);
        intent.putExtra("news_title", Topic_list.get(rand.nextInt(Topic_list.size())));
        intent.setAction("notification");

        // Make unique Request Code
        int req_code = hour + minute ;

        sender = PendingIntent.getBroadcast(context, req_code, intent,  PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT );
        Log.i(TAG, String.valueOf(req_code));

        // Set Time from Params
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        long sdl = calendar.getTimeInMillis();


        // 알람 주기 calendar에 설정
        // reservation Alarm
        am.set(AlarmManager.RTC_WAKEUP, sdl, sender);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000, sender);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Load user set time in Preference
        SharedPreferences time_shared = getSharedPreferences("time_setting", MODE_PRIVATE);
        pre_time_set = time_shared.getStringSet("time_set", null);
        Log.i("pre_time_set", pre_time_set.toString());


        // Push Time setting
        if(!(pre_time_set.isEmpty())) {
            if(sender != null && am != null) {
                Log.i(TAG, "Sender is not null!!");
                am.cancel(sender);
                sender.cancel();
            }
            pre_set = new ArrayList<String>(pre_time_set);
            Log.i("pre_set", pre_set.toString());

            for(int i=0; i < pre_set.size(); i++) {

                String time_tmp = pre_set.get(i);
                split_time = time_tmp.split("_");
                setAlarm(this, Integer.parseInt(split_time[0]), Integer.parseInt(split_time[1]));

            }

        }
        Log.i(TAG, "onStart: Start!");
    }

    // if user press back button twice, exit
    public void onBackPressed() {
        SharedPreferences sharedPreferences = getSharedPreferences("favorite", MODE_PRIVATE);
        Set<String> keys = favorite_list.keySet();
        SharedPreferences.Editor editor = sharedPreferences.edit();


        for(String key : keys){
            Log.i("keys: ", key);
            Log.i(key, topicAdapter.getFavorite(key).toString());
            editor.putBoolean(key, topicAdapter.getFavorite(key));
        }


        editor.commit();

        backClickHandler.onBackPressed();
    }

    // if interesting is false, topic is not display
    private void setDataList(String topic, JSONArray jsonArray, Boolean favorite, Boolean interesting) {
        if(!interesting)    return;
        try {
            if (!jsonArray.isNull(1)) {
                recyclerviewItem_news_topic topicData = new recyclerviewItem_news_topic();
                topicData.setNews_topic(topic);
                topicData.setFavorite(favorite);


                recyclerviewItem_news_data newsData = new recyclerviewItem_news_data();
                dataList_data = new ArrayList<recyclerviewItem_news_data>();
                for (int i = 0; i < 10; i++) {
                    JSONObject jsonObject_Politics = jsonArray.getJSONObject(i);

                    newsData = new recyclerviewItem_news_data();
                    newsData.setTitle(jsonObject_Politics.getString("title"));
                    Topic_list.add(jsonObject_Politics.getString("title"));
                    newsData.setContents(jsonObject_Politics.getString("content"));
                    newsData.setUrl(jsonObject_Politics.getString("url"));

                    dataList_data.add(newsData);
                }
                topicData.setAllItemsInSection(dataList_data);
                if(favorite){
                    dataList_topic.add(0, topicData);
                }else dataList_topic.add(topicData);

                topicAdapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
