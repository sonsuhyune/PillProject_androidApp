package com.example.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ListResultItem extends AppCompatActivity {

    private Drawable pill ;
    private String pill_name ;


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

}
