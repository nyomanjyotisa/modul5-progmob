package com.example.praktikum;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.praktikum.databinding.FragmentAddChordBinding;
import com.example.praktikum.databinding.FragmentHomeBinding;
import com.example.praktikum.databinding.FragmentProfileBinding;
import com.example.praktikum.ui.add.AddFragment;
import com.example.praktikum.ui.home.HomeFragment;
import com.example.praktikum.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bottomNavigationView = findViewById(R.id.nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

    }

    HomeFragment fragmentHome = new HomeFragment();
    AddFragment fragmentAddChord = new AddFragment();
    ProfileFragment fragmentProfile = new ProfileFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentHome).commit();
                return true;

            case R.id.navigation_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentProfile).commit();
                return true;

            case R.id.navigation_add:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentAddChord).commit();
                return true;
        }
        return false;
    }
    @Override
    public void onResume() {
        super.onResume();
    }
}