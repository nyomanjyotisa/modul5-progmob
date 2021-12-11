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

import com.example.praktikum.adapter.DBAdapter;
import com.example.praktikum.helper.DBHelper;


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
        DBAdapter adapter = new DBAdapter(MainActivity.this, dbHelper.getChord());
        listChord.setAdapter(adapter);

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