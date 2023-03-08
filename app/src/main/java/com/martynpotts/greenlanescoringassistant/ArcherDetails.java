package com.martynpotts.greenlanescoringassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.martynpotts.greenlanescoringassistant.databinding.ActivityArcherDetailsBinding;

public class ArcherDetails extends DrawerBaseActivity {


    ActivityArcherDetailsBinding activityArcherDetailsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityArcherDetailsBinding = ActivityArcherDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityArcherDetailsBinding.getRoot());
        allocateActivityTitle("Archer Details");
    }
}