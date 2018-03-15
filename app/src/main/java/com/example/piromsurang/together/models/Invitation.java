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
    private String meeting_time;
    private String currentTime;
    //need to implement friends

    public Invitation(String title, String location, String meeting_time, int countDownMinute, String currentTime) {
        this.title = title;
        this.location = location;
        this.meeting_time = meeting_time;
        this.countDownMinute = countDownMinute;
        this.currentTime = currentTime;
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

    public String getCurrentTime() {
        return currentTime;
    }

    public String getMeeting_time() {
        return meeting_time;
    }

}
