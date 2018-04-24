package com.example.piromsurang.together.models;

import java.util.ArrayList;

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
    private ArrayList<Friend> invitedFriends = new ArrayList<>();

    public CreatedInvitation(String title,
                             String location,
                             String meeting_time,
                             String countDownMinute,
                             String currentTim,
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


    public ArrayList<Friend> getInvitedFriends() {
        return invitedFriends;
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

    public void setInvitedFriends(ArrayList<Friend> invitedFriends) {
        this.invitedFriends = invitedFriends;
    }

    @Override
    public int getInvitationType() {
        return invitationType;
    }

}
