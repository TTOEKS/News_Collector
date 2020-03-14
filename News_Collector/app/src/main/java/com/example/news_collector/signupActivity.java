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
public class signupActivity extends AppCompatActivity {

    // edittext name, id, password, password_again, email
    // button signup
    String HOST = "";
    JSONObject jsonObject = new JSONObject();
    protected String name, id, password, password_confirm, email, result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final EditText editText_name, editText_id, editText_password, editText_password_again, editText_email;
        Button button_signup;

        // set view
        editText_name           = findViewById(R.id.edittext_signup_name);
        editText_id             = findViewById(R.id.edittext_signup_id);
        editText_password       = findViewById(R.id.edittext_signup_password);
        editText_password_again = findViewById(R.id.edittext_signup_password_again);
        editText_email          = findViewById(R.id.edittext_signup_email);
        button_signup           = findViewById(R.id.button_signup);



        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name                = editText_name.getText().toString();
                id                  = editText_id.getText().toString();
                password            = editText_password.getText().toString();
                password_confirm    = editText_password_again.getText().toString();
                email               = editText_email.getText().toString();
//                Intent intent = new Intent(signupActivity.this, initActivity.class);

                if (name.isEmpty() |id.isEmpty() | password.isEmpty() | password_confirm.isEmpty() | email.isEmpty()) {
                    Toast.makeText(signupActivity.this, "빈칸이 없도록 모두 작성해주세요", Toast.LENGTH_SHORT).show();


                } else {
                    if (!(password.equals(password_confirm))){
                        Toast.makeText(signupActivity.this, "패스워드가 다릅니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        try {

                            jsonObject.put("name", name);
                            jsonObject.put("id",id);
                            jsonObject.put("password",password);
                            jsonObject.put("email",email);

                            SignupTask signupTask = new SignupTask();
                            result = signupTask.execute(jsonObject.toString()).get();

                            Log.i("signupActivity_result: ", result);
                            if(result.equals("Success")){
                                Toast.makeText(signupActivity.this, "회원가입에 성공했습니다.", Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(signupActivity.this, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }

                        }catch(JSONException e) {
                            Log.e("SignupAcitivty: ", "JSONException ERROR");
                            e.printStackTrace();
                        }catch(InterruptedException e){
                            Log.e("signupActivity:","InterruptedException ERROR");
                            e.printStackTrace();
                        }catch(ExecutionException e){
                            Log.e("signupActivity:", "ExecutionException ERROR");
                            e.printStackTrace();
                        }

                        Log.d("Data", jsonObject.toString());




                        // Back to init Activity
//                        startActivity(intent);
                    }
                }




            }
        });


    }
    public class SignupTask extends AsyncTask<String ,Void, String> {
        String sendMsg, recieveMsg;

        @Override
        protected String doInBackground(String... strings) {
                    try{
                        String str;
                        URL url = new URL("http://"+HOST+":8080/news_collector/signup.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Conetent-type", "application/x-www/form/urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                sendMsg = "signup_data=" + URLEncoder.encode(strings[0], "UTF-8");
                Log.i("sendMsg: ",sendMsg);
                osw.write(sendMsg);
                osw.flush();

                if(conn.getResponseCode() == conn.HTTP_OK){
                    InputStreamReader   tmp     = new InputStreamReader(conn.getInputStream());
                    BufferedReader      reader  = new BufferedReader(tmp);
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

