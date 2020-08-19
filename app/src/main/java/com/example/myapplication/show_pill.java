package com.example.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class show_pill extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_pill);

        ListView listview ;
        ListViewAdapter adapter;

        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.camera2),
                "타이레놀", "두통약") ;

        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.camera2),
                "부르펜", "해열제") ;

        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.camera2),
                "부르펜", "해열제") ;

        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.camera2),
                "부르펜", "해열제") ;

        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.camera2),
                "부르펜", "해열제") ;

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);

                String pill_name = item.getPill_name();
                String nickname = item.getNickname();
                Drawable pill = item.getPill();
            }
        }) ;
    }

            public void after_back(View v) {
                Intent intent = new Intent(getApplicationContext(), after_login.class);
                startActivity(intent);
                overridePendingTransition(R.transition.anim_slide_a, R.transition.anim_slide_b);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Toast.makeText(getApplicationContext(), " 뒤로가기가 눌렸습니다.", Toast.LENGTH_SHORT).show();
            }

        }
