    package com.example.instagram_copy;

    import android.os.Bundle;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ArrayAdapter;
    import android.widget.ListView;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.fragment.app.Fragment;

    import com.firebase.ui.database.FirebaseListAdapter;
    import com.firebase.ui.database.FirebaseListOptions;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.Locale;

    public class PostsFragment extends Fragment {
        private DatabaseReference postsReference;
        private ListView postsListView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_posts, container, false);

            postsReference = FirebaseDatabase.getInstance().getReference().child("posts");
            postsListView = view.findViewById(R.id.listViewPosts);

            ArrayList<Post> posts = new ArrayList<Post>(); //TODO make downloadPosts() work
            String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            posts.add(new Post("et11", "ASFFAS", "https://images.pexels.com/photos/1108099/pexels-photo-1108099.jpeg?auto=compress&cs=tinysrgb&w=600", currentDate));
            posts.add(new Post("et11", "fff", "https://images.pexels.com/photos/1108099/pexels-photo-1108099.jpeg?auto=compress&cs=tinysrgb&w=600", currentDate));

            PostAdapter adapter = new PostAdapter(requireContext(), posts);
            // Ustaw adapter dla ListView

            postsListView.setAdapter(adapter);

            return view;
        }
        public ArrayList<Post> downloadPosts() {
            ArrayList<Post> posts = new ArrayList<>();

            postsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Post post = postSnapshot.getValue(Post.class);
                        if (post != null) {
                            posts.add(post);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase", "Failed to read value.", error.toException());
                }
            });

            return posts;
        }
    private void displayPosts() {
        FirebaseListOptions<Post> options = new FirebaseListOptions.Builder<Post>()
                .setLayout(R.layout.post_item)
                .setQuery(postsReference, Post.class)
                .build();

        FirebaseListAdapter<Post> adapter = new FirebaseListAdapter<Post>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Post model, int position) {
                TextView usernameTextView = v.findViewById(R.id.post_username);
                TextView descriptionTextView = v.findViewById(R.id.post_description);
                TextView dateTextView = v.findViewById(R.id.post_date);

                usernameTextView.setText(model.getUsername());
                descriptionTextView.setText(model.getDescription());
                dateTextView.setText(model.getDate());
            }
        };

        postsListView.setAdapter(adapter);
    }
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
