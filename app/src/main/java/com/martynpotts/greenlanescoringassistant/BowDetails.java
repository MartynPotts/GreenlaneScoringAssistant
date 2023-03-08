package com.martynpotts.greenlanescoringassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.martynpotts.greenlanescoringassistant.databinding.ActivityBowDetailsBinding;

public class BowDetails extends DrawerBaseActivity {

    ActivityBowDetailsBinding activityBowDetailsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBowDetailsBinding = ActivityBowDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityBowDetailsBinding.getRoot());
        allocateActivityTitle("Bow Details");
    }
}