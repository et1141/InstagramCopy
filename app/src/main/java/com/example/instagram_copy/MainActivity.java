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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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


            profile_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainActivity_fragments_layout, new UserFragment(sp, sp.getString("username", ""))).commit();
                }
            });

            home_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainActivity_fragments_layout, new PostsFragment(sp.getString("username", "default"), true))
                            .commit();
                }
            });

            messages_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainActivity_fragments_layout, new MessagesFragment())
                            .commit();

                }
            });

            search_editText.addTextChangedListener(new TextWatcher() {
                String text;
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    String searchFr = search_editText.getText().toString();
                    if (searchFr.equals("")){
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.mainActivity_fragments_layout, new PostsFragment(sp.getString("username", "default"), true))
                                .commit();
                    } else {
                        Log.i("Stringy string searching for", searchFr);
                        searchForUsers(searchFr);

                    }

                }
            });

            /*search_editText.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // If the event is a key-down event on the "enter" button
                    Log.i("Pritisnuto nesto", search_editText.getText().toString());
                    // no posts to show
                    String search2 = search_editText.getText().toString();

                    Log.i("MMMMMMMMMMMMMsearched", search2);
                    if ((event.getAction() == KeyEvent.ACTION_DOWN)
                            //&& (keyCode == KeyEvent.KEYCODE_ENTER)
                    ) {
                        // Perform action on key press
                        if (search2.equals("")){
                            Log.i("PRazan string", "???");

                        }
                        else {
                            String searchFr = search_editText.getText().toString();
                            Log.i("Stringy string za pretrsge", searchFr);
                            searchForUsers(searchFr);

                        }
                        //searchForUsers(search_editText.getText().toString());

                        //Toast.makeText(HelloFormStuff.this, edittext.getText(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    return false;
                }
            });*/

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
