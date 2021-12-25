package com.example.praktikum;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.praktikum.api.Constant;
import com.example.praktikum.helper.DBHelper;
import com.example.praktikum.model.Chord;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TambahChordActivity extends AppCompatActivity {
    EditText nama,penyanyi, chordl;
    Button btnSubmit;
    RadioGroup level;
    RadioButton radio, mudah, sedang, susah;
    SeekBar menit,detik;
    TextView textmenit,textdetik;
    CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8;
    private ArrayList<String> cbs;

    private String id, judul_lagu,nama_penyanyi,  level_lagu, menit_lagu, detik_lagu, chord_lagu, genre_lagu;
    private boolean isEditMode = false;

    private DBHelper dbHelper = new DBHelper(this);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_chord);

        btnSubmit = findViewById(R.id.buttonSubmit);


        nama = findViewById(R.id.editTextNamaLagu);
        penyanyi = findViewById(R.id.editTextNamaPenyanyi);
        chordl = findViewById(R.id.editTextchordlagu);

        menit = findViewById(R.id.SeekBarMenit);
        detik = findViewById(R.id.SeekBarDetik);
        textmenit = findViewById(R.id.TextMenit);
        textdetik = findViewById(R.id.TextDetik);

        cb1 = findViewById(R.id.checkjazz);
        cb2 = findViewById(R.id.checkpop);
        cb3 = findViewById(R.id.checkrnb);
        cb4 = findViewById(R.id.checkrock);
        cb5 = findViewById(R.id.checkhiphop);
        cb6 = findViewById(R.id.checkedm);
        cb7 = findViewById(R.id.checkakustik);
        cb8 = findViewById(R.id.checkblues);

        mudah = findViewById(R.id.radioButtonMudah);
        sedang = findViewById(R.id.radioButtonSedang);
        susah = findViewById(R.id.radioButtonSusah);

