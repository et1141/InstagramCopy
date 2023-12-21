package com.example.instagram_copy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddPostFragment extends Fragment {

    private EditText descriptionEditText;
    private Button postButton;
    private DatabaseReference postsReference;

    private ImageButton imageButtonAddPhoto;
    private ImageView imageViewSelectedPhoto;

    private SharedPreferences sp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);

        descriptionEditText = view.findViewById(R.id.editTextDescription);
        postButton = view.findViewById(R.id.buttonPost);
        imageButtonAddPhoto = view.findViewById(R.id.imageButtonAddPhoto);
        imageViewSelectedPhoto = view.findViewById(R.id.imageViewSelectedPhoto);


        sp = requireActivity().getSharedPreferences("login", requireActivity().MODE_PRIVATE);
        postsReference = FirebaseDatabase.getInstance().getReference("posts");

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost();
            }
        });
        imageButtonAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //TODO   choosePhoto();
            }
        });
        return view;
    }

    private void addPost() {
        final String description = descriptionEditText.getText().toString().trim();

        if (description.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter a description", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate unique post ID
        String postId = postsReference.push().getKey();

        // Get current date
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        final Post post = new Post(sp.getString("username", ""), description, "https://images.pexels.com/photos/1108099/pexels-photo-1108099.jpeg?auto=compress&cs=tinysrgb&w=600", currentDate);

        // Add post to the database
        postsReference.child(postId).setValue(post);

        Toast.makeText(requireContext(), "Post added successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }


}
