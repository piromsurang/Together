package com.example.piromsurang.together.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piromsurang.together.InvitationFragment;
import com.example.piromsurang.together.R;
import com.example.piromsurang.together.listeners.FriendItemClickedListener;
import com.example.piromsurang.together.listeners.InvitationClickedListener;
import com.example.piromsurang.together.models.CreatedInvitation;
import com.example.piromsurang.together.models.Friend;
import com.example.piromsurang.together.models.Invitation;
import com.example.piromsurang.together.models.ReceivedInvitation;
import com.example.piromsurang.together.presenters.InvitationPresenter;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by piromsurang on 12/3/2018 AD.
 */

public class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.ViewHolder> {

    private InvitationPresenter presenter;
    private List<Invitation> list;
    private InvitationFragment invitationFragment;
    private TextView titleTextView;
    private TextView locationTextView;
    private TextView timeTextView;
    private TextView dateTextView;
    private TextView remainingTimeTextView;
    private RecyclerView togetherRecycleView;
    private Dialog dialog;
    private Button cancelButton;
    private Button declineButton;
    private Button acceptButton;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        public TextView remainingTime;
        private InvitationClickedListener itemClickedListener;


        // each data item is just a string in this case
        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.invitation_title);
            remainingTime = v.findViewById(R.id.countdowntime_textview);
            itemView.setOnClickListener(this);
        }

        public void setItemClickedListener(InvitationClickedListener itemClickedListener) {
            this.itemClickedListener = itemClickedListener;
        }

        @Override
        public void onClick(View v) {
            itemClickedListener.onClick(v, getAdapterPosition());
        }
    }

    public InvitationAdapter(InvitationPresenter presenter, InvitationFragment invitationFragment) {
        this.presenter = presenter;
        this.list = new ArrayList<>();
        this.invitationFragment = invitationFragment;

        dialog = new Dialog(invitationFragment.getContext());

        addAll();

    }

    public void addAll() {
        for(CreatedInvitation c : presenter.getCreatedInvitationList()) {

            if(!list.contains(c)) {
                list.add(c);
            }

        }

        for(ReceivedInvitation r : presenter.getReceivedInvitationList()) {
            if(!list.contains(r)) {
                list.add(r);
            }
        }
    }


    @Override
    public InvitationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_view_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InvitationAdapter.ViewHolder holder, int position) {

        if(list.get(position).getInvitationType() == Invitation.CREATED) {
            CreatedInvitation invitation = (CreatedInvitation) list.get(position);
            holder.title.setText(invitation.getTitle());
            holder.remainingTime.setText("Remaining Time: " + invitation.getCountDownMinute() + "min");

        } else if(list.get(position).getInvitationType() == Invitation.RECEIVED) {
            ReceivedInvitation invitation = (ReceivedInvitation) list.get(position);
            holder.title.setText(invitation.getTitle());
            holder.remainingTime.setText("Remaining Time: " + invitation.getCountDownMinute() + "min");
        }

        holder.setItemClickedListener(new InvitationClickedListener() {
            @Override
            public void onClick(View view, int position) {

                LayoutInflater inflater = invitationFragment.getLayoutInflater();
                if(list.get(position).getInvitationType() == Invitation.CREATED) {
                    View layout = inflater.inflate(R.layout.dialog_created_invitation, null);
                    dialog.setContentView(layout);
                    initializeCreatedInvitationComponent(position);

                } else if(list.get(position).getInvitationType() == Invitation.RECEIVED) {
                    Toast.makeText(view.getContext(), "Received", Toast.LENGTH_SHORT).show();
                    View layout = inflater.inflate(R.layout.dialog_received_invitation, null);
                    dialog.setContentView(layout);
                    initializeReceivedInvitationComponent(position);
                }

                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void initializeCreatedInvitationComponent(int position) {
        titleTextView = (TextView) dialog.findViewById(R.id.created_invitation_title);
        locationTextView = (TextView) dialog.findViewById(R.id.created_invitation_location);
        timeTextView = (TextView) dialog.findViewById(R.id.created_invitaiton_time);
        dateTextView = (TextView) dialog.findViewById(R.id.created_invitation_date);
        remainingTimeTextView = (TextView) dialog.findViewById(R.id.created_invitation_remaining_time);
        togetherRecycleView = (RecyclerView) dialog.findViewById(R.id.together_recycleview);
        cancelButton = (Button) dialog.findViewById(R.id.created_invitation_cancel_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        CreatedInvitation c = presenter.getCreatedInvitationFromUuid(list.get(position).getUuid());

        titleTextView.setText(c.getTitle());
        locationTextView.setText("Location : " + c.getLocation());
        timeTextView.setText("Time : " + c.getMeeting_time());
        remainingTimeTextView.setText("Remaining Time : " + c.getCountDownMinute());

        togetherRecycleView.setLayoutManager(new LinearLayoutManager(invitationFragment.getContext()));


        // need to check whether friends accepted or not
        togetherRecycleView.setAdapter(new TogetherAdapter(c.getInvitedFriends()));
    }

    public void initializeReceivedInvitationComponent(final int position) {
        titleTextView = (TextView) dialog.findViewById(R.id.received_invitation_title);
        locationTextView = (TextView) dialog.findViewById(R.id.received_invitation_location);
        timeTextView = (TextView) dialog.findViewById(R.id.received_inivitaiton_time);
        dateTextView = (TextView) dialog.findViewById(R.id.received_invitation_date);
        remainingTimeTextView = (TextView) dialog.findViewById(R.id.received_invitation_remaining_time);
        declineButton = (Button) dialog.findViewById(R.id.received_invitaiton_decline_button);
        acceptButton = (Button) dialog.findViewById(R.id.received_invitaiton_accept_button);

        final ReceivedInvitation r = presenter.getReceivedInvitationFromUuid(list.get(position).getUuid());

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child(r.getSender())
                        .child("invitations")
                        .child("created")
                        .child(r.getUuid())
                        .child("invitedFriends")
                        .child(r.getIndex()+"")
                        .child("status")
                        .setValue(ReceivedInvitation.ReceivedStatus.DECLINE);

                databaseReference.child(getFacebookUserId())
                        .child("invitations")
                        .child("received")
                        .child(list.get(position).getUuid()).removeValue();

                dialog.dismiss();
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(r.getSender())
                        .child("invitations")
                        .child("created")
                        .child(r.getUuid())
                        .child("invitedFriends")
                        .child(r.getIndex()+"")
                        .child("status")
                        .setValue(ReceivedInvitation.ReceivedStatus.ACCEPT);

                databaseReference.child(getFacebookUserId())
                        .child("invitations")
                        .child("received")
                        .child(list.get(position).getUuid()).removeValue();

                dialog.dismiss();
            }
        });

        titleTextView.setText(r.getTitle());
        locationTextView.setText("Location : " + r.getLocation());
        timeTextView.setText("Time : " + r.getMeeting_time());
        remainingTimeTextView.setText("Remaining Time : " + r.getCountDownMinute());


    }

    public String getFacebookUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String facebookUserId = "";
//      find the Facebook profile and get the user's id
        for(UserInfo profile : user.getProviderData()) {
            // check if the provider id matches "facebook.com"
            if(FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                facebookUserId = profile.getUid();
            }
        }
        return facebookUserId;
    }

    private class TogetherAdapter extends RecyclerView.Adapter<TogetherAdapter.ViewHolder>  {

        private ArrayList<Friend> invitedFriends = new ArrayList<Friend>();

        public TogetherAdapter(ArrayList<Friend> invitedFriends) {
            this.invitedFriends = invitedFriends;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView friendTextView;
            public TextView statusTextView;
            public ViewHolder(View v) {
                super(v);
                friendTextView = (TextView) v.findViewById(R.id.created_invitation_friend_name);
                statusTextView = (TextView) v.findViewById(R.id.created_invitation_status);
            }
        }

        @Override
        public TogetherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dialog_created_invitation_friend_status_item, parent, false);

            return new TogetherAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(TogetherAdapter.ViewHolder holder, int position) {
            holder.friendTextView.setText(invitedFriends.get(position).getName());

//            Log.d("Testing invited friends", invitedFriends.get(position).getStatus().toString());
            if(invitedFriends.get(position).getStatus() == ReceivedInvitation.ReceivedStatus.WAITING) {
                holder.statusTextView.setText("?");
            } else if(invitedFriends.get(position).getStatus() == ReceivedInvitation.ReceivedStatus.ACCEPT) {
                holder.statusTextView.setText("YES");
            } else if(invitedFriends.get(position).getStatus() == ReceivedInvitation.ReceivedStatus.DECLINE) {
                holder.statusTextView.setText("NO");
            }
        }

        @Override
        public int getItemCount() {
            return invitedFriends.size();
        }
    }
}
