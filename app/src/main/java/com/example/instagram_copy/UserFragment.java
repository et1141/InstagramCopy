package com.example.instagram_copy;

import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private int followingStatus = 0;

    private int followingStatus2 = 0;

    private Boolean showPosts = false;

    private String followId1;

    private String followId2;

    private String username;
    DatabaseReference followingReference;

    private DatabaseReference postsReference;

    private SharedPreferences sp;
    public UserFragment() {
        // Required empty public constructor
    }

    public UserFragment (SharedPreferences sp, String username) {
        // set posts
        this.sp = sp;
        this.username = username;
        postsReference = FirebaseDatabase.getInstance().getReference().child("posts");//.orderByChild("username").equalTo(username);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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

    // prikaz svojih postova

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        this.view = view;
        // remove this
        TextView tview = (TextView) view.findViewById(R.id.username_text_view);
        String txt = this.sp.getString("username", "default");
        //txt = txt + "showing " + this.username;
        txt = this.username;
        tview.setText(txt);

//        TextView tview2 = (TextView) view.findViewById(R.id.us2);
//        String txt2 = "Showing profile in: " + username;
//        tview2.setText(txt2);

        // why is getfragmentmanager deprecated?
        // display fragments only if user is following user

        // username2 is the username that is being clicked on
        // and logged in user is in sp
        // dummy username2 below just for testing
        // TODO remove these dummies
        String username1 = sp.getString("username", "default"); // for testing only
        String username2 = username;
        //username2 = "benedek";
        Log.i("Isuse boze1", username1);
        Log.i("Isuse boze2", username2);

        followingReference = FirebaseDatabase.getInstance().getReference("following11");

        displayAllDataFromDatabase();


        TextView tv = view.findViewById(R.id.following);
        //String tmp = sp.getString("following", "") + " following";
        String tmp = "Following";
        tv.setText(tmp);

//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                displayFollowing();
//            }
//        });

        TextView tv2 = view.findViewById(R.id.followers);
        //tmp = sp.getString("followers", "") + " followers";
        tmp = "Followers";
        tv2.setText(tmp);

//        tv2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                displayFollowers();
//            }
//        });

        TextView tv4 = view.findViewById(R.id.posts_tv);
        //tmp = sp.getString("posts", "") + " posts";
        tmp = "Posts";
        tv4.setText(tmp);



        TextView tv3 = view.findViewById(R.id.message);
        if (username1.equals(username2)){
            showPosts = true;
            Button b = view.findViewById(R.id.followingButton);
            b.setVisibility(View.GONE);
            setupPosts(true);
            tv3.setVisibility(View.GONE);
        }
        else {
            //followingReference = FirebaseDatabase.getInstance().getReference("following3");
//
            //followingReference
            //Query query = followingReference.whereEqualTo("user1", "CA");
            Query followingQuery = followingReference.orderByChild("user1").equalTo(username1);//.orderByChild("user2").equalTo(username2);
            followingQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean found = false;
                    if (snapshot.exists()) {
                        Log.i("Pronadjeno nesto korisni1-2", snapshot.toString());

                        for (DataSnapshot d : snapshot.getChildren()) {
                            Log.i("Pronadjeno nesto", snapshot.toString());
                            Log.i("Pronadjeno d, treba mi id", d.toString());

                            Following f = d.getValue(Following.class);
                            if (f.getUser2().equals(username2)) {
                                followId1 = d.getKey();
                                found = true;
                                //followId1 = d.getKey()
                                // da dobijem id;
                                // za svakog korisnika da dodam broj
                                // followers i following
                                // i da ih prikazem i dodam dugmice
                                Log.i("Pronadjeno nesto", "asd");
                                if (f.getFollowing()) {
                                    // user is following user 2
                                    followingStatus = 1;
                                    setupPosts(true);
                                    showPosts = true;
                                } else {
                                    // a follow request is sent
                                    followingStatus = 2;
                                    setupPosts(false);
                                }
                                break;
                            }

                        }
                        if (!found) {
                            followingStatus = 0;
                            setupPosts(false);
                        }

                    } else {
                        // not following/requested
                        // do not show posts
                        followingStatus = 0;
                        Log.i("Ne prate se", "tralala");
                        setupPosts(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            //
            Query followingQuery2 = followingReference.orderByChild("user1").equalTo(username2);//.orderByChild("user2").equalTo(username2);
            followingQuery2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Log.i("Pronadjeno nesto 2-1", snapshot.toString());
                        for (DataSnapshot d : snapshot.getChildren()) {
                            Log.i("dddddd", d.getKey());
                            Log.i("dddddd2", d.getChildren().toString());


                            Following f = d.getValue(Following.class);
                            Log.i("User2 iz nez", f.getUser2().toString());
                            Log.i("Ovo nez ni sta je, username1", username1);
                            if (f.getUser2().equals(username1)) {
                                followId2 = d.getKey();

                                Log.i("Found", f.getFollowing().toString());
                                if (f.getFollowing()) {
                                    // user is following user 2
                                    followingStatus2 = 1;
                                    setupBeingFollowed(false);
                                } else {
                                    // a follow request is sent
                                    followingStatus2 = 2;
                                    setupBeingFollowed(true);
                                }
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


        }

//        //Log.i("ulogovan je", this.username);
//        getFragmentManager().beginTransaction()
//                .replace(R.id.user_posts, new PostsFragment(this.username))
//
//                //.replace(R.id.mainActivity_fragments_layout, new PostsFragment(sp.getString("username", "")))
//                .commit();




//        // add following and followers
//        Following f = new Following("et11", "benedek", false);
//
//
//        followingReference = FirebaseDatabase.getInstance().getReference("following");
//        String followingId = followingReference.push().getKey();
//
//
//        // Add post to the database
//        followingReference.child(followingId).setValue(f);

        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();

                MessagesFragment messagesFragment = new MessagesFragment();
                Bundle args = new Bundle();
                args.putString("receiver", username);
                messagesFragment.setArguments(args);
                fragmentTransaction.replace(getId(), messagesFragment);

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private void displayAllDataFromDatabase() {

        Log.i("ZZZ Usao je u funkciju", "asda");
        followingReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot followingSnapshot : snapshot.getChildren()) {
                        Following f = followingSnapshot.getValue(Following.class);
                        Log.i("ZZZ FOllowing iz baze", followingSnapshot.toString());
                    }
                }
                else {
                    Log.i("U bazi nema nista", "za following");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Failed to read value.", error.toException());
            }
        });
    }

    private void setupPosts(boolean setup){
        // jel trebam negdje promijeniti ovaj following status?
        Button button = (Button) view.findViewById(R.id.followingButton);
        if (followingStatus == 0){
            button.setText("Follow");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    follow();
                }
            });
        } else if (followingStatus == 2){
            button.setText("Requested");
            // click?
        } else {
            button.setText("Unfollow");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unfollow();
                }
            });
        }
        if (setup){
            TextView tv = view.findViewById(R.id.following);
            String tmp = sp.getString("following", "") + " following";
            tv.setText(tmp);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayFollowing();
                }
            });
            // TODO OVO TESTIRATI
            TextView tv4 = view.findViewById(R.id.posts_tv);
            tv4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setupPosts(showPosts);
                }
            });

            TextView tv2 = view.findViewById(R.id.followers);
            tmp = sp.getString("followers", "") + " followers";
            tv2.setText(tmp);

            tv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayFollowers();
                }
            });
            // add apropriate buttons
            getFragmentManager().beginTransaction()
                    .replace(R.id.user_posts, new PostsFragment(this.username))

                    //.replace(R.id.mainActivity_fragments_layout, new PostsFragment(sp.getString("username", "")))
                    .commit();
        }
        else {
            // pozvati refresh
            LinearLayout tv = view.findViewById(R.id.nf);
            tv.setVisibility(View.VISIBLE);
        }


    }

    private void follow(){
        // napravi ovaj zahtjev i tjt
        // dodati u usera followers i following brojeve
        // kad se posalje zahtjev postaviti na requested
        String user1 = sp.getString("username", "default");
        String user2 = this.username;
        Following f = new Following(sp.getString("username", "default"), this.username , false);
        Log.i("crko dabogda", user1);
        Log.i("crko dabogda", user2);


        //followingReference = FirebaseDatabase.getInstance().getReference("following3");
        String followingId = followingReference.push().getKey();


        // Add post to the database
        followingReference.child(followingId).setValue(f);

        Button button = (Button) view.findViewById(R.id.followingButton);
        button.setText("Requested");



    }

    private void setupBeingFollowed(boolean request){
        if (request){
            TextView tv = view.findViewById(R.id.text_view_rq);
            String txt = this.username + " wants to follow you.";
            tv.setText(txt);
            LinearLayout ll = view.findViewById(R.id.requested);
            ll.setVisibility(View.VISIBLE);
            Log.i("treba da bude vidljiv", "asdasdas");
            Button b1 = view.findViewById(R.id.accept_follow_btn);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptFollow();
                }
            });
            Button b2 = view.findViewById(R.id.delete_follow_btn);
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteFollow();
                }
            });
        }
        else {
            // samo napisati da nas prati ovaj korisnik
        }
    }

    // lsita onih koje pratis, unfollow, remove from followers
    private void deleteFollow(){
        // You deleted _'s follow request.
        followingReference.child(followId2).removeValue();

        TextView tv = view.findViewById(R.id.text_view_rq);
        String txt = "You deleted " + this.username + "'s follow request.";
        tv.setText(txt);
        Button b1 = view.findViewById(R.id.accept_follow_btn);
        Button b2 = view.findViewById(R.id.delete_follow_btn);
        b1.setVisibility(View.GONE);
        b2.setVisibility(View.GONE);


    }

    private void acceptFollow(){
        // You accepted _'s follow request.
        // nabaviti id tog followa, nek ide zivot sacuvacu u private
        //        // add following and followers
        Following f = new Following(username, sp.getString("username", "default"),  true);
        followingReference.child(followId2).setValue(f);
        TextView tv = view.findViewById(R.id.text_view_rq);
        String txt = "You accepted " + this.username + "'s follow request.";
        tv.setText(txt);
        Button b1 = view.findViewById(R.id.accept_follow_btn);
        Button b2 = view.findViewById(R.id.delete_follow_btn);
        b1.setVisibility(View.GONE);
        b2.setVisibility(View.GONE);
        // increase followers count
        // and following
        //String followers = FirebaseDatabase.getInstance().getReference("users2").child(username).child("followers").toString();
        //Log.i("Stari broj foloversa", followers);
//        FirebaseDatabase.getInstance().getReference("users2").child(username).child("followers").setValue(followers);
        //Integer following = Integer.getInteger(sp.getString("following", "-1")) + 1;
        //String loggedUser = sp.getString("username", "default");
        //FirebaseDatabase.getInstance().getReference("users").child(loggedUser).child("following").setValue(following);

    }

    // kad kliknemo na followers i following, da se pokupe te liste
    // iz following tabele
    private void unfollow(){
        // delete row from table where user2 = user profile and user1
        // = logged user
        followingReference.child(followId1).removeValue();
        Button button = (Button) view.findViewById(R.id.followingButton);
        button.setText("Follow");
        button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
             follow();
                }
         });
        }
        // decrease followers and following

    private void displayFollowing(){

//        getFragmentManager().beginTransaction()
//                .replace(R.id.user_posts, new FollowersFragment(sp.getString("username", "default"), username, true) )
//                .commit();

        getFragmentManager().beginTransaction()
                .replace(R.id.user_posts, new FollowersFragment(username, username, true) )
                .commit();

    }

    private void displayFollowers(){
        getFragmentManager().beginTransaction()
                .replace(R.id.user_posts, new FollowersFragment(username, sp.getString("username", "default"), false))
                .commit();
    }

    private void displayPosts(){

    }



}
