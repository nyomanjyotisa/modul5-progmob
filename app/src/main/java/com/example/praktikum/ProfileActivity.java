package com.example.praktikum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.praktikum.helper.DBHelper;

public class ProfileActivity extends AppCompatActivity {
    private DBHelper dbHelper = new DBHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView tvMychord = findViewById(R.id.mychord);
        Button btnLogout = findViewById(R.id.logout);

        tvMychord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(ProfileActivity.this, MyChordActivity.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.deleteSemua();

                SharedPreferences sharedPreferencesLogin = getSharedPreferences("loginPre", MODE_PRIVATE);
                sharedPreferencesLogin.edit().clear().commit();

                String test = sharedPreferencesLogin.getString("id_user", "");
                Log.v("aaa", test);
                Log.v("bbb", "ccc");
                Intent intent =new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}