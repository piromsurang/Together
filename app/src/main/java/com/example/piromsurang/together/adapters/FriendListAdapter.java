package com.example.piromsurang.together.adapters;

import android.content.ClipData;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.piromsurang.together.R;
import com.example.piromsurang.together.listeners.ItemClickedListener;
import com.example.piromsurang.together.models.Friend;
import com.example.piromsurang.together.models.Invitation;
import com.example.piromsurang.together.presenters.FriendPresenter;

import java.util.List;

/**
 * Created by piromsurang on 15/3/2018 AD.
 */

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {

    private FriendPresenter friendPresenter;
    private int permission;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        private ItemClickedListener itemClickedListener;
        private int permission;

        // each data item is just a string in this case
        public ViewHolder(View v, int permission) {
            super(v);
            name = v.findViewById(R.id.displayFriendName);
            itemView.setOnClickListener(this);
            this.permission = permission;
        }

        public void setItemClickedListener(ItemClickedListener itemClickedListener) {
            this.itemClickedListener = itemClickedListener;
        }

        @Override
        public void onClick(View v) {
            itemClickedListener.onClick(v, getAdapterPosition(), permission);
        }
    }

    public FriendListAdapter(FriendPresenter friendPresenter, int permission) {
        this.friendPresenter = friendPresenter;
        this.permission = permission;
    }

    @Override
    public FriendListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_recycleview_item, parent, false);

        return new FriendListAdapter.ViewHolder(itemView, permission);
    }

    @Override
    public void onBindViewHolder(FriendListAdapter.ViewHolder holder, int position) {
        Friend friend = null;
        if(permission == FriendPresenter.ON_DISPLAY_ADDED_LIST) {
            friend = friendPresenter.getAddedList().get(position);
        } else if(permission == FriendPresenter.ON_DISPLAY_SEARCHED_LIST) {
            friend = friendPresenter.getSearchedList().get(position);
        }
        holder.name.setText(friend.getName());
        holder.setItemClickedListener(new ItemClickedListener() {
            @Override
            public void onClick(View view, int position, int permission) {
                Log.d("Test", "Permission: " + permission);
                if(permission == FriendPresenter.ON_DISPLAY_ADDED_LIST) {
                    if(friendPresenter.getAddedList().size() > 0) {
                        friendPresenter.removeFromAddedList(friendPresenter.getAddedList().get(position));
                    }
                } else if(permission == FriendPresenter.ON_DISPLAY_SEARCHED_LIST) {
                    if(!friendPresenter.getAddedList().contains(friendPresenter.getSearchedList().get(position))) {
                        friendPresenter.addToAddedList(friendPresenter.getSearchedList().get(position));
                    }
                }
                friendPresenter.displayList(FriendPresenter.ON_DISPLAY_ADDED_LIST);
            }
        });
    }

    @Override
    public int getItemCount() {

        if(permission == FriendPresenter.ON_DISPLAY_ADDED_LIST) {
            return friendPresenter.getAddedList().size();
        } else if(permission == FriendPresenter.ON_DISPLAY_SEARCHED_LIST) {
            return friendPresenter.getSearchedList().size();
        } else {
            return -1;
        }
    }
}
