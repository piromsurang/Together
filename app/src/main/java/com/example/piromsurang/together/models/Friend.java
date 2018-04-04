package com.example.piromsurang.together.models;

/**
 * Created by piromsurang on 15/3/2018 AD.
 */

public class Friend {

    private String id;
    private String name;

    public Friend(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Friend() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
