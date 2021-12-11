package com.example.praktikum;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.praktikum.adapter.DBAdapter;
import com.example.praktikum.helper.DBHelper;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;


public class MainActivity extends AppCompatActivity{
    private Button btnTambah;
    private RecyclerView listChord;

    private DBHelper dbHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTambah = findViewById(R.id.btnTambah);
        listChord = findViewById(R.id.list_chord);

        dbHelper = new DBHelper(this);

        loadRecords();

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TambahChordActivity.class);
                intent.putExtra("isEditMode", false);
                startActivity(intent);
            }
        });
    }

    private void loadRecords() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constant.CHORDS;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")){
                                dbHelper.deleteSemua();
                                JSONArray array = new JSONArray(object.getString("data"));
                                for(int i=0;i<array.length();i++){
                                    JSONObject chordObject = array.getJSONObject(i);
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
                                DBAdapter adapter = new DBAdapter(MainActivity.this, dbHelper.getChord());
                                listChord.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Display the first 500 characters of the response string.

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"No Connection",Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

//    private void searchRecords(String query){
//        DBAdapter adapter = new DBAdapter(MainActivity.this, dbHelper.searchResep(query));
//
//        listResep.setAdapter(adapter);
//    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecords();
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
}