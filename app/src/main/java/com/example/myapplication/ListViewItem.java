package com.example.myapplication;

import android.graphics.drawable.Drawable;

public class ListViewItem {
    private Drawable pill ;
    private String pill_name ;
    private String nickname ;

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
}
