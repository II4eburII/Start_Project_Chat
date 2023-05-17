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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Login extends AppCompatActivity  {
    EditText emailAdress;
    EditText Password;
    Button send;
    Button toRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        emailAdress = findViewById(R.id.emailaddress);
        Password = findViewById(R.id.password);
        send = findViewById(R.id.btnLogin);
        toRegister = findViewById(R.id.btnToRegister);
        FirebaseFirestore firebaseFirestore;
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        if (!emailAdress.getText().toString().trim().isEmpty() &&
                                !Password.getText().toString().trim().isEmpty()) {
                            FirebaseFirestore.getInstance().collection("client").document(emailAdress.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.getResult().exists()) {
                                        SignIn();
                                    } else {
                                        Log.d("MyApp", "Never was registered");
                                        Toast.makeText(getApplicationContext(), "Never was registered", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
            }
        });
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(Login.this, Registration.class);
                startActivity(register);
                finish();
            }
        });
    }
    private void SignIn(){
        FirebaseFirestore.getInstance().collection("client").document(emailAdress.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    if (task.getResult().getData().values().toArray()[6].toString().equals(Password.getText().toString())){
                        Log.d("MyApp", "Successfully login");
                        Toast.makeText(getApplicationContext(), "Successfull login", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailAdress.getText().toString(), Password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful())
                                {
                                    Log.d("MyApp", "Successfully login in Firebase");
                                    Toast.makeText(getApplicationContext(), "Successfully login in Firebase", Toast.LENGTH_SHORT).show();
                                    Intent login = new Intent(Login.this, MainActivity.class);
                                    startActivity(login);
                                    finish();
                                } else {
                                    Log.d("MyApp", "Failure login in Firebase");
                                    Toast.makeText(getApplicationContext(), "Failure login in Firebase", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Log.d("MyApp", "Failure login");
                        Toast.makeText(getApplicationContext(), "Failure login", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Whoops", Toast.LENGTH_SHORT).show();
                    Log.d("MyApp", "Task in login was failure");
                }
            }
        });
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
