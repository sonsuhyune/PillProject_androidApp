package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class add_pill extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_pill);
    }

    public void add_pill_user_button(View v) {
        Intent intent = new Intent(getApplicationContext(), add_pill_user.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext()," 직접 등록하기가 눌렸습니다.", Toast.LENGTH_SHORT).show();
    }
}
