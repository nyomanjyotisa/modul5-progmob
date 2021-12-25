package com.example.praktikum.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.praktikum.Chord;
import com.example.praktikum.Constant;
import com.example.praktikum.DetailChordActivity;
import com.example.praktikum.EditChordActivity;
import com.example.praktikum.MainActivity;
import com.example.praktikum.MyChordActivity;
import com.example.praktikum.R;
import com.example.praktikum.helper.DBHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChordAdapter extends RecyclerView.Adapter<ChordAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Chord> arrayList;
    DBHelper databaseHelper;

    //constructor
    public ChordAdapter(Context context, ArrayList<Chord> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

        databaseHelper = new DBHelper(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textJudulLagu, textLevelLagu, textLamaMenit, textLamaDetik, textArtist;

        public ViewHolder(@NonNull View view) {
            super(view);

            textJudulLagu = view.findViewById(R.id.text_judul_lagu);
            textLevelLagu = view.findViewById(R.id.text_level);
            textArtist = view.findViewById(R.id.text_artist);
            textLamaMenit = view.findViewById(R.id.text_lama_menit);
            textLamaDetik = view.findViewById(R.id.text_detik);
        }
    }

    @NonNull
    @Override
    public ChordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //infalate layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_chord, parent, false);
        return new ViewHolder(view);
    }

    //get data, set data, handle view click in method
    @Override
    public void onBindViewHolder(@NonNull ChordAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        //get data
        Chord chord = arrayList.get(position);
        String id_chord = chord.getId();
        String judul = chord.getJudul();
        String penyanyi = chord.getPenyanyi();
        String genre = chord.getGenre();
        String level = chord.getLevel();
        String lamaMenit = chord.getDurasiMenit();
        String lamaDetik = chord.getDurasiDetik();
        String chordLirik = chord.getChordLirik();

        //set data ke views
        holder.textJudulLagu.setText(judul);
        holder.textLevelLagu.setText(level);
        holder.textArtist.setText(penyanyi);
        holder.textLamaMenit.setText(lamaMenit);
        holder.textLamaDetik.setText(lamaDetik);

        //handle klik item (diarahkan ke activity detail saat klik item)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass record id to next activity to show details of that records
                Intent intent = new Intent(context, DetailChordActivity.class);
                Chord chordNew = new Chord(id_chord, judul, penyanyi, genre, level,
                        lamaMenit, lamaDetik, chordLirik);
                intent.putExtra("chord", chordNew);
                intent.putExtra("asal", "list");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
