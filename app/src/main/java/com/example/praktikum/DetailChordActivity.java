package com.example.praktikum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.praktikum.adapter.CommentAdapter;
import com.example.praktikum.api.Constant;
import com.example.praktikum.helper.DBHelper;
import com.example.praktikum.model.Chord;
import com.example.praktikum.model.CommentModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailChordActivity extends AppCompatActivity {
    private TextView judulText, penyanyiText, chordLaguText, levelText, durasimenitText, durasidetikText;
//    private ImageView foto_resep;
    private String idChord, genre, asal;
    private DBHelper dbHelper;
    private FloatingActionButton btnBack, btnMore;
    private TextView ratingLabel, labelComments, genre1, genre2, genre3, genre4, genre5, genre6, genre7, genre8, labelAddComment;
    private ArrayList<CommentModel> commentArrayList  = new ArrayList<>();
    private RecyclerView listComment;
    private CommentModel commentModel;
    private SeekBar seekRating;
    private EditText etComment;
    private Button submitComment;
    Intent intent;
    Chord chord;
    RelativeLayout editTextCommentLayout;
    SharedPreferences sharedPreferencesLogin;
    String id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_chord);

//        foto_resep = findViewById(R.id.fotoResep);

        sharedPreferencesLogin = getSharedPreferences("loginPre", MODE_PRIVATE);
        id_user = sharedPreferencesLogin.getString("id_user", "");

        judulText = findViewById(R.id.textJudulLagu);
        penyanyiText = findViewById(R.id.textPenyanyi);
        chordLaguText = findViewById(R.id.textChordLagu);
        levelText = findViewById(R.id.textLevel);
        durasimenitText = findViewById(R.id.textMenit);
        durasidetikText = findViewById(R.id.textDetik);

        btnBack = findViewById(R.id.btn_Back);
        btnMore = findViewById(R.id.btn_More);

        // get id record dari adapter melalui intent
        intent = getIntent();
        asal = intent.getStringExtra("asal");
        chord = intent.getParcelableExtra("chord");
        idChord = chord.getId();

        Log.v("tessss", chord.getJudul());


        seekRating = findViewById(R.id.SeekBarRating);
        etComment = findViewById(R.id.editTextComment);
        submitComment = findViewById(R.id.buttonSubmit);
        labelAddComment = findViewById(R.id.labelAddComment);
        listComment = findViewById(R.id.list_comment);
        labelComments = findViewById(R.id.labelComments);
        ratingLabel = findViewById(R.id.ratingLabel);
        editTextCommentLayout = findViewById(R.id.editTextCommentLayout);

        if(asal.equals("add")){
            fromAdd();
            seekRating.setVisibility(View.INVISIBLE);
            etComment.setVisibility(View.INVISIBLE);
            submitComment.setVisibility(View.INVISIBLE);
            labelAddComment.setVisibility(View.INVISIBLE);
            listComment.setVisibility(View.INVISIBLE);
            labelComments.setVisibility(View.INVISIBLE);
            ratingLabel.setVisibility(View.INVISIBLE);
            editTextCommentLayout.setVisibility(View.INVISIBLE);
        }else{
            fromAdd();
//            dbHelper = new DBHelper(this);

//            showDetailChord();


            submitComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    storeComment();
                }
            });

            loadComments();
        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(asal.equals("add")){
                    Intent intent = new Intent(DetailChordActivity.this, MyChordActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(DetailChordActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

//        Bundle bundle = intent.getExtras();
//
//        Chord chord;
//
//        if (bundle != null){
//            chord = bundle.getParcelable("chord");
//            judulText.setText(chord.getJudul());
//            penyanyiText.setText(chord.getPenyanyi());
//            genreText.setText(chord.getGenre());
//            levelText.setText(chord.getLevel());
//            durasimenitText.setText(chord.getDurasiMenit());
//            durasidetikText.setText(chord.getDurasiDetik());
//            chordLaguText.setText(chord.getChordLirik());
//        }
    }

    private void showDetailChord() {
        String QUERY = "SELECT * FROM chord WHERE id = "+idChord;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(QUERY, null);

        // ngecek db ada apa ngga record detailnya
        if (cursor.moveToFirst()){
            do {
                String id = ""+ cursor.getInt(cursor.getColumnIndex("id"));
                String judul = ""+ cursor.getString(cursor.getColumnIndex("judul"));
                String penyanyi = ""+ cursor.getString(cursor.getColumnIndex("penyanyi"));
                genre = ""+ cursor.getString(cursor.getColumnIndex("genre"));
                String level = ""+ cursor.getString(cursor.getColumnIndex("level"));
                String durasimenit = ""+ cursor.getString(cursor.getColumnIndex("durasi_menit"));
                String durasidetik = ""+ cursor.getString(cursor.getColumnIndex("durasi_detik"));
                String chord = ""+ cursor.getString(cursor.getColumnIndex("chord_lirik"));

                judulText.setText(judul);
                penyanyiText.setText(penyanyi);
                chordLaguText.setText(chord);
                levelText.setText(level);
                durasimenitText.setText(durasimenit);
                durasidetikText.setText(durasidetik);
                genreSetter();

                btnMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] options = {"Edit", "Delete"};

                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailChordActivity.this);
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //tombol edit diklik
                                if (which==0){
                                    //tombol edit diklik
                                    Intent intent = new Intent(DetailChordActivity.this, TambahChordActivity.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("judul", judul);
                                    intent.putExtra("penyanyi", penyanyi);
                                    intent.putExtra("chord", chord);
                                    intent.putExtra("level", level);
                                    intent.putExtra("textmenit", durasimenit);
                                    intent.putExtra("textdetik", durasidetik);
                                    intent.putExtra("genre", genre);
                                    intent.putExtra("isEditMode", true);
                                    startActivity(intent);
                                }
                                //tombol delete diklik
                                else if (which==1){
                                    dbHelper.delete(id);
                                    Intent intent = new Intent(DetailChordActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
                        builder.create().show();
                    }
                });

            }while (cursor.moveToNext());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(asal.equals("add")){
            Intent intent = new Intent(this, MyChordActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void genreSetter() {
        genre1 = findViewById(R.id.textGenre1);
        genre2 = findViewById(R.id.textGenre2);
        genre3 = findViewById(R.id.textGenre3);
        genre4 = findViewById(R.id.textGenre4);
        genre5 = findViewById(R.id.textGenre5);
        genre6 = findViewById(R.id.textGenre6);
        genre7 = findViewById(R.id.textGenre7);
        genre8 = findViewById(R.id.textGenre8);

        String[] iniGenre;

        iniGenre = chord.getGenre().split("-");

        if(iniGenre.length > 1){
            genre1.setVisibility(View.VISIBLE);
            genre1.setText(iniGenre[1]);
        }
        if(iniGenre.length > 2){
            genre2.setVisibility(View.VISIBLE);
            genre2.setText(iniGenre[2]);
        }
        if(iniGenre.length > 3){
            genre3.setVisibility(View.VISIBLE);
            genre3.setText(iniGenre[3]);
        }
        if(iniGenre.length > 4){
            genre4.setVisibility(View.VISIBLE);
            genre4.setText(iniGenre[4]);
        }
        if(iniGenre.length > 5){
            genre5.setVisibility(View.VISIBLE);
            genre5.setText(iniGenre[5]);
        }
        if(iniGenre.length > 6){
            genre6.setVisibility(View.VISIBLE);
            genre6.setText(iniGenre[6]);
        }
        if(iniGenre.length > 7){
            genre7.setVisibility(View.VISIBLE);
            genre7.setText(iniGenre[7]);
        }
        if(iniGenre.length > 8){
            genre8.setVisibility(View.VISIBLE);
            genre8.setText(iniGenre[8]);
        }
    }

    private void loadComments() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constant.COMMENTS;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")){
                                JSONArray array = new JSONArray(object.getString("data"));
                                for(int i=0;i<array.length();i++){
                                    JSONObject chordObject = array.getJSONObject(i);
                                    commentModel = new CommentModel(chordObject.getInt("id"),
                                            chordObject.getInt("id_user"),
                                            chordObject.getInt("id_chord"),
                                            chordObject.getInt("rating"),
                                            chordObject.getString("comment"),
                                            chordObject.getString("username"));

                                    commentArrayList.add(commentModel);
                                }
                                CommentAdapter adapter = new CommentAdapter(DetailChordActivity.this, commentArrayList);
                                listComment.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Display the first 500 characters of the response string.

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailChordActivity.this,"No Connection",Toast.LENGTH_SHORT).show();
            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id_chord",idChord);
                return map;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void storeComment(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constant.ADD_COMMENT;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")){
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                                Toast.makeText(DetailChordActivity.this,"Add comment success",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Display the first 500 characters of the response string.

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailChordActivity.this,"No Connection",Toast.LENGTH_SHORT).show();
            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id_user", id_user);
                map.put("id_chord", idChord);
                map.put("rating", String.valueOf(seekRating.getProgress()));
                map.put("comment", etComment.getText().toString());
                return map;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void fromAdd(){
        Log.v("bas", chord.getJudul());
        judulText.setText(chord.getJudul());
        penyanyiText.setText(chord.getPenyanyi());
        chordLaguText.setText(chord.getChordLirik());
        levelText.setText(chord.getLevel());
        durasimenitText.setText(chord.getDurasiMenit());
        durasidetikText.setText(chord.getDurasiDetik());
        genreSetter();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(DetailChordActivity.this,"onRestart",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(DetailChordActivity.this,"onResume",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(DetailChordActivity.this,"onStop",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(DetailChordActivity.this,"onDestroy",Toast.LENGTH_SHORT).show();
    }
}