package com.example.praktikum.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.praktikum.Constant;
import com.example.praktikum.MainActivity;
import com.example.praktikum.R;
import com.example.praktikum.TambahChordActivity;
import com.example.praktikum.adapter.DBAdapter;
import com.example.praktikum.databinding.FragmentHomeBinding;
import com.example.praktikum.helper.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private DBHelper dbHelper;
    private Button btnTambah;
    private RecyclerView listChord;

    private HomeViewModel homeViewModel;
    public HomeFragment(){
        // require a empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
//        dbHelper = new DBHelper(getContext());
        View root =inflater.inflate(R.layout.fragment_home, container, false);
//        btnTambah = root.findViewById(R.id.btnTambah);
//        listChord = root.findViewById(R.id.list_chord);
//
//        loadRecords();

//        btnTambah.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), TambahChordActivity.class);
//                intent.putExtra("isEditMode", false);
//                startActivity(intent);
//            }
//        });

        return root;
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        dbHelper = new DBHelper(getContext());
        listChord = view.findViewById(R.id.list_chord);

        loadRecords();
    }

    private void loadRecords() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());
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
                                DBAdapter adapter = new DBAdapter(getContext(), dbHelper.getChord());
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
                Toast.makeText(getContext(),"No Connection",Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

//@Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
}