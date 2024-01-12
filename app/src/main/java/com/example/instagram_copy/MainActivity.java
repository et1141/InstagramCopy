package com.example.instagram_copy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    EditText search_editText;
    ImageButton profile_button,addPost_button, messages_button,home_button;
    SharedPreferences sp;
    private AddPostFragment addPostFragment = new AddPostFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("login", MODE_PRIVATE);

        if(!sp.getBoolean("logged",false)){
            //redirect to LoginActivity
            switch_loginView();
        }
        else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainActivity_fragments_layout, new PostsFragment())
                    .commit();

            search_editText = findViewById(R.id.editTextSearch);
            profile_button = findViewById(R.id.buttonProfile);
            addPost_button = findViewById(R.id.buttonAddPost);
            messages_button = findViewById(R.id.buttonMessages);
            home_button = findViewById(R.id.buttonHome);

            profile_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainActivity_fragments_layout, new PostsFragment(sp.getString("username", "")))
                            .commit();
                }
            });

            home_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainActivity_fragments_layout, new PostsFragment())
                            .commit();
                }
            });

            addPost_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainActivity_fragments_layout, addPostFragment)
                            .commit();
                }
            });
        }
    }

    public void log_out(View view) {
        sp.edit().putBoolean("logged", false).apply();
        sp.edit().commit();
        switch_loginView();
    }

    private void switch_loginView(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}
