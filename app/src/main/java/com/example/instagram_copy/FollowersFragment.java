package com.example.instagram_copy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FollowersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View v;

    public FollowersFragment() {
        // Required empty public constructor
    }

    private ListView followersListView;

    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    private ArrayList<String> usernames = new ArrayList<>();

    DatabaseReference followingReference;

    DatabaseReference usersReference;

    private boolean following = false;

    private String username1;

    private boolean search;
    private String searchString;

    private String username2;
    public FollowersFragment(String user1, String user2, Boolean following){
        this.username1 = user1;
        this.username2 = user2;
        this.following = following;
        followingReference = FirebaseDatabase.getInstance().getReference("following11");


    }

    public FollowersFragment(String searchString, boolean search){
        this.searchString = searchString.toLowerCase();
        this.search = search;
        usersReference = FirebaseDatabase.getInstance().getReference("users");


    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FollowersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FollowersFragment newInstance(String param1, String param2) {
        FollowersFragment fragment = new FollowersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v =  inflater.inflate(R.layout.fragment_followers, container, false);
        this.v = v;

        if (search){
            searchUsers();
            return v;
        }

        Query followingQuery;
        if (following)
            followingQuery = followingReference.orderByChild("user1").equalTo(username1);//.orderByChild("user2").equalTo(username2);
        else
            followingQuery = followingReference.orderByChild("user2").equalTo(username1);//.orderByChild("user2").equalTo(username2);

        //usernames = new ArrayList<String>();
        followingQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usernames = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot d : snapshot.getChildren()) {
                        Following f = d.getValue(Following.class);
                            if (f.getFollowing()) {
                                if (following)
                                    usernames.add(f.getUser2());
                                else
                                    usernames.add(f.getUser1());
                            }
                    }
                }
                setupFollowers();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return v;
    }

    private void searchUsers() {
        usernames = new ArrayList<String>();
        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usernames = new ArrayList<>();
                    for (DataSnapshot d : snapshot.getChildren()) {
                        User u = d.getValue(User.class);
                        if (u.getUsername().toLowerCase().contains(searchString)) {
                            usernames.add(u.getUsername());
                            }
                    }

                setupFollowers();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setupFollowers(){
        if (usernames.size() == 0){
            TextView tv = v.findViewById(R.id.no_users_tv);
            tv.setVisibility(View.VISIBLE);
            return;
        }

        ListView listView = (ListView) v.findViewById(R.id.listViewFollowers);
        FollowerAdapter adapter = new FollowerAdapter(requireContext(), usernames,false);
        // set adapter for ListView
        listView.setAdapter(adapter);

    }
}