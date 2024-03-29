package com.example.instagram_copy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
                    .replace(R.id.mainActivity_fragments_layout, new PostsFragment(sp.getString("username", "default"), true))
                    .commit();

            search_editText = findViewById(R.id.editTextSearch);
            profile_button = findViewById(R.id.buttonProfile);
            addPost_button = findViewById(R.id.buttonAddPost);
            messages_button = findViewById(R.id.buttonMessages);
            home_button = findViewById(R.id.buttonHome);
            TextView button_search = findViewById(R.id.buttonSearch);

            profile_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    search_editText.setText("");
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainActivity_fragments_layout, new UserFragment(sp, sp.getString("username", ""))).commit();
                }
            });

            home_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    search_editText.setText("");
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainActivity_fragments_layout, new PostsFragment(sp.getString("username", "default"), true))
                            .commit();
                }
            });

            messages_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    search_editText.setText("");
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainActivity_fragments_layout, new messageList())
                            .commit();

                }
            });

            button_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String searchFr = search_editText.getText().toString();
                    if (searchFr.equals("")){
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.mainActivity_fragments_layout, new PostsFragment(sp.getString("username", "default"), true))
//                                .commit();
                    } else {
                        searchForUsers(searchFr);

                    }
                }
            });

//            search_editText.addTextChangedListener(new TextWatcher() {
//                String text;
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                    String searchFr = search_editText.getText().toString();
//                    if (searchFr.equals("")){
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.mainActivity_fragments_layout, new PostsFragment(sp.getString("username", "default"), true))
//                                .commit();
//                    } else {
//                        searchForUsers(searchFr);
//
//                    }
//
//                }
//            });



            addPost_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    search_editText.setText("");
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainActivity_fragments_layout, addPostFragment)
                            .commit();
                }
            });
        }
    }

    private void searchForUsers(String search) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainActivity_fragments_layout, new FollowersFragment(search, true)).commit();

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

    public void showUserProfile(String username){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainActivity_fragments_layout, new UserFragment(sp, username)).commit();

    }

}
