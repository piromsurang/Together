package com.example.piromsurang.together.models;

/**
 * Created by piromsurang on 4/4/2018 AD.
 */

public interface Invitation {

    int CREATED = 1;
    int RECEIVED = 2;

    public int getInvitationType();
    public String getUuid();
}
