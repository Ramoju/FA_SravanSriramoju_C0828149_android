package com.example.fa_sravansriramoju_c0828149_android;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fa_sravansriramoju_c0828149_android.Adapter.PlacesRVAdapter;
import com.example.fa_sravansriramoju_c0828149_android.Model.Place;
import com.example.fa_sravansriramoju_c0828149_android.utilities.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FloatingActionButton addPlace;
    private DBHelper db;
    private List<Place> placesList;
    private PlacesRVAdapter adapter;
    //ActivityResultLauncher<Place> place;
    private static final int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.placesRV);
        addPlace = findViewById(R.id.addplace);
        db = new DBHelper(this);
        placesList = new ArrayList<>();
        adapter = new PlacesRVAdapter(MainActivity.this, db);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        placesList = db.getAllPlaces();
        Collections.reverse(placesList);
        adapter.setPlaces(placesList);

        addPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayMapActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        placesList = db.getAllPlaces();
        Collections.reverse(placesList);
        adapter.setPlaces(placesList);
        adapter.notifyDataSetChanged();
    }
}