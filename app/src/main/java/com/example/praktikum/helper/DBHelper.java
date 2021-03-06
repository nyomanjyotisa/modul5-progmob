package com.example.praktikum.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.praktikum.model.Chord;
import com.example.praktikum.model.CommentModel;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 6;
    static final String DATABASE_NAME = "chord_lagu.db";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TB_CHORD = "CREATE TABLE chord (id INTEGER PRIMARY KEY autoincrement," +
                "judul TEXT NOT NULL," +
                "penyanyi TEXT NOT NULL," +
                "genre TEXT NOT NULL," +
                "level TEXT NOT NULL," +
                "durasi_menit TEXT NOT NULL," +
                "durasi_detik TEXT NOT NULL," +
                "chord_lirik TEXT NOT NULL)";
        final String SQL_CREATE_TB_USER = "CREATE TABLE users (id INTEGER PRIMARY KEY autoincrement," +
                "name TEXT NOT NULL," +
                "email_verified_At TEXT NOT NULL," +
                "email TEXT NOT NULL," +
                "password TEXT NOT NULL," +
                "remember_token TEXT NOT NULL," +
                "created_At TEXT NOT NULL," +
                "updated_at TEXT NOT NULL)";
        final String SQL_CREATE_TB_FAVORITES = "CREATE TABLE favorites (id INTEGER PRIMARY KEY autoincrement," +
                "id_chord INTEGER NOT NULL," +
                "id_user INTEGER NOT NULL)";
        final String SQL_CREATE_TB_COMMENTS = "CREATE TABLE comments (id INTEGER PRIMARY KEY autoincrement," +
                "id_chord INTEGER NOT NULL," +
                "id_user INTEGER NOT NULL," +
                "rating TEXT NOT NULL," +
                "comment TEXT NOT NULL," +
                "created_At TEXT NOT NULL," +
                "updated_at TEXT NOT NULL)";

        db.execSQL(SQL_CREATE_TB_CHORD);
        db.execSQL(SQL_CREATE_TB_USER);
        db.execSQL(SQL_CREATE_TB_FAVORITES);
        db.execSQL(SQL_CREATE_TB_COMMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS chord");
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS favorites");
        db.execSQL("DROP TABLE IF EXISTS comments");
    }

    // mengambil semua data chord
    public ArrayList <Chord> getChord() {
        ArrayList<Chord> arrayList = new ArrayList<>();

        // query select
        String QUERY = "SELECT * FROM chord";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(QUERY, null);

        //looping semua baris dan menambahkan ke list
        if (cursor.moveToFirst()){
            do {
                Chord chord = new Chord();
                chord.setId(cursor.getString(0));
                chord.setJudul(cursor.getString(1));
                chord.setPenyanyi(cursor.getString(2));
                chord.setGenre(cursor.getString(3));
                chord.setLevel(cursor.getString(4));
                chord.setDurasiMenit(cursor.getString(5));
                chord.setDurasiDetik(cursor.getString(6));
                chord.setChordLirik(cursor.getString(7));
                arrayList.add(chord);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public ArrayList <CommentModel> getComment() {
        ArrayList<CommentModel> arrayList = new ArrayList<>();

        // query select
        String QUERY = "SELECT * FROM comments";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(QUERY, null);

        //looping semua baris dan menambahkan ke list
        if (cursor.moveToFirst()){
            do {
                CommentModel commentModel = new CommentModel(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getString(4), "");
                arrayList.add(commentModel);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    // mencari data resep
    public ArrayList <Chord> searchChord(String query) {
        ArrayList<Chord> arrayList = new ArrayList<>();

        // query select
        String QUERY = "SELECT * FROM chord WHERE judul LIKE '%" + query +"%'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(QUERY, null);

        //looping semua baris dan menambahkan ke list
        if (cursor.moveToFirst()){
            do {
                Chord chord = new Chord();
                chord.setId(cursor.getString(0));
                chord.setJudul(cursor.getString(1));
                chord.setPenyanyi(cursor.getString(2));
                chord.setGenre(cursor.getString(3));
                chord.setLevel(cursor.getString(4));
                chord.setDurasiMenit(cursor.getString(5));
                chord.setDurasiDetik(cursor.getString(6));
                chord.setChordLirik(cursor.getString(7));
                arrayList.add(chord);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    //mengambil jumlah data record
    public int getRecordCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM chord ";
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //memasukan data input ke db
    public void insert (String judul, String penyanyi, String genre, String level, String durasiMenit, String durasiDetik, String chordLirik){
        SQLiteDatabase database = this.getWritableDatabase();
        String QUERY = "INSERT INTO chord (judul, penyanyi,  genre, level, durasi_menit, durasi_detik, chord_lirik)" +
                "VALUES ('"+judul+"', '"+penyanyi+"', '"+genre+"', '"+level+"', '"+durasiMenit+"', '"+durasiDetik+"', '"+chordLirik+"')";
        database.execSQL(QUERY);
    }

    //mengupdate data
    public void update(String id, String judul, String penyanyi, String genre, String level, String durasiMenit, String durasiDetik, String chordLirik){
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("judul", judul);
        values.put("penyanyi", penyanyi);
        values.put("genre", genre);
        values.put("level", level);
        values.put("durasi_menit", durasiMenit);
        values.put("durasi_detik", durasiDetik);
        values.put("chord_lirik", chordLirik);

        database.update("chord", values, "id = ?", new String[]{id});

        database.close();
    }

    // menghapus data
    public void delete(String id){
        SQLiteDatabase database = this.getWritableDatabase();

        database.delete("chord", "id = ?", new String[]{id});
        database.close();
    }

    public void deleteSemua(){
        SQLiteDatabase database = this.getWritableDatabase();

        database.execSQL("DELETE FROM chord");
        database.close();
    }

    //add with id
    public void add (Integer id, String judul, String penyanyi, String genre, String level, String durasiMenit, String durasiDetik, String chordLirik){
        SQLiteDatabase database = this.getWritableDatabase();
        String QUERY = "INSERT INTO chord (id, judul, penyanyi,  genre, level, durasi_menit, durasi_detik, chord_lirik)" +
                "VALUES ('"+id+"','"+judul+"', '"+penyanyi+"', '"+genre+"', '"+level+"', '"+durasiMenit+"', '"+durasiDetik+"', '"+chordLirik+"')";
        database.execSQL(QUERY);
    }

    public void addComment (Integer id, Integer id_user, Integer id_chord, Integer rating, String commnet){
        SQLiteDatabase database = this.getWritableDatabase();
        String QUERY = "INSERT INTO commnets (id, id_user, id_chord,  rating, commnet)" +
                "VALUES ('"+id+"','"+id_user+"', '"+id_chord+"', '"+rating+"', '"+commnet+"')";
        database.execSQL(QUERY);
    }
}
