package com.example.praktikum.model;

public class CommentModel {
    Integer id, id_user, id_chord, rating;
    String comment, username;

    public CommentModel(Integer id, Integer id_user, Integer id_chord, Integer rating, String comment, String username) {
        this.id = id;
        this.id_user = id_user;
        this.id_chord = id_chord;
        this.rating = rating;
        this.comment = comment;
        this.username = username;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getIdUser() {
        return id_user;
    }
    public void setIdUser(Integer id_user) {
        this.id_user = id_user;
    }
    public Integer getIdChord() {
        return id_chord;
    }
    public void setIdChord(Integer id_chord) {
        this.id_chord = id_chord;
    }
    public Integer getRating() {
        return rating;
    }
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) { this.comment = comment; }
    public String getUsername() {
        return username;
    }
    public void setUsername(String comment) { this.username = username; }

}
