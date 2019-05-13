package com.hackthon.here.models;

public class PostsModel {

    private String id, datetime, imageUrl, username, comment, location, locationText, upvotes, downvotes;

    public PostsModel(String id, String datetime, String imageUrl, String username, String comment, String location, String locationText, String upvotes, String downvotes) {
        this.id = id;
        this.datetime = datetime;
        this.imageUrl = imageUrl;
        this.username = username;
        this.comment = comment;
        this.location = location;
        this.locationText = locationText;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

    public PostsModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationText() {
        return locationText;
    }

    public void setLocationText(String locationText) {
        this.locationText = locationText;
    }

    public String getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(String upvotes) {
        this.upvotes = upvotes;
    }

    public String getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(String downvotes) {
        this.downvotes = downvotes;
    }
}
