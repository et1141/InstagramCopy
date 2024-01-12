package com.example.instagram_copy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        ImageView post_image = listItemView.findViewById(R.id.post_image);

        usernameTextView.setText(currentPost.getUsername());
        descriptionTextView.setText(currentPost.getDescription());

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date_converted = date.format(new Date(currentPost.getDate()));

        dateTextView.setText(date_converted);

        new DownloadImageFromInternet((ImageView) post_image).execute(currentPost.getImageUrl()); //prev version
        //  Picasso.get()
        //         .load(currentPost.getImageUrl())
        //         .into(post_image);


        return listItemView;
    }


//code source:https://www.tutorialspoint.com/how-do-i-load-an-imageview-by-url-on-android
private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;
    public DownloadImageFromInternet(ImageView imageView) {
        this.imageView=imageView;
        Toast.makeText(context.getApplicationContext(), "Please wait, it may take a few minute...",Toast.LENGTH_SHORT).show();
    }
    protected Bitmap doInBackground(String... urls) {
        String imageURL=urls[0];
        Bitmap bimage=null;
        try {
            InputStream in=new java.net.URL(imageURL).openStream();
            bimage= BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error Message", e.getMessage());
            e.printStackTrace();
        }
        return bimage;
    }
    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }
}
}

