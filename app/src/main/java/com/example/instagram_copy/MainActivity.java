package com.example.instagram_copy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
    ImageButton profile_button,addPost_button, messages_button;
    ListView posts_listView;
    SharedPreferences sp;

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
            search_editText = findViewById(R.id.editTextSearch);
            profile_button = findViewById(R.id.buttonAddPost);
            addPost_button = findViewById(R.id.buttonAddPost);
            messages_button = findViewById(R.id.buttonMessages);

            posts_listView = findViewById(R.id.listViewPosts);

            String[] posts = {
                    "Post 1: User1 - Time1 - Description1",
                    "Post 2: User2 - Time2 - Description2",
                    "Post 3: User3 - Time3 - Description3",
                    "Post 4: User4 - Time4 - Description4",
                    "Post 5: User5 - Time5 - Description5",
                    "Post 6: User6 - Time6 - Description6",
                    "Post 3: User3 - Time3 - Description3",
                    "Post 4: User4 - Time4 - Description4",
                    "Post 5: User5 - Time5 - Description5",
                    "Post 6: User6 - Time6 - Description6",
                    "Post 3: User3 - Time3 - Description3",
                    "Post 4: User4 - Time4 - Description4",
                    "Post 5: User5 - Time5 - Description5",
                    "Post 6: User6 - Time6 - Description6"
            };

            // Utwórz adapter dla listy postów
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, posts);
            // Ustaw adapter dla ListView
            posts_listView.setAdapter(adapter);

            addPost_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getSupportFragmentManager().beginTransaction().add(
                                    R.id.mainActivity_layout, new AddPost()).
                            commit();
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
