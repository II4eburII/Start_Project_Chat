package com.example.start.data;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;

import io.realm.RealmObject;

public class User extends RealmObject {
    private boolean isLoginned = false;
    private String user;
    public User(String user){
        this.user = user;
        isLoginned = true;
    }
    public User(){
    }
    public String getUser(){
        return user;
    }
    public void setUser(String user){
        this.user = user;
    }
    public boolean getIsLoginned(){
        return isLoginned;
    }
}
