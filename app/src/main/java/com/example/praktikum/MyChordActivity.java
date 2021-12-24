package com.example.praktikum;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.praktikum.adapter.DBAdapter;
import com.example.praktikum.helper.DBHelper;
import com.example.praktikum.model.CommentModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyChordActivity extends AppCompatActivity {
    DBHelper dbHelper;
    Integer countdb;
    Button btnTambah, btnSync;
    private RecyclerView listChord;
    JSONArray loadRecordsArray = null;
    SharedPreferences sharedPreferencesLogin;
    String id_user;
    ArrayList<Chord> chordFromSQLite;
    ArrayList<CommentModel> commentFromSQLite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mychord);

        sharedPreferencesLogin = getSharedPreferences("loginPre", MODE_PRIVATE);
        id_user = sharedPreferencesLogin.getString("id_user", "");
        Log.v("babii", id_user);

        dbHelper = new DBHelper(this);
        if(dbHelper.getRecordCount() == 0){
            loadRecords();
        }

        listChord = findViewById(R.id.list_chord);
        btnTambah = findViewById(R.id.btnTambah);
        btnSync = findViewById(R.id.btnSync);


        Log.v("babi", String.valueOf(countdb));


        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyChordActivity.this, TambahChordActivity.class);
                intent.putExtra("isEditMode", false);
                startActivity(intent);
            }
        });

        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncWebServer();
            }
        });

        getChord();
    }

    @Override
    public void onResume() {
        super.onResume();
        getChord();
    }

    private void getChord(){
        DBAdapter adapter = new DBAdapter(MyChordActivity.this, dbHelper.getChord());
        listChord.setAdapter(adapter);
    }

    private void loadRecords(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =Constant.USER_CHORD;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject object = new JSONObject(response);
                            if(object.getBoolean("success")){
                                loadRecordsArray = new JSONArray(object.getString("data"));
                                for(int i=0;i<loadRecordsArray.length();i++) {
                                    JSONObject chordObject = loadRecordsArray.getJSONObject(i);
                                    dbHelper.add(
                                            chordObject.getInt("id"),
                                            chordObject.getString("judul"),
                                            chordObject.getString("penyanyi"),
                                            chordObject.getString("genre"),
                                            chordObject.getString("level"),
                                            chordObject.getString("durasi_menit"),
                                            chordObject.getString("durasi_detik"),
                                            chordObject.getString("chord_dan_lirik")
                                    );
                                }
                                DBAdapter adapter = new DBAdapter(MyChordActivity.this, dbHelper.getChord());
                                listChord.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyChordActivity.this,"Load Gagal",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id_user", id_user);
                return map;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void syncWebServer(){
        getUserChordComments();
        deleteUserChordOnWebserver();

        chordFromSQLite = dbHelper.getChord();
        for(int i=0; i<chordFromSQLite.size(); i++) {
            Chord curChord = chordFromSQLite.get(i);
            Log.v("anj", curChord.getId());
            Log.v("anj", id_user);
            Log.v("anj", curChord.getJudul());
            Log.v("anj", curChord.getPenyanyi());
            Log.v("anj", curChord.getLevel());
            Log.v("anj", curChord.getGenre());
            Log.v("anj", curChord.getDurasiMenit());
            Log.v("anj", curChord.getDurasiDetik());
            Log.v("anj", curChord.getChordLirik());
            insertToWebServer(curChord.getId(),
                    id_user,
                    curChord.getJudul(),
                    curChord.getPenyanyi(),
                    curChord.getLevel(),
                    curChord.getGenre(),
                    curChord.getDurasiMenit(),
                    curChord.getDurasiDetik(),
                    curChord.getChordLirik());
        }

        commentFromSQLite = dbHelper.getComment();
        for(int i=0; i<commentFromSQLite.size(); i++) {
            CommentModel curComment = commentFromSQLite.get(i);
            insertCommentToWebServer(curComment.getId(),
                    curComment.getIdUser(),
                    curComment.getIdChord(),
                    curComment.getRating(),
                    curComment.getComment());
        }
    }

    private void deleteUserChordOnWebserver(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =Constant.DELETE_CHORD_USER;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject object = new JSONObject(response);
                            if(object.getBoolean("success")){
//                                Intent intent = new Intent(TambahChordActivity.this, MainActivity.class);
//                                startActivity(intent);
//                                Toast.makeText(getApplicationContext(), "Add Data Success", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyChordActivity.this,"Clear Gagal",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id_user", id_user);
                return map;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getUserChordComments(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =Constant.USER_COMMENT;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject object = new JSONObject(response);
                            if(object.getBoolean("success")){
                                loadRecordsArray = new JSONArray(object.getString("data"));
                                for(int i=0;i<loadRecordsArray.length();i++) {
                                    JSONObject chordObject = loadRecordsArray.getJSONObject(i);
                                    dbHelper.addComment(
                                            chordObject.getInt("id"),
                                            chordObject.getInt("id_user"),
                                            chordObject.getInt("id_chord"),
                                            chordObject.getInt("rating"),
                                            chordObject.getString("commnet")
                                    );
                                }
                                DBAdapter adapter = new DBAdapter(MyChordActivity.this, dbHelper.getChord());
                                listChord.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyChordActivity.this,"Load Gagal",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id_user", id_user);
                return map;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void insertToWebServer(String id,
                                   String id_user,
                                   String judul_lagu,
                                   String nama_penyanyi,
                                   String level_lagu,
                                   String genre_lagu,
                                   String menit_lagu,
                                   String detik_lagu,
                                   String chord_lagu){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =Constant.STORE_CHORD;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject object = new JSONObject(response);
                            if(object.getBoolean("success")){
//                                Intent intent = new Intent(TambahChordActivity.this, MainActivity.class);
//                                startActivity(intent);
//                                Toast.makeText(getApplicationContext(), "Add Data Success", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyChordActivity.this,"Add Chord Gagal",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id",id);
                map.put("id_user",id_user);
                map.put("judul",judul_lagu);
                map.put("penyanyi", nama_penyanyi);
                map.put("level",level_lagu);
                map.put("genre", genre_lagu);
                map.put("durasi_menit", menit_lagu);
                map.put("durasi_detik", detik_lagu);
                map.put("chord_dan_lirik", chord_lagu);
                return map;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void insertCommentToWebServer(Integer id,
                                          Integer id_user,
                                          Integer id_chord,
                                          Integer rating,
                                          String comment){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =Constant.STORE_COMMENT;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject object = new JSONObject(response);
                            if(object.getBoolean("success")){
//                                Intent intent = new Intent(TambahChordActivity.this, MainActivity.class);
//                                startActivity(intent);
//                                Toast.makeText(getApplicationContext(), "Add Data Success", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyChordActivity.this,"Add Comment Gagal",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", String.valueOf(id));
                map.put("id_user", String.valueOf(id_user));
                map.put("judul", String.valueOf(id_chord));
                map.put("penyanyi", String.valueOf(rating));
                map.put("level",comment);
                return map;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
