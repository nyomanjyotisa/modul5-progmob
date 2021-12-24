package com.example.praktikum;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.praktikum.adapter.ChordAdapter;
import com.example.praktikum.adapter.DBAdapter;
import com.example.praktikum.api.ChordAPIHelper;
import com.example.praktikum.api.RetroHelper;
import com.example.praktikum.api.UserAPIHelper;
import com.example.praktikum.helper.DBHelper;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity{
    private Button btnTambah;
    private LinearLayout profile;
    private RecyclerView listChord;
    private EditText searchText;
    private String keyword="";

    private DBHelper dbHelper;
    private ChordAdapter chordAdapter;

    private ChordAPIHelper chordAPIHelper;

    private Call<ArrayList<Chord>> call;
    private ArrayList<Chord> chordList;


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

        chordAPIHelper = RetroHelper.connectRetrofit().create(ChordAPIHelper.class);
        Gson gson = new Gson();

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
                timer.cancel();
                timer= new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                if (s.toString().trim().length() == 0) {
                                    keyword="";
                                }else{
                                    keyword=s.toString();
                                }
                                getChord();
                            }
                        }, DELAY
                );
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        getChord();
    }

    private void getChord(){


        if(keyword.trim().length()==0){
            call= chordAPIHelper.getChord();
        }else {
            call= chordAPIHelper.getChordDariJudul(keyword);
        }

        call.enqueue(new Callback<ArrayList<Chord>>() {
            @Override
            public void onResponse(Call<ArrayList<Chord>> call, Response<ArrayList<Chord>> response) {
                if(!response.isSuccessful()){
                    Log.d("app_chord",String.valueOf(response.code()));
                    Toast.makeText(getApplicationContext(), "Code : " + Integer.toString(response.code()), Toast.LENGTH_LONG).show();
                    return;
                }

                chordList=response.body();
                Log.d("panjang_data",Integer.toString(chordList.size()));

                chordAdapter= new ChordAdapter(getApplicationContext(),chordList);
                listChord.setAdapter(chordAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Chord>> call, Throwable t) {
                Log.d("api",t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
//    private void loadRecords() {
//        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url = Constant.CHORDS;
//
//        // Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject object = new JSONObject(response);
//                            if (object.getBoolean("success")){
//                                dbHelper.deleteSemua();
//                                JSONArray array = new JSONArray(object.getString("data"));
//                                for(int i=0;i<array.length();i++){
//                                    JSONObject chordObject = array.getJSONObject(i);
//                                    dbHelper.add(
//                                            chordObject.getInt("id"),
//                                            chordObject.getString("judul"),
//                                            chordObject.getString("penyanyi"),
//                                            chordObject.getString("genre"),
//                                            chordObject.getString("level"),
//                                            chordObject.getString("durasi_menit"),
//                                            chordObject.getString("durasi_detik"),
//                                            chordObject.getString("chord_dan_lirik")
//                                    );
//                                }
//                                DBAdapter adapter = new DBAdapter(MainActivity.this, dbHelper.getChord());
//                                listChord.setAdapter(adapter);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        // Display the first 500 characters of the response string.
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(MainActivity.this,"No Connection",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest);
//    }

//    private void searchRecords(String query){
//        DBAdapter adapter = new DBAdapter(MainActivity.this, dbHelper.searchResep(query));
//
//        listResep.setAdapter(adapter);
//    }

    @Override
    public void onResume() {
        super.onResume();
        getChord();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        //inflate menu
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//
//        //searchview
//        MenuItem item = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) item.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                //search saat tombol search di keybord diklik
//                searchRecords(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                searchRecords(newText);
//                return false;
//            }
//        });
//
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //handle item menu
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}