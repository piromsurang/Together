package com.example.piromsurang.together.models;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by piromsurang on 15/3/2018 AD.
 */

public class FriendRepository {

    private static FriendRepository instance = null;
    private ArrayList<Friend> friendList;
    private ArrayList<Friend> searchedList;
    private Context context;

    private FriendRepository() {
        friendList = new ArrayList<>();
        searchedList = new ArrayList<>();
    }

    public static FriendRepository getInstance() {
        if(instance == null) {
            instance = new FriendRepository();
        }
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Friend> getFriendList() {
        return friendList;
    }

    public ArrayList<Friend> getSearchedList() {
        return searchedList;
    }

    public void addFriend(Friend friend) {
        friendList.add(friend);
    }

    public Friend getFriendByName(String name) {
        for(Friend f : friendList) {
            if(f.getName().equals(name)) {
                return f;
            }
        }
        return new Friend("", "");
    }

    public ArrayList<Friend> searchByName(String name) {
        searchedList.clear();
        for (Friend b : friendList) {
            if( b.getName().contains(name)) {
                searchedList.add(b);
            }
        }

        Collections.sort(searchedList, new Comparator<Friend>() {
            @Override
            public int compare(Friend one, Friend other) {
                return one.getName().compareTo(other.getName());
            }
        });

        return searchedList;
    }

}
