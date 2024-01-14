package com.example.instagram_copy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText username_EditTxt, password_EditTxt;
    Button login_Button, registerRedirect_Button;
    FirebaseDatabase database;
    DatabaseReference reference;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sp = getSharedPreferences("login", MODE_PRIVATE);

        if (sp.getBoolean("logged", false)) {
            switch_MainView();
        }

        username_EditTxt = findViewById(R.id.editTextUsername);
        password_EditTxt = findViewById(R.id.editTextPassword);
        login_Button = findViewById(R.id.buttonLogin);
        registerRedirect_Button = findViewById(R.id.buttonRegisterRedirect);

        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_username() && check_password()){
                    checkUser();
                }
            }
        });
        registerRedirect_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void switch_MainView() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
    public Boolean check_username(){
        String val = username_EditTxt.getText().toString();
        if (val.isEmpty()) {
            username_EditTxt.setError("Username cannot be empty");
            return false;
        } else {
            password_EditTxt.setError(null);
            return true;
        }
    }
    public Boolean check_password(){
        String val = password_EditTxt.getText().toString();
        if (val.isEmpty()) {
            password_EditTxt.setError("Password cannot be empty");
            return false;
        } else {
            password_EditTxt.setError(null);
            return true;
        }
    }
    public void checkUser() {
        String username = username_EditTxt.getText().toString().trim();
        String password = password_EditTxt.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(username);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    username_EditTxt.setError(null);
                    String passwordFromDB = snapshot.child(username).child("password").getValue(String.class);
                    if (passwordFromDB.equals(password)) {
                        username_EditTxt.setError(null);
                        sp.edit().putBoolean("logged", true).apply();
                        sp.edit().putString("username",username).apply();
//                        String posts = snapshot.child(username).child("posts").getValue(Integer.class).toString();
//                        String followers = snapshot.child(username).child("followers").getValue(Integer.class).toString();
//                        String following = snapshot.child(username).child("following").getValue(Integer.class).toString();
//                        sp.edit().putString("posts", posts).apply();
//                        sp.edit().putString("followers", followers).apply();
//                        sp.edit().putString("following", following).apply();
                        sp.edit().commit();

                        switch_MainView();
                    } else {
                        password_EditTxt.setError("Invalid Credentials");
                        password_EditTxt.requestFocus();
                    }
                } else {
                    username_EditTxt.setError("User does not exist");
                    username_EditTxt.requestFocus();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}