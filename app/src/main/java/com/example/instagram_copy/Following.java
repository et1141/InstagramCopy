package com.example.instagram_copy;

public class Following {

    private String user1;
    private String user2;
    private Boolean following;


    public Following(){

    }

    public Following(String user1, String user2, Boolean following){
        this.user1 = user1;
        this.user2 = user2;
        this.following = following;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public Boolean getFollowing() {
        return following;
    }

    public void setFollowing(Boolean following) {
        this.following = following;
    }
}
