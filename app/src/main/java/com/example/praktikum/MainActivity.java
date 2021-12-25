package com.example.praktikum;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.praktikum.adapter.ChordAdapter;
import com.example.praktikum.api.Constant;
import com.example.praktikum.helper.DBHelper;
import com.example.praktikum.model.Chord;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class MainActivity extends AppCompatActivity{
    private Button btnTambah;
    private LinearLayout profile;
    private RecyclerView listChord;
    private EditText searchText;
    private String keyword="";

    private DBHelper dbHelper;
    private ChordAdapter chordAdapter;

    private List<Chord> chordList = new ArrayList<>();
    private List<Chord> filterList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTambah = findViewById(R.id.btnTambah);
        listChord = findViewById(R.id.list_chord);
        profile = findViewById(R.id.profile);
        searchText = findViewById(R.id.editTextSearch);

        dbHelper = new DBHelper(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listChord.setLayoutManager(mLayoutManager);

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TambahChordActivity.class);
                intent.putExtra("isEditMode", false);
                startActivity(intent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("isEditMode", false);
                startActivity(intent);
            }
        });
        searchText.addTextChangedListener(new TextWatcher() {
            private Timer timer = new Timer();
            private final long DELAY = 1000;
            @Override
            public void afterTextChanged(Editable s) {
                filterList.clear();
                if(s.toString().isEmpty()){
                    listChord.setAdapter(new ChordAdapter(getApplicationContext(),chordList));
                }
                else{
                    Filter(s.toString());
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        loadRecords();
    }

    private void Filter(String text) {
        for(Chord chord:chordList){
            if(chord.getJudul().equals(text)){
                filterList.add(chord);
            }
        }
        listChord.setAdapter(new ChordAdapter(getApplicationContext(),filterList));
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    private void showDialog(){
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);

        // Set title dialog
        alertDialogBuilder.setTitle("You want to close the app?");

        // Set pesan dari dialog
        alertDialogBuilder
                .setMessage("Choose 'Exit' to close the app.")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Exit",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // Jika tombol diklik, maka akan menutup activity ini
                        MainActivity.this.finishAffinity();
                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Jika tombol ini diklik, akan menutup dialog
                        dialog.cancel();
                    }
                });

        // Membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Menampilkan alert dialog
        alertDialog.show();
    }

    private void loadRecords() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constant.CHORDS;

        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject chordObject = response.getJSONObject(i);
                        chordList.add(new Chord(
                                chordObject.getString("id"),
                                chordObject.getString("judul"),
                                chordObject.getString("penyanyi"),
                                chordObject.getString("genre"),
                                chordObject.getString("level"),
                                chordObject.getString("durasi_menit"),
                                chordObject.getString("durasi_detik"),
                                chordObject.getString("chord_dan_lirik")
                        ));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ChordAdapter adapter = new ChordAdapter(MainActivity.this, chordList);
                listChord.setAdapter(adapter);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"No Connection",Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}