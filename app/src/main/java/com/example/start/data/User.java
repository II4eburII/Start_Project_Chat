package com.example.start.data;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {
    @PrimaryKey
    private int user_id = 1;
    private String user;
    private boolean isLoginned;
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
        isLoginned = true;
    }
    public void signOut(){
        user = "null";
        isLoginned = false;
    }
    public boolean getIsLoginned(){
        return isLoginned;
    }
}
