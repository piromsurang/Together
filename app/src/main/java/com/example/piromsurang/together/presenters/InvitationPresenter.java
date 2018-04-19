package com.example.piromsurang.together.presenters;

import com.example.piromsurang.together.models.CreatedInvitation;
import com.example.piromsurang.together.models.InvitationRepository;
import com.example.piromsurang.together.models.ReceivedInvitation;
import com.example.piromsurang.together.views.InvitationView;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by piromsurang on 29/3/2018 AD.
 */

public class InvitationPresenter {
    private InvitationRepository invitationRepository;
    private InvitationView invitationView;

    public InvitationPresenter(InvitationRepository invitationRepository, InvitationView invitationView) {
        this.invitationRepository = invitationRepository;
        this.invitationView = invitationView;
    }

    public ArrayList<CreatedInvitation> getCreatedInvitationList() {
        return invitationRepository.getCreatedInvitationList();
    }

    public ArrayList<ReceivedInvitation> getReceivedInvitationList() {
        return invitationRepository.getReceivedInvitationList();
    }

    public void addToCreatedInvitation(CreatedInvitation invitation) {
        invitationRepository.addToCreatedInvitation(invitation);
    }

    public void addToReceivedInvitation(ReceivedInvitation invitation) {
        invitationRepository.addToReceivedInvitation(invitation);
    }

    public void removeFromReceivedInvitation(ReceivedInvitation invitation) {
        invitationRepository.removeFromReceivedInvitation(invitation);
    }

    public String generateUuid() {
        return UUID.randomUUID().toString();
    }

    public void displayRecycleView() {
        invitationView.displayInvitationList();
    }

    public void updateCreatedInvitation(CreatedInvitation invitation) {
        for(int i = 0 ; i < invitationRepository.getCreatedInvitationList().size() ; i++) {
            if(invitationRepository.getCreatedInvitationList().get(i).getUuid() == invitation.getUuid()) {
                invitationRepository.getCreatedInvitationList().get(i).setCountDownMinute(invitation.getCountDownMinute());
            }
        }
    }

    public void updateReceivedInvitation(ReceivedInvitation invitation) {
        for(int i = 0 ; i < invitationRepository.getReceivedInvitationList().size() ; i++) {
            if(invitationRepository.getReceivedInvitationList().get(i).getUuid() == invitation.getUuid()) {
                invitationRepository.getReceivedInvitationList().get(i).setCountDownMinute(invitation.getCountDownMinute());
            }
        }
    }
}
