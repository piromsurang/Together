package com.example.piromsurang.together.models;

import com.example.piromsurang.together.presenters.InvitationPresenter;

import java.util.UUID;

/**
 * Created by piromsurang on 29/3/2018 AD.
 */

public class ReceivedInvitation implements Invitation{

    private String uuid;
    private String title;
    private String location;
    private int countDownMinute;
    private String meeting_time;
    private String currentTime;
    private int type;
    private ReceivedStatus status;

    public ReceivedInvitation(String title,
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
        this.type = RECEIVED;
        this.uuid = uuid;
    }

    public ReceivedStatus getStatus() {
        return status;
    }

    public void setStatus(ReceivedStatus status) {
        this.status = status;
    }

    public String getUuid() {
        return uuid;
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

    public String getMeeting_time() {
        return meeting_time;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    @Override
    public int getInvitationType() {
        return type;
    }

    public enum ReceivedStatus {
        ACCEPT,
        DECLINE,
        WAITING
    }
}
