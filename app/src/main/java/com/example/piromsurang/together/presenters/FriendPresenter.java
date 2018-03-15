package com.example.piromsurang.together.presenters;

import com.example.piromsurang.together.models.Friend;
import com.example.piromsurang.together.models.FriendRepository;
import com.example.piromsurang.together.views.FriendView;

import java.util.ArrayList;

/**
 * Created by piromsurang on 15/3/2018 AD.
 */

public class FriendPresenter {

    private FriendRepository friendRepository;
    private FriendView friendView;
    public static int ON_DISPLAY_SEARCHED_LIST = 1;
    public static int ON_DISPLAY_ADDED_LIST = 2;

    public FriendPresenter(FriendRepository friendRepository, FriendView friendView) {
        this.friendRepository = friendRepository;
        this.friendView = friendView;
    }

    public void displayList(int permission) {
        friendView.displayeFriendList(permission);
    }

    public ArrayList<Friend> searchByName(String name) {
        return friendRepository.searchByName(name);
    }

    public void removeFromAddedList(Friend friend) {
        friendRepository.removeFromAddedList(friend);
    }

    public Friend getFriend(String name) {
        return friendRepository.getFriendByName(name);
    }

    public ArrayList<Friend> getFriendList() { return friendRepository.getFriendList(); }

    public ArrayList<Friend> getSearchedList() { return friendRepository.getSearchedList(); }

    public ArrayList<Friend> getAddedList() { return friendRepository.getAddedList(); }

    public void addFriend(Friend friend) {
        friendRepository.addFriend(friend);
    }

    public void addToAddedList(Friend friend) {
        friendRepository.addToAddedList(friend);
    }
}
