package com.example.instagram_copy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class FollowerAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> followers;

    private boolean own = false;


    public FollowerAdapter(Context context, ArrayList<String> users, boolean own) {
        super(context, 0, users);
        this.context = context;
        this.followers = users;
        this.own = own;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.follower_item, parent, false);
        }

        String currentUser = getItem(position);

        TextView usernameTextView = listItemView.findViewById(R.id.follower_username);
        usernameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity ma = (MainActivity) context;
                ma.showUserProfile(currentUser);
            }
        });
        usernameTextView.setText(currentUser);


        return listItemView;
    }

}

