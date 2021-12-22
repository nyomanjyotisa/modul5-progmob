package com.example.praktikum.ui.add;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.praktikum.Constant;
import com.example.praktikum.DashboardActivity;
import com.example.praktikum.DetailChordActivity;
import com.example.praktikum.MainActivity;
import com.example.praktikum.R;
import com.example.praktikum.TambahChordActivity;
import com.example.praktikum.databinding.FragmentAddChordBinding;
import com.example.praktikum.helper.DBHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddFragment extends Fragment {

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

    private DBHelper dbHelper;

    private AddViewModel addViewModel;
    public AddFragment(){
        // require a empty public constructor
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        addViewModel = new ViewModelProvider(this).get(AddViewModel.class);

        return inflater.inflate(R.layout.fragment_add_chord, container, false);

//        binding = FragmentAddChordBinding.inflate(inflater, container, false);

//        final TextView textView = binding.textDashboard;
//        addViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        btnSubmit = view.findViewById(R.id.buttonSubmit);


        nama = view.findViewById(R.id.editTextNamaLagu);
        penyanyi = view.findViewById(R.id.editTextNamaPenyanyi);
        chordl = view.findViewById(R.id.editTextchordlagu);

        menit = view.findViewById(R.id.SeekBarMenit);
        detik = view.findViewById(R.id.SeekBarDetik);
        textmenit = view.findViewById(R.id.TextMenit);
        textdetik = view.findViewById(R.id.TextDetik);

        cb1 = view.findViewById(R.id.checkjazz);
        cb2 = view.findViewById(R.id.checkpop);
        cb3 = view.findViewById(R.id.checkrnb);
        cb4 = view.findViewById(R.id.checkrock);
        cb5 = view.findViewById(R.id.checkhiphop);
        cb6 = view.findViewById(R.id.checkedm);
        cb7 = view.findViewById(R.id.checkakustik);
        cb8 = view.findViewById(R.id.checkblues);

        mudah = view.findViewById(R.id.radioButtonMudah);
        sedang = view.findViewById(R.id.radioButtonSedang);
        susah = view.findViewById(R.id.radioButtonSusah);

        dbHelper = new DBHelper(getContext());

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
                    level = view.findViewById(R.id.radioGroupLevel);

                    int idRadio = level.getCheckedRadioButtonId();
                    radio = view.findViewById(idRadio);

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

                    AlertDialog.Builder alertdialog=new AlertDialog.Builder(getContext());
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
            Toast.makeText(getContext(),"Pilih salah satu tingkat kesulitan Lagu!", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (chordl.getText().toString().length() == 0){
            chordl.requestFocus();
            chordl.setError("Chord dan Lagu harus diisi!");
            return false;
        }
        else if (!cb1.isChecked() && !cb2.isChecked() && !cb3.isChecked() && !cb4.isChecked() && !cb5.isChecked() && !cb6.isChecked() && !cb7.isChecked() && !cb8.isChecked()){
            Toast.makeText(getContext(),"Pilih salah satu genre lagu!", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
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

        //insert data baru ke tabel

        //penempatan sementara
        insertToWebServer();

        dbHelper.insert(
                ""+judul_lagu,
                ""+nama_penyanyi,
                ""+genre_lagu,
                ""+level_lagu,
                ""+menit_lagu,
                ""+detik_lagu,
                ""+chord_lagu
        );
        Toast.makeText(getContext(),"Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
//            Chord chord = new Chord(id, judul_lagu, nama_penyanyi, genre_lagu, level_lagu, menit_lagu, detik_lagu, chord_lagu);
        Intent intent = new Intent(getActivity(), DashboardActivity.class);
//            intent.putExtra("chord", (Parcelable) chord);

        startActivity(intent);

    }

    private void insertToWebServer(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());
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
                                Intent intent = new Intent(getActivity(), DashboardActivity.class);
                                startActivity(intent);
                                Toast.makeText(getContext(), "Add Data Success", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Add Gagal",Toast.LENGTH_SHORT).show();
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
}