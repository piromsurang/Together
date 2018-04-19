package com.example.piromsurang.together.models;

/**
 * Created by piromsurang on 10/3/2018 AD.
 */

public class CreatedInvitation implements Invitation{

    private String uuid;
    private String title;
    private String location;
    private String countDownMinute;
    private String meeting_time;
    private String currentTime;
    private int invitationType;

    public CreatedInvitation(String title,
                             String location,
                             String meeting_time,
                             String countDownMinute,
                             String currentTime,
                             String uuid) {
        this.title = title;
        this.location = location;
        this.meeting_time = meeting_time;
        this.countDownMinute = countDownMinute;
        this.currentTime = currentTime;
        this.invitationType = CREATED;
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

    public String getCountDownMinute() {
        return countDownMinute;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public String getMeeting_time() {
        return meeting_time;
    }

    public void setCountDownMinute(String countDownMinute) {
        this.countDownMinute = countDownMinute;
    }

    @Override
    public int getInvitationType() {
        return invitationType;
    }

}
