package com.example.piromsurang.together.models;

import android.os.CountDownTimer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by piromsurang on 10/3/2018 AD.
 */

public class CreatedInvitation implements Invitation{

    private String uuid;
    private String title;
    private String location;
    private int countDownMinute;
    private String meeting_time;
    private String currentTime;
    private int type;

    public CreatedInvitation(String title,
                             String location,
                             String meeting_time,
                             int countDownMinute,
                             String currentTime,
                             String uuid) {
        this.title = title;
        this.location = location;
        this.meeting_time = meeting_time;
        this.countDownMinute = countDownMinute;
        this.currentTime = currentTime;
        this.type = CREATED;
        this.uuid = uuid;
    }

    public CreatedInvitation() {

    }

    public String getUuid() {
        return uuid;
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

    @Override
    public int getInvitationType() {
        return type;
    }

}
