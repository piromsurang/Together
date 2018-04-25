package com.example.piromsurang.together.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by piromsurang on 17/3/2018 AD.
 */

public class InvitationRepository {
    private static InvitationRepository instance = null;
    private ArrayList<CreatedInvitation> createdInvitationList;
    private ArrayList<ReceivedInvitation> receivedInvitationList;

    private InvitationRepository() {
        createdInvitationList = new ArrayList<>();
        receivedInvitationList = new ArrayList<>();
    }

    public static InvitationRepository getInstance() {
        if(instance == null) {
            instance = new InvitationRepository();
        }
        return instance;
    }

    public ArrayList<CreatedInvitation> getCreatedInvitationList() {
        return createdInvitationList;
    }

    public ArrayList<ReceivedInvitation> getReceivedInvitationList() {
        return receivedInvitationList;
    }

    public void addToCreatedInvitation(CreatedInvitation invitation) {
        if(!createdInvitationList.contains(invitation)) {
            createdInvitationList.add(invitation);
        }
    }

    public void addToReceivedInvitation(ReceivedInvitation invitation) {
        if(!receivedInvitationList.contains(invitation)) {
            receivedInvitationList.add(invitation);
        }
    }

    public void removeFromReceivedInvitation(ReceivedInvitation invitation) {
        receivedInvitationList.remove(invitation);
    }

    public CreatedInvitation getCreatedInvitationFromUuid(String uuid) {
        for(CreatedInvitation c : createdInvitationList) {
            if(c.getUuid().equals(uuid)) {
                return c;
            }
        }
        return new CreatedInvitation();
    }
    public ReceivedInvitation getReceivedInvitationFromUuid(String uuid) {
        for(ReceivedInvitation c : receivedInvitationList) {
            if(c.getUuid().equals(uuid)) {
                return c;
            }
        }
        return new ReceivedInvitation();
    }
}
