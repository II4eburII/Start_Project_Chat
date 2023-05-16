package com.example.start.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.start.data.Message;
import com.example.start.R;
import com.example.start.data.Post;
import com.example.start.databinding.ActivityMainBinding;
import com.example.start.email.EmailPasswordActivity;
import com.example.start.network.Network;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.text.DateFormat;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;


public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel viewModel;
    private ActivityMainBinding binding;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        binding.recyclerView.setAdapter(viewModel.getAdapter());
        checkCurrentUser();
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnSend:
                        if (!binding.message.getText().toString().trim().isEmpty()) {
                            viewModel.addMessage(
                                    new Message("Марк",
                                            binding.message.getText().toString(),
                                            System.currentTimeMillis() - TimeZone.getDefault().getOffset(System.currentTimeMillis()),
                                            !binding.authorname.getText().toString().isEmpty(), binding.authorname.getText().toString().isEmpty() ? "0" : "1", binding.authorname.getText().toString().isEmpty() ? "1" : "0"));
                            binding.message.getText().clear();
                        }
                    case R.id.btnBackToMenu:
                        FirebaseAuth.getInstance().signOut();
                }
            }
        });



            //foreground
        }
    public void checkCurrentUser() {
        // [START check_current_user]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in

        } else {
            // No user is signed in
            Intent register = new Intent(MainActivity.this, Registration.class);
            startActivity(register);
            finish();
        }
        // [END check_current_user]
    }

    public void sendEmailVerification() {
        // [START send_email_verification]
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("MyApp", "Email sent.");
                        }
                    }
                });
        // [END send_email_verification]
    }

    public void deleteUser() {
        // [START delete_user]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("MyApp", "User account deleted.");
                        }
                    }
                });
        // [END delete_users]
    }

    }