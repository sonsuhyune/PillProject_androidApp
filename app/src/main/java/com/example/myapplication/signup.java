package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
    }
    public void temp_signup_button(View v) {
        Intent intent = new Intent(getApplicationContext(), login.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(),"로그인하기가 눌렸습니다.", Toast.LENGTH_SHORT).show();
    }
}
