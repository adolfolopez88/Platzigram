package com.example.adolfo.platzigram.model;

/**
 * Created by Adolfo on 29/06/2017.
 */

public class Post {

    private String id;
    private String title;
    private String description;
    private String imgUrl;


    public Post(String id, String title, String description, String imgUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
    }


    public Post(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
