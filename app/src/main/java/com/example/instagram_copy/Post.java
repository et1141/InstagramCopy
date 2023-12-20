package com.example.instagram_copy;

public class Post {
    String username, description,imageUrl,date;

    public Post(String username, String description, String imageUrl, String date) {
        this.username = username;
        this.description = description;
        this.imageUrl = imageUrl;
        this.date = date;
    }

    public Post() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
