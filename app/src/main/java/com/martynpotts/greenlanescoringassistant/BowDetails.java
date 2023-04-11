package com.martynpotts.greenlanescoringassistant;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import com.martynpotts.greenlanescoringassistant.databinding.ActivityBowDetailsBinding;

public class BowDetails extends DrawerBaseActivity {

    ActivityBowDetailsBinding activityBowDetailsBinding;

    private EditText bowType, poundage, bracingHeight, sightMark20yrds, sightMark30yrds
            , sightMark40yrds, sightMark50yrds, sightMark60yrds, sightMark80yrds
            , sightMark100yrds, sightMark30mtrs, sightMark50mtrs, sightMark70mtrs
            , sightMark90mtrs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBowDetailsBinding = ActivityBowDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityBowDetailsBinding.getRoot());
        allocateActivityTitle("Bow Details");

        bowType = findViewById(R.id.edtBowType);
        poundage = findViewById(R.id.edtPoundage);
        bracingHeight = findViewById(R.id.edtBracingHeight);

        sightMark20yrds = findViewById(R.id.edt20YardsSightMark);
        sightMark30yrds = findViewById(R.id.edt30YardsSightMark);
        sightMark40yrds = findViewById(R.id.edt40YardsSightMark);
        sightMark50yrds = findViewById(R.id.edt50YardsSightMark);
        sightMark60yrds = findViewById(R.id.edt60YardsSightMark);
        sightMark80yrds = findViewById(R.id.edt80YardsSightMark);
        sightMark100yrds = findViewById(R.id.edt100YardsSightMark);

        sightMark30mtrs = findViewById(R.id.edt30MetreSightMark);
        sightMark50mtrs = findViewById(R.id.edt50MetreSightMark);
        sightMark70mtrs = findViewById(R.id.edt70MetreSightMark);
        sightMark90mtrs = findViewById(R.id.edt90MetreSightMark);
    }

    public void savePreferences(){
        SharedPreferences sp = getSharedPreferences("SharedPreferences",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sp.edit();

        myEdit.putString("BowType", bowType.getText().toString() );
        try {
            myEdit.putInt("Poundage", Integer.parseInt(poundage.getText().toString()));

            myEdit.putFloat("BracingHeight", Float.parseFloat(bracingHeight.getText().toString()));

            myEdit.putFloat("SightMarks20yrds", Float.parseFloat(sightMark20yrds.getText().toString()));
            myEdit.putFloat("SightMarks30yrds", Float.parseFloat(sightMark30yrds.getText().toString()));
            myEdit.putFloat("SightMarks40yrds", Float.parseFloat(sightMark40yrds.getText().toString()));
            myEdit.putFloat("SightMarks50yrds", Float.parseFloat(sightMark50yrds.getText().toString()));
            myEdit.putFloat("SightMarks60yrds", Float.parseFloat(sightMark60yrds.getText().toString()));
            myEdit.putFloat("SightMarks80yrds", Float.parseFloat(sightMark80yrds.getText().toString()));
            myEdit.putFloat("SightMarks100yrds", Float.parseFloat(sightMark100yrds.getText().toString()));

            myEdit.putFloat("SightMarks30mtrs", Float.parseFloat(sightMark30mtrs.getText().toString()));
            myEdit.putFloat("SightMarks50mtrs", Float.parseFloat(sightMark50mtrs.getText().toString()));
            myEdit.putFloat("SightMarks70mtrs", Float.parseFloat(sightMark70mtrs.getText().toString()));
            myEdit.putFloat("SightMarks90mtrs", Float.parseFloat(sightMark90mtrs.getText().toString()));

        } catch (NumberFormatException nfe){
            myEdit.putInt("Poundage", 0);

            myEdit.putFloat("BracingHeight",0F);

            myEdit.putFloat("SightMarks20yrds",0F);
        }


        myEdit.apply();
    }



    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        String BowTypeString = sp.getString("BowType","");
        int poundageInt = sp.getInt("Poundage", 0);

        float bracingHeightFloat = sp.getFloat("BracingHeight",0F);

        float sightMarks20yrdsFloat = sp.getFloat("SightMarks20yrds", 0F);
        float sightMarks30yrdsFloat = sp.getFloat("SightMarks30yrds", 0F);
        float sightMarks40yrdsFloat = sp.getFloat("SightMarks40yrds", 0F);
        float sightMarks50yrdsFloat = sp.getFloat("SightMarks50yrds", 0F);
        float sightMarks60yrdsFloat = sp.getFloat("SightMarks60yrds", 0F);
        float sightMarks80yrdsFloat = sp.getFloat("SightMarks80yrds", 0F);
        float sightMarks100yrdsFloat = sp.getFloat("SightMarks100yrds", 0F);

        float sightMarks30mtrsFloat = sp.getFloat("SightMarks30mtrs", 0F);
        float sightMarks50mtrsFloat = sp.getFloat("SightMarks50mtrs", 0F);
        float sightMarks70mtrsFloat = sp.getFloat("SightMarks70mtrs", 0F);
        float sightMarks90mtrsFloat = sp.getFloat("SightMarks90mtrs", 0F);

        bowType.setText(BowTypeString);
        poundage.setText(String.valueOf(poundageInt));

        bracingHeight.setText(String.valueOf(bracingHeightFloat));

        sightMark20yrds.setText(String.valueOf(sightMarks20yrdsFloat));
        sightMark30yrds.setText(String.valueOf(sightMarks30yrdsFloat));
        sightMark40yrds.setText(String.valueOf(sightMarks40yrdsFloat));
        sightMark50yrds.setText(String.valueOf(sightMarks50yrdsFloat));
        sightMark60yrds.setText(String.valueOf(sightMarks60yrdsFloat));
        sightMark80yrds.setText(String.valueOf(sightMarks80yrdsFloat));
        sightMark100yrds.setText(String.valueOf(sightMarks100yrdsFloat));

        sightMark30mtrs.setText(String.valueOf(sightMarks30mtrsFloat));
        sightMark50mtrs.setText(String.valueOf(sightMarks50mtrsFloat));
        sightMark70mtrs.setText(String.valueOf(sightMarks70mtrsFloat));
        sightMark90mtrs.setText(String.valueOf(sightMarks90mtrsFloat));
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePreferences();
    }
}