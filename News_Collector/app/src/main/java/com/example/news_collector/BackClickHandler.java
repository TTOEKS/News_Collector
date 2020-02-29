package com.example.news_collector;

import android.app.Activity;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class BackClickHandler {
    private long backKeyClickTime = 0;
    private Activity activity;

    public BackClickHandler(Activity activity){
        this.activity = activity;
    }
    public void onBackPressed() {
        if(System.currentTimeMillis() > backKeyClickTime + 2000){
            backKeyClickTime = System.currentTimeMillis();
            showToast();
            return;
        }
        if(System.currentTimeMillis() <= backKeyClickTime + 2000){
            ActivityCompat.finishAffinity(activity);
            System.exit(0);
        }
    }
    public void showToast(){
        Toast.makeText(activity, "뒤로 가기 버튼을 한 번더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
    }

}
