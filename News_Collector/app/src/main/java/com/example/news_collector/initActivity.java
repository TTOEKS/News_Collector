package com.example.news_collector;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

// Button button_initlogin, button_initsignup, button_test
public class initActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        Button button_login, button_signup, button_test;

        button_login    = findViewById(R.id.button_initLogin);
        button_signup   = findViewById(R.id.button_initSignup);
        button_test     = findViewById(R.id.button_test);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(initActivity.this, loginActivity.class);
                startActivity(intent);

            }
        });

        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(initActivity.this, signupActivity.class);
                startActivity(intent);
            }
        });

        button_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(initActivity.this, loadingActivity.class);
                intent.putExtra("name", "");
                startActivity(intent);
            }
        });
    }
}
