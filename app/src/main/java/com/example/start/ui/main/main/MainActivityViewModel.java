package com.example.start.ui.main.main;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.start.MyApp;
import com.example.start.data.Message;
import com.example.start.data.Post;
import com.example.start.data.User;
import com.example.start.db.RealmDatabase;
import com.example.start.network.Network;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.mongodb.App;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends AndroidViewModel {
    private final RealmDatabase mDatabase;
    private MessagesAdapter mAdapter;
    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        mDatabase = ((MyApp)application).getDatabase();

        mAdapter = new MessagesAdapter(mDatabase.getMessages("time"));

        Network.getInstance().getMessageApi().getMessages().enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {

                if(!response.isSuccessful()) {
                    Log.d("ViewModel", "bad response");
                    return;
                }
                for(Message msg: response.body()){
                    Log.d("ViewModel", msg.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Log.d("ViewModel", "Fail, Error:" + t);
            }
        });


        //mDatabase.clear(Message.class);
    }
    public MessagesAdapter getAdapter() {
        return mAdapter;
    }
    public void addMessage(Message message){
        mDatabase.insert(message);
        Network.getInstance().getMessageApi().postMessage(message).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });

    }
    public void deleteMessage(Message message){
        //mDatabase.deleteById(message.getId());
    }
    public Message getContextClickOperation(){
        return mAdapter.getContextClickOperation();
    }
    public User getUser(){
        return mDatabase.getUser();
    }
}