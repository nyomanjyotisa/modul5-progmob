package com.example.praktikum.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.praktikum.model.Chord;
import com.example.praktikum.DetailChordActivity;
import com.example.praktikum.R;
import com.example.praktikum.helper.DBHelper;

import java.util.List;

public class ChordAdapter extends RecyclerView.Adapter<ChordAdapter.ViewHolder> {
    private Context context;
    List<Chord> listChord;
    DBHelper databaseHelper;

    //constructor
    public ChordAdapter(Context context, List<Chord> listChord) {
        this.context = context;
        this.listChord = listChord;

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
        Chord chord = listChord.get(position);
        String id = chord.getId();
        String judul = chord.getJudul();
        String penyanyi = chord.getPenyanyi();
        String level = chord.getLevel();
        String lamaMenit = chord.getDurasiMenit();
        String lamaDetik = chord.getDurasiDetik();

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
                intent.putExtra("id_chord", id);
                intent.putExtra("asal", "list");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listChord.size();
    }
}
