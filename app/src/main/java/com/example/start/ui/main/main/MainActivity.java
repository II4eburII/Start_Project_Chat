package com.example.start.ui.main.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.start.MyApp;
import com.example.start.data.Message;
import com.example.start.R;
import com.example.start.databinding.ActivityMainBinding;
import com.example.start.ui.main.login.RegistrationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.TimeZone;


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
                        if (!binding.message.getText().toString().trim().isEmpty()) {
                            viewModel.addMessage(
                                    new Message("Марк",
                                            binding.message.getText().toString(),
                                            System.currentTimeMillis() - TimeZone.getDefault().getOffset(System.currentTimeMillis()),
                                            !binding.authorname.getText().toString().isEmpty(), binding.authorname.getText().toString().isEmpty() ? "0" : "1", binding.authorname.getText().toString().isEmpty() ? "1" : "0"));
                            binding.message.getText().clear();
                        }
            }
        });
        binding.btnBackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApp app = ((MyApp) getApplicationContext());
                app.signOut();
            }
        });
        }
    public void checkCurrentUser() {
        MyApp app = ((MyApp) getApplicationContext());
        Log.d("MyApp", app.getUser() + " ");
        if (app.checkUser() != "null") { /*app.getIsLoginned()*/
            Log.d("MyApp", app.getUser() + "login");
        } else {
            Intent register = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(register);
            finish();
        }
    }

    public void sendEmailVerification() {
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
    }

    public void deleteUser() {
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
    }
}