package com.example.start.db;
import androidx.annotation.NonNull;

import com.example.start.data.Message;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmResults;

public class RealmDatabase {
    private final RealmConfiguration mRealmConfiguration;
    private final Realm mRealm;

    public RealmDatabase(RealmConfiguration realmConfiguration){
        mRealmConfiguration = realmConfiguration;
        mRealm = Realm.getInstance(mRealmConfiguration);
    }
    public void insert(Message message){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.copyToRealm(message);
            }
        });
    }
    public RealmResults<Message> getMessages(String sortField){
        return mRealm.where(Message.class).sort(sortField).findAll();
    }

    public void deleteById(long id){
        Message message = mRealm.where(Message.class).equalTo("id", id).findFirst();
        if (message == null)
            return;
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                message.deleteFromRealm();
            }
        });
    }
    public void clear(Class<? extends RealmModel> table){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.delete(table);
            }
        });
    }
    public long getMessageNextId(){
        Number number = mRealm.where(Message.class).findAll().max("id");
        if (number == null)
            return 0;
        return number.longValue() + 1;
    }

}