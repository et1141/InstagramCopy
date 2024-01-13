package com.example.instagram_copy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText email_EditTxt, fullName_EditTxt, username_EditTxt, password_EditTxt;

    Button register_Button;
    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        email_EditTxt= findViewById(R.id.editTextUsername);
        fullName_EditTxt= findViewById(R.id.editTextFullName);
        username_EditTxt= findViewById(R.id.editTextUsername);
        password_EditTxt= findViewById(R.id.editTextPassword);
        register_Button = findViewById(R.id.buttonSignUp);

        register_Button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
              database = FirebaseDatabase.getInstance();
              reference = database.getReference("users");

              String email = email_EditTxt.getText().toString();
              String fullName = fullName_EditTxt.getText().toString();
              String username = username_EditTxt.getText().toString();
              String password = password_EditTxt.getText().toString();

                writeNewUser(email,username,fullName,password);


                Toast.makeText(RegisterActivity.this, "You have register successfully!",Toast.LENGTH_SHORT).show();
              //intent to redicrect user to loginActivity
              Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
              startActivity(intent);
            }
        });
    }
    public void writeNewUser(String email, String username, String fullName, String password) {
        User user = new User(email, username, fullName, password);
        reference.child(username).setValue(user);
    }
}