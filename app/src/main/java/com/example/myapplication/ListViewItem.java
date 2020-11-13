package com.example.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ListViewItem extends AppCompatActivity {

    private Drawable pill ;
    private String pill_name ;
    private String nickname ;
    private String  company, ingredient, volumn;

    public Drawable getPill() {
        return pill;
    }

    public void setPill(Drawable pill) {
        this.pill = pill;
    }

    public String getPill_name() {
        return pill_name;
    }

    public void setPill_name(String pill_name) {
        this.pill_name = pill_name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }

    public String getIngredient() {
        return ingredient;
    }
    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getVolumn() { return volumn; }
    public void setVolumn(String volumn) {
        this.volumn = volumn;
    }

    public void more_button(View v) {
        Intent intent = new Intent(getApplicationContext(), search_result.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.transition.anim_slide_in_left, R.transition.anim_slide_out_right);

        Toast.makeText(getApplicationContext()," 자세히 보기가 눌렸습니다.", Toast.LENGTH_SHORT).show();
    }
}
