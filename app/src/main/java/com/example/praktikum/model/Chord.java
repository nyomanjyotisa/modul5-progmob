package com.example.praktikum.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Chord implements Parcelable {
    String id;
    String id_user;
    String judul;
    String penyanyi;
    String genre;
    String level;
    String durasiMenit;
    String durasiDetik;
    String chordLirik;


    public Chord(String id, String judul, String penyanyi, String genre, String level, String durasiMenit, String durasiDetik, String chordLirik) {
        this.id = id;
        this.judul = judul;
        this.penyanyi = penyanyi;
        this.genre = genre;
        this.level = level;
        this.durasiMenit = durasiMenit;
        this.durasiDetik = durasiDetik;
        this.chordLirik = chordLirik;
    }

    protected Chord(Parcel in) {
        id = in.readString();
        judul = in.readString();
        penyanyi = in.readString();
        genre = in.readString();
        level = in.readString();
        durasiMenit = in.readString();
        durasiDetik = in.readString();
        chordLirik = in.readString();
    }

    public static final Creator<Chord> CREATOR = new Creator<Chord>() {
        @Override
        public Chord createFromParcel(Parcel in) {
            return new Chord(in);
        }

        @Override
        public Chord[] newArray(int size) {
            return new Chord[size];
        }
    };

    public Chord() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPenyanyi() {
        return penyanyi;
    }

    public void setPenyanyi(String penyanyi) {
        this.penyanyi = penyanyi;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDurasiMenit() {
        return durasiMenit;
    }

    public void setDurasiMenit(String durasiMenit) {
        this.durasiMenit = durasiMenit;
    }

    public String getDurasiDetik() {
        return durasiDetik;
    }

    public void setDurasiDetik(String durasiDetik) {
        this.durasiDetik = durasiDetik;
    }

    public String getChordLirik() {
        return chordLirik;
    }

    public void setChordLirik(String chordLirik) {
        this.chordLirik = chordLirik;
    }

    public static Creator<Chord> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(judul);
        parcel.writeString(penyanyi);
        parcel.writeString(genre);
        parcel.writeString(level);
        parcel.writeString(durasiMenit);
        parcel.writeString(durasiDetik);
        parcel.writeString(chordLirik);
    }
}
