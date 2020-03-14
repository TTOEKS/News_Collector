package com.example.news_collector;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


// get Intent infomations : name

public class loadingActivity extends AppCompatActivity {
    String user_name;
    JSONArray jsonArray_Politics     = new JSONArray();
    JSONArray jsonArray_Economy      = new JSONArray();
    JSONArray jsonArray_IT           = new JSONArray();
    JSONArray jsonArray_Security     = new JSONArray();
    JSONArray jsonArray_Science      = new JSONArray();
    JSONArray jsonArray_Global       = new JSONArray();
    ArrayList<Boolean> interesting = new ArrayList<Boolean>();

    JSONObject jsonObject   = new JSONObject();
    final webCrawler Crawler = new webCrawler();

    /*

    푸시 알람 사용    : key_push_alarm
    푸시 알람 설정    : key_push_time
    알람 소리         : key_alarm_sound
    저녁 알람 허용    : key_night_alarm
        - uninterested Topic Shared Preference-
    정치: key_politics
    경제: key_economy
    IT:  key_IT
    보안: key_security
    기술/과학: key_science
    세계: key_global

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        // Fade in Animation
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this,  R.anim.fade_in);
        LinearLayout init_layout = (LinearLayout) findViewById(R.id.Linearlayout_Init);

        Animation fade_in = new AlphaAnimation(1, 0);
        fade_in.setDuration(1000);
        fade_in.setStartOffset(1000);

        init_layout.startAnimation(fadeInAnimation);

        // Ignore SSL Certification Conntecting
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};
            // Install the all-trusting trust manager
            final SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        }catch(Exception e){
            e.printStackTrace();
        }

        Log.i("Lodaing Activity", "Start!");

        crawlingTask CrawTask = new crawlingTask();
        CrawTask.execute();


        /*
        while(jsonArray.isNull(3)) {
            try {
                Thread.interrupted();
            } catch(Exception e){
                e.printStackTrace();
            }
        }
*/



    }

    public class crawlingTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(Void... voids) {

            // Crawlering and save in JSONArray each topic
            jsonArray_Politics = Crawler.parsing("http://news.jtbc.joins.com/section/index.aspx?scode=10",
                    "bd news_area", "title_cr", "read_cr");
            jsonArray_Economy = Crawler.parsing("https://www.mk.co.kr/news/economy/",
                    "list_area", "tit", "desc");
            jsonArray_IT      = Crawler.parsing("http://www.itnews.or.kr/?cat=1177",
                    "td-ss-main-content", "td-module-meta-info", "td-excerpt");
            jsonArray_Security = Crawler.parsing("https://www.boannews.com/media/list.asp?mkind=1",
                    "news_list", "news_txt", "news_content");
            jsonArray_Science = Crawler.parsing("http://www.itnews.or.kr/?cat=1179",
                    "td-ss-main-content", "td-module-meta-info", "td-excerpt");
            jsonArray_Global = Crawler.parsing("https://www.yna.co.kr/international/all?site=navi_international_dep02",
                    "list-type038","tit-wrap","lead");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent_send = new Intent(loadingActivity.this, MainActivity.class);

            // every news data hand to MainActivity
            intent_send.putExtra("jsonArray_Politics", jsonArray_Politics.toString());
            intent_send.putExtra("jsonArray_Economy", jsonArray_Economy.toString());
            intent_send.putExtra("jsonArray_IT", jsonArray_IT.toString());
            intent_send.putExtra("jsonArray_Security", jsonArray_Security.toString());
            intent_send.putExtra("jsonArray_Science", jsonArray_Science.toString());
            intent_send.putExtra("jsonArray_Global", jsonArray_Global.toString());

            startActivity(intent_send);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            Toast.makeText(getApplicationContext(), "환영합니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
