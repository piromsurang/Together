package com.example.piromsurang.together.models;

import android.os.CountDownTimer;

import java.util.Date;

/**
 * Created by piromsurang on 10/3/2018 AD.
 */

public class Invitation {

    private String title;
    private String location;
    private Date date;
    private CountDownTimer timer;
    //need to implement friends

    public Invitation(String title, String location, Date date) {
        this.title = title;
        this.location = location;
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public Date getDate() {
        return date;
    }

}
