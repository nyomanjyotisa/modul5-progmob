package com.example.praktikum;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.praktikum.helper.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetailChordActivity extends AppCompatActivity {
    private TextView judulText, penyanyiText, chordLaguText, levelText, durasimenitText, durasidetikText, genreText;
//    private ImageView foto_resep;
    private String idChord;
    private DBHelper dbHelper;
    private FloatingActionButton btnBack, btnMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_chord);

//        foto_resep = findViewById(R.id.fotoResep);

        judulText = findViewById(R.id.textJudulLagu);
        penyanyiText = findViewById(R.id.textPenyanyi);
        chordLaguText = findViewById(R.id.textChordLagu);
        levelText = findViewById(R.id.textLevel);
        durasimenitText = findViewById(R.id.textMenit);
        durasidetikText = findViewById(R.id.textDetik);
        genreText = findViewById(R.id.textGenre);

        btnBack = findViewById(R.id.btn_Back);
        btnMore = findViewById(R.id.btn_More);

        // get id record dari adapter melalui intent
        Intent intent = getIntent();
        idChord = intent.getStringExtra("id_chord");

        dbHelper = new DBHelper(this);

        showDetailChord();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailChordActivity.this, MainActivity.class);
                startActivity(intent);
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
                String genre = ""+ cursor.getString(cursor.getColumnIndex("genre"));
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
                genreText.setText(genre);

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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}