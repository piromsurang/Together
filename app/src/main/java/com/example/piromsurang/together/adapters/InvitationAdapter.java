package com.example.piromsurang.together.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.piromsurang.together.R;
import com.example.piromsurang.together.models.Invitation;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by piromsurang on 12/3/2018 AD.
 */

public class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.ViewHolder> {

    private List<Invitation> invitationList;

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

    public InvitationAdapter(List<Invitation> moviesList) {
        this.invitationList = moviesList;
    }

    @Override
    public InvitationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_view_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InvitationAdapter.ViewHolder holder, int position) {
        Invitation invitation = invitationList.get(position);
        holder.title.setText(invitation.getTitle());
    }

    @Override
    public int getItemCount() {
        return invitationList.size();
    }
}
