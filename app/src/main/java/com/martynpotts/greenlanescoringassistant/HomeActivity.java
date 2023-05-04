package com.martynpotts.greenlanescoringassistant;


import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.martynpotts.greenlanescoringassistant.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends DrawerBaseActivity {

    ActivityMainBinding activityMainBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        allocateActivityTitle("Home");

        RecyclerView roundView = findViewById(R.id.recPreviousRounds);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        roundView.setLayoutManager(linearLayoutManager);
        roundView.setHasFixedSize(true);
        DBHandler mDatabase = new DBHandler(this);
        ArrayList<Round> listRounds = mDatabase.listRounds();
        if (listRounds.size() > 0) {
            roundView.setVisibility(View.VISIBLE);
            RoundAdapter mAdapter = new RoundAdapter(this,listRounds);
            roundView.setAdapter(mAdapter);
        }
        else {
            roundView.setVisibility(View.GONE);
            Toast.makeText(this, "There are no rounds in the database. Start shooting now", Toast.LENGTH_LONG).show();
        }


        ImageButton launchSelectRound = (ImageButton) findViewById(R.id.ibtnSelectRound);
        launchSelectRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,SelectRound.class);
                startActivity(intent);
            }
        });

    }







}