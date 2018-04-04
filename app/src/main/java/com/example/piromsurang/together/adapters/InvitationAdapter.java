package com.example.piromsurang.together.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.piromsurang.together.R;
import com.example.piromsurang.together.models.CreatedInvitation;
import com.example.piromsurang.together.models.Invitation;
import com.example.piromsurang.together.models.ReceivedInvitation;
import com.example.piromsurang.together.presenters.InvitationPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piromsurang on 12/3/2018 AD.
 */

public class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.ViewHolder> {

    private InvitationPresenter presenter;
    private List<Invitation> list;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ProgressBar progressBar;

        // each data item is just a string in this case
        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.invitation_title);
            progressBar = v.findViewById(R.id.countdown_progressbar);
        }
    }

    public InvitationAdapter(InvitationPresenter presenter) {
        this.presenter = presenter;
        this.list = new ArrayList<>();
        addAll();

    }

    public void addAll() {
        for(CreatedInvitation c : presenter.getCreatedInvitationList()) {
            list.add(c);
        }

        for(ReceivedInvitation r : presenter.getReceivedInvitationList()) {
            list.add(r);
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
        } else if(list.get(position).getInvitationType() == Invitation.RECEIVED) {
            ReceivedInvitation invitation = (ReceivedInvitation) list.get(position);
            holder.title.setText(invitation.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
