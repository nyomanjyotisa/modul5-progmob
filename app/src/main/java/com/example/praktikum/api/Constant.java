package com.example.praktikum.api;

public class Constant {
    public static final String URL="http://10.0.2.2:8000/";
    public static final String HOME=URL+"api";
    public static final String LOGIN=HOME+"/user/login";
    public static final String REGISTER=HOME+"/user/register";
    public static final String CHORDS=HOME+"/chords";
    public static final String ADD_CHORD=CHORDS+"/create";
    public static final String STORE_CHORD=CHORDS+"/store";
    public static final String UPDATE_CHORD=CHORDS+"/update";
    public static final String DELETE_CHORD=CHORDS+"/delete";
    public static final String DELETE_CHORD_USER=CHORDS+"/deleteuser";
    public static final String COMMENTS=HOME+"/comments";
    public static final String ADD_COMMENT=COMMENTS+"/create";
    public static final String STORE_COMMENT=COMMENTS+"/store";
    public static final String USER_COMMENT=COMMENTS+"/user";
    public static final String USER_CHORD=CHORDS+"/user";
}
