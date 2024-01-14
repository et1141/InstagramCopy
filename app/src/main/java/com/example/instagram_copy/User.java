package com.example.instagram_copy;

public class User {
    String email,username,full_name,password;

    int followers, following, posts;

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public User() {
    }

    public User(String email, String username, String full_name, String password, int followers, int following, int posts) {
        this.email = email;
        this.username = username;
        this.full_name = full_name;
        this.password = password;
        this.followers = followers;
        this.following = following;
        this.posts = posts;
    }

    public User(String email, String username, String full_name, String password) {
        this.email = email;
        this.username = username;
        this.full_name = full_name;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
