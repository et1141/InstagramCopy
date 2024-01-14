package com.example.instagram_copy;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class messageList extends Fragment {


    private LinearLayout messagesLinearLayout;
    private DatabaseReference postsReference;
    private SharedPreferences sp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_list, container, false);
        // Inflate the layout for this fragment
        sp = requireActivity().getSharedPreferences("login", requireActivity().MODE_PRIVATE);
        messagesLinearLayout = view.findViewById(R.id.messagesLinearLayout);

        fillLayout();


        return view;

    }

    private void fillLayout() {
        ArrayList<String> perpetraitors = new ArrayList<>();
        DatabaseReference messagesReference = FirebaseDatabase.getInstance().getReference("messages");
        messagesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Assuming you have a Message class with appropriate getters
                    Message message = snapshot.getValue(Message.class);

                    if (message != null && sp.getString("username", "") != null) {



                        if(((message.getSender().equals(sp.getString("username", ""))) ||
                                (message.getTarget().equals(sp.getString("username", "")))) &&
                                !perpetraitors.contains(message.getTarget())
                        )
                        {
                            String temp = message.getTarget();
                            perpetraitors.add(temp);
                            Log.i("perps", temp);
                            addNameToLayout(temp);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("MessageLog", "Error retrieving messages: " + databaseError.getMessage());
            }
        });

    }
    private void addNameToLayout(String name){
        String receiver = name;
        Button button = new Button(getContext());
        button.setText(receiver );
        button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();

                MessagesFragment messagesFragment = new MessagesFragment();
                Bundle args = new Bundle();
                args.putString("receiver", receiver);
                messagesFragment.setArguments(args);
                fragmentTransaction.replace(getId(), messagesFragment);

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        messagesLinearLayout.addView(button);
    }
}
