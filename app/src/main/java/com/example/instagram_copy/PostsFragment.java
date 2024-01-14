    package com.example.instagram_copy;

    import android.os.Bundle;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ListView;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.fragment.app.Fragment;


    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.Query;
    import com.google.firebase.database.ValueEventListener;

    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.Locale;

    public class PostsFragment extends Fragment {
        private DatabaseReference postsReference;
        private ListView postsListView;

        private boolean feed = false;

        String username1 = null;

        private ArrayList<String> usernames;

        public PostsFragment() {
            postsReference = FirebaseDatabase.getInstance().getReference().child("posts");
            //this.username1="";
        }

        public PostsFragment (String username, boolean feed) {
            postsReference = FirebaseDatabase.getInstance().getReference().child("posts");
            this.feed = true;
            this.username1 = username;
        }

        public PostsFragment (String username) {
            postsReference = FirebaseDatabase.getInstance().getReference().child("posts");
            // TODO TEST THIS?????
            this.feed = false;
            this.username1 = username;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_posts, container, false);

            postsListView = view.findViewById(R.id.listViewPosts);

            if (feed){
                downloadFeed();
                return view;
            }

            //Log.i("oooDa li je feed, username", username1);
            //Log.i("oooDa li je feed", feed + "");

//            downloadFeed(new OnPostsDownloadedListener() {
//                @Override
//                public void onPostsDownloaded(ArrayList<Post> posts) {
//
//                }
//            });

            // Use a callback to handle the completion of downloadPosts()
            downloadPosts(new OnPostsDownloadedListener() {
                @Override
                public void onPostsDownloaded(ArrayList<Post> posts) {
                    // Now you can proceed with the rest of the code
                    PostAdapter adapter = new PostAdapter(requireContext(), posts);
                    // set adapter for ListView
                    postsListView.setAdapter(adapter);
                }
            });

            return view;
        }

        public void downloadFeed() {
            // query za usere
            downloadUsers(new OnFollowingLoadedListener() {
                @Override
                public void onFollowersLoaded(ArrayList<String> users) {
                    downloadPosts(new OnPostsDownloadedListener() {
                        @Override
                        public void onPostsDownloaded(ArrayList<Post> posts) {
                            // Now you can proceed with the rest of the code
                            PostAdapter adapter = new PostAdapter(requireContext(), posts);
                            // set adapter for ListView
                            postsListView.setAdapter(adapter);
                        }
                    });
                }
            });
        }

        private ArrayList<String> downloadUsers(OnFollowingLoadedListener listener){
           this.usernames = new ArrayList<>();
            Query query = FirebaseDatabase.getInstance().getReference()
                    .child("following11") // TODO IS THIS GOOD
                    .orderByChild("user1")
                    .equalTo(username1);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot followingSnapshot : snapshot.getChildren()) {
                        Following following = followingSnapshot.getValue(Following.class);
                        if (following.getFollowing()) {
                            usernames.add(following.getUser2());
                        }
                    }
                    //infor to the listener that posts have been downloaded
                    listener.onFollowersLoaded(usernames);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase", "Failed to read value.", error.toException());
                }
            });
            return usernames;       // todo ovi returnovi
                                    // mi ni ne trebaju?
        }

        public interface OnPostsDownloadedListener {
            void onPostsDownloaded(ArrayList<Post> posts);
        }

        public interface OnFollowingLoadedListener {
            void onFollowersLoaded(ArrayList<String> users);
        }

        public void downloadPosts(OnPostsDownloadedListener listener) {
            ArrayList<Post> posts = new ArrayList<>();

            Query query;
            if (!feed) {
                query = FirebaseDatabase.getInstance().getReference()
                        .child("posts")
                        .orderByChild("username")
                        .equalTo(username1);


                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Post post = postSnapshot.getValue(Post.class);
                            if (post != null) {
                                posts.add(post);
                            }
                        }
                        //infor to the listener that posts have been downloaded
                        listener.onPostsDownloaded(posts);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Failed to read value.", error.toException());
                    }
                });
            } else {
                Log.i("Ucitavanje feeda", "aaa");
                loadFeed();
                postsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Post post = postSnapshot.getValue(Post.class);
                            if (post != null) {
                                if (usernames.contains(post.getUsername()) || post.getUsername().equals(username1))
                                    posts.add(post);
                            }
                        }
                        //infor to the listener that posts have been downloaded
                        listener.onPostsDownloaded(posts);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Failed to read value.", error.toException());
                    }
                });
            }

        }

        private void loadFeed() {
        }

    }


 /*
    //old function showing how adapter works
    private void displayRandom(){
        //displayPosts();
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, posts);
        // Ustaw adapter dla ListView
        postsListView.setAdapter(adapter);
    }
}
*/