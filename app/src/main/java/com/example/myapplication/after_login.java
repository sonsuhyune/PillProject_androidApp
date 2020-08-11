package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class after_login extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_login);
    }
    public void add_pill_button(View v) {
        Intent intent = new Intent(getApplicationContext(), add_pill.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext()," 알약등록 하기가 눌렸습니다.", Toast.LENGTH_SHORT).show();
    }
}
