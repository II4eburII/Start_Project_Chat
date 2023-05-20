package com.example.start;

import android.app.Application;

import com.example.start.data.User;
import com.example.start.db.RealmDatabase;

import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApp extends Application {
    private RealmDatabase mDatabase;
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        mDatabase = new RealmDatabase(
                new  RealmConfiguration.Builder().
                        name(Realm.DEFAULT_REALM_NAME).
                        schemaVersion(0).
                        allowWritesOnUiThread(true).
                        deleteRealmIfMigrationNeeded().
                        build());
    }

    public User getUser() {
        return mDatabase.getUserDB();
    }

    public void setUser(com.example.start.data.User user) {
        mDatabase.setUserDB(user);
    }
    public void signOut(){
        mDatabase.signOut();
    }
    public RealmDatabase getDatabase(){
        return mDatabase;
    }
    public String checkUser(){return mDatabase.checkUser();}
}
