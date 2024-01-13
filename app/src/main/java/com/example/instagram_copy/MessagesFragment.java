package com.example.instagram_copy;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MessagesFragment extends Fragment {
    private EditText descriptionEditText;
    private Button postButton;
    private DatabaseReference postsReference;

    private ImageButton imageButtonAddPhoto;
    private ImageView imageViewSelectedPhoto;

    private LinearLayout messageRiver;

    private Button sendButton;
    private TextInputEditText messagePrompt;

    private ScrollView scrollView;

    private SharedPreferences sp;
    private String receiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        sp = requireActivity().getSharedPreferences("login", requireActivity().MODE_PRIVATE);
        postsReference = FirebaseDatabase.getInstance().getReference("messages");

        sendButton = view.findViewById(R.id.buttonSend);
        messagePrompt = view.findViewById(R.id.textPrompt);
        messageRiver = view.findViewById(R.id.messageRiver);
        scrollView = view.findViewById(R.id.scrollView);
        Bundle args = getArguments();
        receiver = args.getString("receiver");

        getAllMessages();




        //getAllMessages();
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMessage();
                getAllMessages();
                int scrollTo = messageRiver.getBottom() - scrollView.getHeight();
                scrollView.smoothScrollTo(0, scrollTo); // Use smoothScrollTo for a smooth scrolling effect
            }
        });


        return view;
    }

    private void addMessage() {
        final String message = messagePrompt.getText().toString().trim();

        if (message.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate unique post ID
        String postId = postsReference.push().getKey();

        // Get current date
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());


        //Message sentMessage = new Message(message, sp.getString("username", ""), receiver);
        Message sentMessage = new Message(message,sp.getString("username", ""), receiver);
        // Add post to the database
        postsReference.child(postId).setValue(sentMessage);



        messagePrompt.setText("");


    }

    private void getAllMessages() {
        removeAllMessages();
        // Assuming you have a DatabaseReference reference for your messages
        DatabaseReference messagesReference = FirebaseDatabase.getInstance().getReference("messages");

        messagesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Assuming you have a Message class with appropriate getters
                    Message message = snapshot.getValue(Message.class);

                    if (message != null) {



                        if((message.getSender().equals(sp.getString("username", ""))) && (message.getTarget().equals(receiver)))
                        {
                            Log.d("MessageLog:Benedek", "magic");
                            TextView temp = new TextView(getContext());
                            temp.setLayoutParams(new LinearLayout.LayoutParams(500, ViewGroup.LayoutParams.WRAP_CONTENT));
                            temp.setText(message.getMessage());
                            temp.setTextColor(Color.BLACK);
                            ((LinearLayout.LayoutParams) temp.getLayoutParams()).gravity = Gravity.END;
                            messageRiver.addView(temp);
                        } else if ((message.getSender().equals(receiver)) && (message.getTarget().equals(sp.getString("username", "")))) {
                            TextView temp = new TextView(getContext());
                            temp.setLayoutParams(new LinearLayout.LayoutParams(500, ViewGroup.LayoutParams.WRAP_CONTENT));

                            temp.setText(message.getMessage());
                            temp.setTextColor(Color.BLACK);
                            messageRiver.addView(temp);
                        }


                        //((LinearLayout.LayoutParams) temp.getLayoutParams()).gravity = Gravity.END;


                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("MessageLog", "Error retrieving messages: " + databaseError.getMessage());
            }
        });
    }
    private void removeAllMessages() {

        try {
            messageRiver.removeAllViews();
            Thread.sleep(100);
        } catch (InterruptedException e){

        }
    }


}