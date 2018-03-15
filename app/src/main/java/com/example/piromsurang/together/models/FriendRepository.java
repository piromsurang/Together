package com.example.piromsurang.together.models;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

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
    private ArrayList<Friend> addedList;

    private FriendRepository() {
        friendList = new ArrayList<>();
        searchedList = new ArrayList<>();
        addedList = new ArrayList<>();
    }

    public static FriendRepository getInstance() {
        if(instance == null) {
            instance = new FriendRepository();
        }
        return instance;
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

    public void addToAddedList(Friend friend) {
        addedList.add(friend);
    }

    public ArrayList<Friend> searchByName(String name) {
        clearSearchedList();
        for (Friend b : friendList) {
            if(b.getName().toLowerCase().contains(name)) {
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

    public void removeFromAddedList(Friend friend) {
        addedList.remove(friend);
    }

    public void clearSearchedList() {
        searchedList.clear();
    }

    public void clearAddedList() { addedList.clear(); }

    public ArrayList<Friend> getAddedList() {
        return addedList;
    }
}
