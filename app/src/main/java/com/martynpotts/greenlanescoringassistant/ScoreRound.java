package com.martynpotts.greenlanescoringassistant;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.martynpotts.greenlanescoringassistant.databinding.ActivityScoreRoundBinding;
import com.martynpotts.greenlanescoringassistant.databinding.ActivitySelectRoundBinding;

public class ScoreRound extends DrawerBaseActivity {

    ActivityScoreRoundBinding activityScoreRoundBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityScoreRoundBinding = ActivityScoreRoundBinding.inflate(getLayoutInflater());
        setContentView(activityScoreRoundBinding.getRoot());
        allocateActivityTitle("Select Round");
    }
}