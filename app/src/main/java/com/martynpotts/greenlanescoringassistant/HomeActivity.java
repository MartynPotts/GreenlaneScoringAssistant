package com.martynpotts.greenlanescoringassistant;


import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.martynpotts.greenlanescoringassistant.databinding.ActivityMainBinding;

public class HomeActivity extends DrawerBaseActivity {

    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        allocateActivityTitle("Home");

        FloatingActionButton launchSelectRound = (FloatingActionButton) findViewById(R.id.btnSelectRound);
        launchSelectRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,SelectRound.class);
                startActivity(intent);
            }
        });

    }




}