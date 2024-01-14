package com.example.instagram_copy;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AddPostFragment extends Fragment {

    private EditText descriptionEditText;
    private Button postButton, chooseButton, uploadButton;
    private DatabaseReference postsReference;


    private ImageView imageViewSelectedPhoto;


    private SharedPreferences sp;

    //geeksforgeeks varriables:
    private final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;
    long milliseconds;
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference postImagesRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);

        descriptionEditText = view.findViewById(R.id.editTextDescription);
        postButton = view.findViewById(R.id.buttonPost);

        chooseButton = view.findViewById(R.id.buttonChoose);
        uploadButton = view.findViewById(R.id.buttonUpload);
        imageViewSelectedPhoto = view.findViewById(R.id.imageViewSelectedPhoto);
        sp = requireActivity().getSharedPreferences("login", requireActivity().MODE_PRIVATE);


        postsReference = FirebaseDatabase.getInstance().getReference("posts");

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        long milliseconds = new Date().getTime();

        StorageReference postImagesRef = storageRef.child("images/" + milliseconds+".jpg");


        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost();
            }
        });
        chooseButton.setOnClickListener(new View.OnClickListener() { //TODO
            @Override
            public void onClick(View v) {
                SelectImage();
            }

        });
        uploadButton.setOnClickListener(new View.OnClickListener() { //TODO
            @Override

            public void onClick(View v) {

                if(imageViewSelectedPhoto.getDrawable() != null){
                    uploadImage();
                }

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

        //generate unique post ID
        String postId = postsReference.push().getKey();

        //recently we don't need Uri anyway. We use firebase storage where image is saved as "miliseconds.jpg"
        final Post post = new Post(sp.getString("username", ""), description, "https://images.pexels.com/photos/1108099/pexels-photo-1108099.jpeg?auto=compress&cs=tinysrgb&w", milliseconds);

        // Add post to the database
        postsReference.child(postId).setValue(post);

        Toast.makeText(requireContext(), "Post added successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void SelectImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        //then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {
                //set image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                requireActivity().getContentResolver(),
                                filePath);
                imageViewSelectedPhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        imageViewSelectedPhoto.setDrawingCacheEnabled(true);
        imageViewSelectedPhoto.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageViewSelectedPhoto.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = postImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(requireActivity().getApplicationContext(), "Failed to upload image",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(requireActivity().getApplicationContext(), "Uploaded image",Toast.LENGTH_SHORT).show();

                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
    }

}
