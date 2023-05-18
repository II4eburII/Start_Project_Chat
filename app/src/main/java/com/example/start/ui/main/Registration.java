package com.example.start.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.start.R;
import com.example.start.ui.main.login.LoginActivity;
import com.example.start.ui.main.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Registration extends AppCompatActivity  {
    EditText emailAdress;
    EditText firstPassword;
    EditText secondPassword;
    EditText name;
    Button send;
    Button toLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        emailAdress = findViewById(R.id.emailaddress);
        firstPassword = findViewById(R.id.firstpassword);
        secondPassword = findViewById(R.id.secondpassword);
        name = findViewById(R.id.name);
        send = findViewById(R.id.btnRegister);
        toLogin = findViewById(R.id.btnToLogin);
        FirebaseFirestore firebaseFirestore;
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        if (!emailAdress.getText().toString().trim().isEmpty() &&
                             !firstPassword.getText().toString().trim().isEmpty() &&
                              !secondPassword.getText().toString().trim().isEmpty() &&
                               !name.getText().toString().trim().isEmpty() &&
                                firstPassword.getText().toString().equals(secondPassword.getText().toString())) {

                                FirebaseFirestore.getInstance().collection("client").document(emailAdress.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.getResult().exists()) {
                                            Log.d("MyApp", "Already registered");
                                            Toast.makeText(getApplicationContext(), "Already registered", Toast.LENGTH_SHORT).show();
                                        } else {
                                            addNewUser();
                                            Toast.makeText(getApplicationContext(), "Successfully registered", Toast.LENGTH_SHORT).show();
                                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailAdress.getText().toString(), firstPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(getApplicationContext(), "Successfully registered in Firebase", Toast.LENGTH_SHORT).show();

                                                        Intent login = new Intent(Registration.this, MainActivity.class);
                                                        startActivity(login);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Failure registered in Firebase", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                        }
            }
        });
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(Registration.this, LoginActivity.class);
                startActivity(login);
                finish();

            }
        });
    }
    private void addNewUser(){
        Map<String, String> reg_entry = new HashMap<>();
        reg_entry.put("Email", emailAdress.getText().toString());
        reg_entry.put("Password", firstPassword.getText().toString());
        reg_entry.put("Name", name.getText().toString());
        reg_entry.put("TimeRegistration", String.valueOf(System.currentTimeMillis()));
        reg_entry.put("Id", UUID.randomUUID().toString());
        reg_entry.put("MobNumber", "Soon");
        reg_entry.put("Is2FaOn", "false");
        FirebaseFirestore.getInstance().collection("client").document(emailAdress.getText().toString()).set(reg_entry);
    }
    private Runnable doBackgroundThreadProcessing = new Runnable() {
        public void run() {
            sendEmailVerification();
        }
    };
    private void sendEmailVerification() {
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
    }
}
