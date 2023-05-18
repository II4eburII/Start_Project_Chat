package com.example.start;

import android.app.Application;

import com.example.start.db.RealmDatabase;

import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApp extends Application {
    private Map<String, Object> User;
    private boolean isLoginned;
    private RealmDatabase mDatabase;
    @Override
    public void onCreate() {
        super.onCreate();
        isLoginned = (User == null? false: true);

        Realm.init(this);

        mDatabase = new RealmDatabase(
                new  RealmConfiguration.Builder().
                        name(Realm.DEFAULT_REALM_NAME).
                        schemaVersion(0).
                        allowWritesOnUiThread(true).
                        deleteRealmIfMigrationNeeded().
                        build());
    }

    public Map<String, Object> getUser() {
        return User;
    }

    public void setUser(Map<String, Object> User) {
        this.User = User;
        this.isLoginned = true;
    }
    public boolean getIsLoginned(){
        return isLoginned;
    }
    public void SignOut(){
        this.User = null;
        this.isLoginned = false;
    }
    public RealmDatabase getDatabase(){
        return mDatabase;
    }
}
