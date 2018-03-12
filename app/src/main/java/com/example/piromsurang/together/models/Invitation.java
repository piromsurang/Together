package com.example.piromsurang.together.models;

import android.os.CountDownTimer;

import java.util.Date;

/**
 * Created by piromsurang on 10/3/2018 AD.
 */

public class Invitation {

    private String title;
    private String location;
    private int countDownMinute;
    private Date currentDate;
    private CountDownTimer timer;
    //need to implement friends

    public Invitation(String title, String location, int countDownMinute, Date date) {
        this.title = title;
        this.location = location;
        this.countDownMinute = countDownMinute;
        this.currentDate = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public int getCountDownMinute() {
        return countDownMinute;
    }

}
