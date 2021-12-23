package com.example.praktikum.api;

import com.example.praktikum.Chord;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ChordAPIHelper {
    @GET("chords")
    Call<ArrayList<Chord>> getChord();

    @GET("chords/{judul}")
    Call<ArrayList<Chord>> getChordDariJudul(@Path("judul") String judul);
}