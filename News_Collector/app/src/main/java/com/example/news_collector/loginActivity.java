package com.example.news_collector;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;


// This Activity does not use
public class loginActivity extends AppCompatActivity {
    protected String id, password, result;
    String HOST = "";
    JSONObject jsonObject = new JSONObject();

    // edittext login_id, login_password
    // button login
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText editText_id, editText_password;
        Button button_login;

        editText_id         = findViewById(R.id.edittext_login_id);
        editText_password   = findViewById(R.id.edittext_login_password);
        button_login         = findViewById(R.id.button_login);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id          = editText_id.getText().toString();
                password    = editText_password.getText().toString();



                
                if(id.isEmpty() | password.isEmpty()){
                    Toast.makeText(loginActivity.this, "아이디 혹은 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                try{
                    jsonObject.put("id", id);
                    jsonObject.put("password", password);

                    Log.d("Login_data", jsonObject.toString());

                    LoginTask loginTask = new LoginTask();
                    result = loginTask.execute(jsonObject.toString()).get();

                    Log.d("Login_result: ", result);

                    if(result.equals("Null")){
                        Toast.makeText(loginActivity.this, "아이디와 패스워드를 확인해주세요!!", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = new Intent(loginActivity.this, loadingActivity.class);
                        intent.putExtra("name", result);

                        Log.i("loginActivity ","StartActivity before");
                        startActivity(intent);
                    }



                }catch(JSONException e){
                    e.printStackTrace();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }catch(ExecutionException e){
                    e.printStackTrace();
                }
            }
        });
    }
    public class LoginTask extends AsyncTask<String, Void, String> {
        String sendMsg, recieveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try{
                String str;
                URL url = new URL("http://"+HOST+":8080/news_collector/login.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Conetent-type", "application/x-www/form/urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                sendMsg = "login_data=" + URLEncoder.encode(strings[0], "UTF-8");
                Log.i("sendMsg: ",sendMsg);
                osw.write(sendMsg);
                osw.flush();

                if(conn.getResponseCode() == conn.HTTP_OK){
                    InputStreamReader tmp     = new InputStreamReader(conn.getInputStream());
                    BufferedReader reader  = new BufferedReader(tmp);
                    StringBuffer        buffer  = new StringBuffer();

                    while((str = reader.readLine()) != null){
                        buffer.append(str);
                    }

                    recieveMsg = buffer.toString();
                }else{
                    Log.e("Connect Error", conn.getResponseCode() + "ERROR");
                }



            }catch(MalformedURLException e){
                Log.e("SignupTask:", "MalformedURLException");
                e.printStackTrace();
            }catch(IOException e){
                Log.e("SignupTask:", "IOException");
                e.printStackTrace();
            }

            return recieveMsg;
        }
    }
}
