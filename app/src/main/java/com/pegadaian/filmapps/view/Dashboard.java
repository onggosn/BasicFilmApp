package com.pegadaian.filmapps.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pegadaian.filmapps.R;
import com.pegadaian.filmapps.adapter.FilmAdapter;
import com.pegadaian.filmapps.database.FilmDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

public class Dashboard extends AppCompatActivity {

    private RecyclerView rv;

    FilmDatabase filmDatabase = new FilmDatabase(this);
    String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, FormInput.class);
                startActivity(intent);
            }
        });
        isStoragePermissionGranted();
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    public void getData() {
        rv = findViewById(R.id.rvFilm);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        FilmAdapter filmAdapter = new FilmAdapter(filmDatabase.getListData(), this);
        rv.setAdapter(filmAdapter);
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
}

