package com.example.piromsurang.together.models;

/**
 * Created by piromsurang on 29/3/2018 AD.
 */

public class ReceivedInvitation implements Invitation{

    private String uuid;
    private String title;
    private String location;
    private String countDownMinute;
    private String meeting_time;
    private String currentTime;
    private int invitationType;
    private ReceivedStatus status;
    private String sender;
    private int index = -1;

    public ReceivedInvitation(String title,
                             String location,
                             String meeting_time,
                             String countDownMinute,
                             String currentTime,
                             String sender,
                             String uuid) {
        this.title = title;
        this.location = location;
        this.meeting_time = meeting_time;
        this.countDownMinute = countDownMinute;
        this.currentTime = currentTime;
        this.invitationType = RECEIVED;
        this.uuid = uuid;
        this.sender = sender;
    }

    public ReceivedInvitation() {

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

    public String getCountDownMinute() {
        return countDownMinute;
    }

    public String getMeeting_time() {
        return meeting_time;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCountDownMinute(String countDownMinute) {
        this.countDownMinute = countDownMinute;
    }

    public String getSender() {
        return sender;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int getInvitationType() {
        return invitationType;
    }

    public enum ReceivedStatus {
        ACCEPT,
        DECLINE,
        WAITING
    }
}
