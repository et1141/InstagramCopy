package com.example.instagram_copy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PostAdapter extends ArrayAdapter<Post> {
    private Context context;
    private ArrayList<Post> posts;

    public PostAdapter(Context context, ArrayList<Post> posts) {
        super(context, 0, posts);
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        }

        Post currentPost = getItem(position);

        TextView usernameTextView = listItemView.findViewById(R.id.post_username);
        TextView descriptionTextView = listItemView.findViewById(R.id.post_description);
        TextView dateTextView = listItemView.findViewById(R.id.post_date);

        usernameTextView.setText(currentPost.getUsername());
        descriptionTextView.setText(currentPost.getDescription());
        dateTextView.setText(currentPost.getDate());

        return listItemView;
    }
}