//        //get data dari intent
//        Intent intent = getIntent();
//        isEditMode = intent.getBooleanExtra("isEditMode", false);
//        if (isEditMode){
//            //update data
//
//            //get data
//            id = intent.getStringExtra("id");
//            judul_lagu = intent.getStringExtra("judul");
//            nama_penyanyi = intent.getStringExtra("penyanyi");
//            level_lagu = intent.getStringExtra("level");
//            detik_lagu = intent.getStringExtra("detik");
//            menit_lagu = intent.getStringExtra("menit");
//            chord_lagu = intent.getStringExtra("chord_lirik");
//            genre_lagu = intent.getStringExtra("genre");
//
//            //set data ke view
//            nama.setText(judul_lagu);
//            penyanyi.setText(nama_penyanyi);
//            chordl.setText(chord_lagu);
//            textmenit.setText(menit_lagu);
//            textdetik.setText(detik_lagu);
//
//            //set radio button checked sesuai value dari resep
//            if ("Mudah".equals(level_lagu)){
//                mudah.setChecked(true);
//            }else if ("Sedang".equals(level_lagu)){
//                sedang.setChecked(true);
//            }else if ("Sudah".equals(level_lagu)){
//                susah.setChecked(true);
//            }else {
//                mudah.setChecked(false);
//                sedang.setChecked(false);
//                susah.setChecked(false);
//            }
//
//            getCheckBoxes();
//        }
//        else {
//
//        }

        dbHelper = new DBHelper(this);

        textmenit.setText(menit.getProgress() + "");
        menit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int valMenit = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                valMenit = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textmenit.setText(valMenit + "");
            }
        });

        textdetik.setText(detik.getProgress() + "");
        detik.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int valDetik = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                valDetik = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textdetik.setText(valDetik + "");
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validasi()){
                    alert();
                }
            }
        });
    }

    private boolean validasi(){
        if (nama.getText().toString().length() == 0){
            nama.requestFocus();
            nama.setError("Nama Lagu harus diisi!");
            return false;
        }
        else if (penyanyi.getText().toString().length() == 0) {
            penyanyi.requestFocus();
            penyanyi.setError("Nama Lagu harus diisi!");
            return false;
        }
        else if (!mudah.isChecked() && !sedang.isChecked() && !susah.isChecked()){
            Toast.makeText(TambahChordActivity.this,"Pilih salah satu tingkat kesulitan Lagu!", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (chordl.getText().toString().length() == 0){
            chordl.requestFocus();
            chordl.setError("Chord dan Lagu harus diisi!");
            return false;
        }
        else if (!cb1.isChecked() && !cb2.isChecked() && !cb3.isChecked() && !cb4.isChecked() && !cb5.isChecked() && !cb6.isChecked() && !cb7.isChecked() && !cb8.isChecked()){
            Toast.makeText(TambahChordActivity.this,"Pilih salah satu genre lagu!", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }

    private void  alert(){
        level = findViewById(R.id.radioGroupLevel);

        int idRadio = level.getCheckedRadioButtonId();
        radio = findViewById(idRadio);

        judul_lagu = nama.getText().toString();
        nama_penyanyi = penyanyi.getText().toString();
        chord_lagu = chordl.getText().toString();
        level_lagu = radio.getText().toString();
        menit_lagu = textmenit.getText().toString();
        detik_lagu = textdetik.getText().toString();
        genre_lagu = "Genre lagu yang dipilih: ";

        if(cb1.isChecked()){
            genre_lagu += "\n-Jazz";
        }
        if(cb2.isChecked()){
            genre_lagu += "\n-Pop";
        }
        if(cb3.isChecked()){
            genre_lagu += "\n-RnB";
        }
        if(cb4.isChecked()){
            genre_lagu += "\n-Rock";
        }
        if(cb5.isChecked()){
            genre_lagu += "\n-Hip Hop";
        }
        if(cb6.isChecked()){
            genre_lagu += "\n-EDM";
        }
        if(cb7.isChecked()){
            genre_lagu += "\n-Akustik";
        }
        if(cb8.isChecked()){
            genre_lagu += "\n-Blues";
        }

        AlertDialog.Builder alertdialog=new AlertDialog.Builder(TambahChordActivity.this);
        alertdialog.setTitle("Apakah input anda sudah benar?");
        alertdialog.setMessage("Judul Lagu : "+judul_lagu+'\n'+'\n'+"Artist : "+nama_penyanyi+'\n'+'\n'+"Tingkat Kesulitan : "+level_lagu+'\n'+'\n'+"Chord Dan lirik : "+'\n'+chord_lagu+'\n'+'\n'+"Durasi : "+'\n'+menit_lagu+'\n'+'\n'+":"+detik_lagu+'\n'+'\n'+"Genre : "+genre_lagu);
        alertdialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                insertData();
//                submit();
            }
        });

        alertdialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = alertdialog.create();
        alert.setCanceledOnTouchOutside( true );
        alert.show();
    }

    private void insertData(){
        judul_lagu = nama.getText().toString();
        nama_penyanyi = penyanyi.getText().toString();
        chord_lagu = chordl.getText().toString();
        level_lagu = radio.getText().toString();
        menit_lagu = textmenit.getText().toString();
        detik_lagu = textdetik.getText().toString();
        genre_lagu = "";

        if(cb1.isChecked()){
            genre_lagu += "-Jazz ";
        }
        if(cb2.isChecked()){
            genre_lagu += "-Pop ";
        }
        if(cb3.isChecked()){
            genre_lagu += "-RnB ";
        }
        if(cb4.isChecked()){
            genre_lagu += "-Rock ";
        }
        if(cb5.isChecked()){
            genre_lagu += "-Hip Hop ";
        }
        if(cb6.isChecked()){
            genre_lagu += "-EDM ";
        }
        if(cb7.isChecked()){
            genre_lagu += "-Akustik ";
        }
        if(cb8.isChecked()) {
            genre_lagu += "-Blues ";
        }
//
//
//
//        if (isEditMode){ //update data
//
//            //penempatan sementara
//            updateToWebServe();
//
//            dbHelper.update(""+id,
//                    ""+judul_lagu,
//                    ""+nama_penyanyi,
//                    ""+genre_lagu,
//                    ""+level_lagu,
//                    ""+menit_lagu,
//                    ""+detik_lagu,
//                    ""+chord_lagu
//            );
//            Toast.makeText(this,"Data berhasil diupdate", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(this, DetailChordActivity.class);
//            intent.putExtra("id_chord", id);
//            startActivity(intent);
//        }
//        else { //insert data baru ke tabel

            //penempatan sementara
//            insertToWebServer();

            dbHelper.insert(
                    ""+judul_lagu,
                    ""+nama_penyanyi,
                    ""+genre_lagu,
                    ""+level_lagu,
                    ""+menit_lagu,
                    ""+detik_lagu,
                    ""+chord_lagu
            );
            Toast.makeText(this,"Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
//            Chord chord = new Chord(id, judul_lagu, nama_penyanyi, genre_lagu, level_lagu, menit_lagu, detik_lagu, chord_lagu);

            Chord chordNew = new Chord("0", judul_lagu, nama_penyanyi, genre_lagu, level_lagu,
                    menit_lagu, detik_lagu, chord_lagu);

            Intent intent = new Intent(this, DetailChordActivity.class);
            intent.putExtra("asal", "add");
            intent.putExtra("chord", chordNew);
            startActivity(intent);
//        }
    }

    public void getCheckBoxes(){
        cbs = new ArrayList<>();
        String[] benefit = genre_lagu.split("-");
        if(benefit.length > 1){
            cb1.setChecked(true);
            cbs.add(cb1.getText().toString());
        }
        if(benefit.length > 2){
            cb2.setChecked(true);
            cbs.add(cb2.getText().toString());
        }
        if(benefit.length > 3){
            cb3.setChecked(true);
            cbs.add(cb3.getText().toString());
        }
        if(benefit.length > 4){
            cb4.setChecked(true);
            cbs.add(cb4.getText().toString());
        }
        if(benefit.length > 5){
            cb5.setChecked(true);
            cbs.add(cb5.getText().toString());
        }
        if(benefit.length > 6){
            cb6.setChecked(true);
            cbs.add(cb6.getText().toString());
        }
        if(benefit.length > 7){
            cb7.setChecked(true);
            cbs.add(cb7.getText().toString());
        }
        if(benefit.length > 8){
            cb8.setChecked(true);
            cbs.add(cb8.getText().toString());
        }
    }

    private void insertToWebServer(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constant.ADD_CHORD;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject object = new JSONObject(response);
                            if(object.getBoolean("success")){
                                Intent intent = new Intent(TambahChordActivity.this, MainActivity.class);
                                startActivity(intent);
//                                Toast.makeText(getApplicationContext(), "Add Data Success", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TambahChordActivity.this,"Add Gagal",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
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
//
//    public void updateToWebServe(){
//        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url =Constant.UPDATE_CHORD;
//
//        // Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        try {
//                            JSONObject object = new JSONObject(response);
//                            if(object.getBoolean("success")){
//                                Intent intent = new Intent(TambahChordActivity.this, MainActivity.class);
//                                startActivity(intent);
//                                Toast.makeText(getApplicationContext(), "Update Data Success", Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(TambahChordActivity.this,"Update Gagal",Toast.LENGTH_SHORT).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> map = new HashMap<>();
//                map.put("id",id);
//                map.put("judul",judul_lagu);
//                map.put("penyanyi", nama_penyanyi);
//                map.put("level",level_lagu);
//                map.put("genre", genre_lagu);
//                map.put("durasi_menit", menit_lagu);
//                map.put("durasi_detik", detik_lagu);
//                map.put("chord_dan_lirik", chord_lagu);
//                return map;
//            }
//        };
//
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest);
//    }
}
