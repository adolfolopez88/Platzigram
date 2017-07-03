package com.example.adolfo.platzigram.model;

/**
 * Created by Adolfo on 17/06/2017.
 */

public class Picture {
    private String postId;
    private String picture;
    private String userName;
    private String time;
    private String like_number = "0 dias";

    public Picture(String postId,String picture, String userName, String time, String like_number) {
        this.postId = postId;
        this.picture = picture;
        this.userName = userName;
        this.time = time;
        this.like_number = like_number;
    }


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLike_number() {
        return like_number;
    }

    public void setLike_number(String like_number) {
        this.like_number = like_number;
    }
}
