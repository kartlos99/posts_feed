package com.example.postsfeedapp.models;

public class PostModel {
    private int id;
    private String name, comment, photoURL;

    public PostModel(int id, String name, String comment, String photoURL) {
        this.id = id;
        this.name = name;
        this.comment = comment;
        this.photoURL = photoURL;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public String getPhotoURL() {
        return photoURL;
    }
}
