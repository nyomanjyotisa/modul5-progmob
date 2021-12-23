package com.example.praktikum.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.praktikum.R;
import com.example.praktikum.helper.DBHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBAdapter extends RecyclerView.Adapter<DBAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Chord> arrayList;
    DBHelper databaseHelper;

    //constructor
    public DBAdapter(Context context, ArrayList<Chord> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

        databaseHelper = new DBHelper(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textJudulLagu, textLevelLagu, textLamaMenit, textLamaDetik, textArtist;
        ImageButton moreBtn;

        public ViewHolder(@NonNull View view) {
            super(view);

            textJudulLagu = view.findViewById(R.id.text_judul_lagu);
            textLevelLagu = view.findViewById(R.id.text_level);
            textArtist = view.findViewById(R.id.text_artist);
            textLamaMenit = view.findViewById(R.id.text_lama_menit);
            textLamaDetik = view.findViewById(R.id.text_detik);
            moreBtn = view.findViewById(R.id.moreBtn);
        }
    }

    @NonNull
    @Override
    public DBAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //infalate layout
        View view = LayoutInflater.from(context).inflate(R.layout.list_chord, parent, false);
        return new ViewHolder(view);
    }

    //get data, set data, handle view click in method
    @Override
    public void onBindViewHolder(@NonNull DBAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        //get data
        Chord chord = arrayList.get(position);
        String id = chord.getId();
        String judul = chord.getJudul();
        String penyanyi = chord.getPenyanyi();
        String genre = chord.getGenre();
        String level = chord.getLevel();
        String lamaMenit = chord.getDurasiMenit();
        String lamaDetik = chord.getDurasiDetik();
        String chordLirik = chord.getChordLirik();
//        Resep resep = new Resep(id, nama, foto, porsi, bahan, tahap, level, lama, kategori);

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
                context.startActivity(intent);
            }
        });

        //handle tombol more untuk edit, delete
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreDialog(
                       ""+position,
                        ""+id,
                        ""+judul,
                        ""+penyanyi,
                        ""+genre,
                        ""+level,
                        ""+lamaMenit,
                        ""+lamaDetik,
                        ""+chordLirik);
            }
        });
//        holder.nama_resep.setText(arrayList.get(position).getNama_resep());
//        holder.kategori.setText(arrayList.get(position).getInputCb());
//        databaseHelper = new DBHelper(context);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void showMoreDialog(String position, String id, String judul, String penyanyi, String genre, String level, String lamaMenit, String lamaDetik, String chordLirik){
        String[] options = {"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //tombol edit diklik
                if (which==0){
                    //tombol edit diklik
                    Intent intent = new Intent(context, EditChordActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("judul", judul);
                    intent.putExtra("penyanyi", penyanyi);
                    intent.putExtra("genre", genre);
                    intent.putExtra("level", level);
                    intent.putExtra("durasi_menit", lamaMenit);
                    intent.putExtra("durasi_detik", lamaDetik);
                    intent.putExtra("chord_lirik", chordLirik);
                    context.startActivity(intent);
                }
                //tombol delete diklik
                else if (which==1){
                    databaseHelper.delete(id);
                    deleteOnWebServer(id);
                    ((MainActivity)context).onResume();
                }
            }
        });
        builder.create().show();
    }

    public void deleteOnWebServer(String id){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Constant.DELETE_CHORD;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("success")){
                                Toast.makeText(context,"Delete Verhasil",Toast.LENGTH_SHORT).show();
                                ((MainActivity)context).onResume();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Delete GAGAL",Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("id",id);
                return map;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
