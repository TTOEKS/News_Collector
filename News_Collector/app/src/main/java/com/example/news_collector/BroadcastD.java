package com.example.news_collector;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;

import androidx.core.app.NotificationCompat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class BroadcastD extends BroadcastReceiver {
    int idx = 0;
    public static String NOTIFICATION_CHANNEL_ID = "10001";

    ArrayList<String> title_msg = new ArrayList<String>();
    String text;

    // This function is started by BroadCast
    @Override
    public void onReceive(Context context, Intent intent) {
        // Push Alarm Title Datas
        title_msg.add("지식을 쌓으실 시간입니다!!");
        title_msg.add("이 내용이 궁금하지 않으신가요??");
        title_msg.add("지식인으로 거듭나시죠!");
        title_msg.add("이런 세상에!!");

        Random rand = new Random();

        Bundle bundle = intent.getExtras();

        // take TextData (NEWS HEADLINE) from MainActivity
        text = bundle.getString("news_title");

        // 알람 시간이 되었을 때 onReceive 호출

        // NotificationManager 안드로이드 상태바에 메시지를 던지기 위한 서비스
        System.out.print("onReceive Function Start in BroadCastD");

        Intent activity_intent  = new Intent(context, loadingActivity.class);
        intent.putExtra("name", " ");
        activity_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Push Alarm setting
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,activity_intent , PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap mLargeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.login_img);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        builder .setTicker("HETT")
                .setWhen(System.currentTimeMillis())
                .setNumber(idx).setContentTitle(title_msg.get(rand.nextInt(title_msg.size())))
                .setContentText(text)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setLargeIcon(mLargeIcon)
                .setAutoCancel(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder.setSmallIcon(R.drawable.ic_launcher_foreground);
            CharSequence channelName = "노티피케이션 채널";
            String description = "오레오 이상입니다.";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
            channel.setDescription(description);

            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        } else
            builder.setSmallIcon(R.mipmap.ic_launcher);

        // Permiss wake up phone when phone is Lock
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK  |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "My:Tag");
        wakeLock.acquire(5000);


        assert notificationManager != null;
        notificationManager.notify(1234, builder.build());

    }


}
