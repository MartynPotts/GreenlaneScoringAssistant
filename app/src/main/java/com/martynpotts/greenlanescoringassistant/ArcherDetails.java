package com.martynpotts.greenlanescoringassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import com.martynpotts.greenlanescoringassistant.databinding.ActivityArcherDetailsBinding;

public class ArcherDetails extends DrawerBaseActivity {


    ActivityArcherDetailsBinding activityArcherDetailsBinding;
    private EditText archerName, archerAge, gender;
    private CheckBox archer1st,archer2nd,archer3rd,bowman1st,bowman2nd,bowman3rd,masterBowman,
            grandMasterBowman,eliteMasterBowman;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityArcherDetailsBinding = ActivityArcherDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityArcherDetailsBinding.getRoot());
        allocateActivityTitle("Archer Details");

        archerName = findViewById(R.id.edtArcherName);
        archerAge = findViewById(R.id.edtAge);
        gender = findViewById(R.id.edtGender);


        archer1st = findViewById(R.id.chkArcherFirst);
        archer2nd = findViewById(R.id.chkArcherSecond);
        archer3rd = findViewById(R.id.chkArcherThird);

        bowman1st = findViewById(R.id.chkBowmanFirst);
        bowman2nd = findViewById(R.id.chkBowmanSecond);
        bowman3rd = findViewById(R.id.chkBowmanThird);

        masterBowman = findViewById(R.id.chkMasterBowman);
        grandMasterBowman = findViewById(R.id.chkGrandMasterBowman);
        eliteMasterBowman = findViewById(R.id.chkEliteMasterBowman);

    }

    public void savePreferences(){
        SharedPreferences sp = getSharedPreferences("SharedPreferences",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sp.edit();

        myEdit.putString("Name",archerName.getText().toString() );
        try {
            myEdit.putInt("Age", Integer.parseInt(archerAge.getText().toString()));
        } catch (NumberFormatException nfe){
            myEdit.putInt("Age", 0);
        }
        myEdit.putString("Gender",gender.getText().toString());

        myEdit.putBoolean("Archer1st",archer1st.isChecked());
        myEdit.putBoolean("Archer2nd",archer2nd.isChecked());
        myEdit.putBoolean("Archer3rd",archer3rd.isChecked());

        myEdit.putBoolean("Bowman1st",bowman1st.isChecked());
        myEdit.putBoolean("Bowman2nd",bowman2nd.isChecked());
        myEdit.putBoolean("Bowman3rd",bowman3rd.isChecked());

        myEdit.putBoolean("MasterBowman",masterBowman.isChecked());
        myEdit.putBoolean("GrandMasterBowman",grandMasterBowman.isChecked());
        myEdit.putBoolean("EliteMasterBowman",eliteMasterBowman.isChecked());
        myEdit.apply();
    }



    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        String nameString = sp.getString("Name","");
        int ageInt = sp.getInt("Age", 0);
        String genderString = sp.getString("Gender","");

        archerName.setText(nameString);
        archerAge.setText(String.valueOf(ageInt));
        gender.setText(genderString);

        if(sp.contains("Archer1st") && sp.getBoolean("Archer1st",true)==true){
            archer1st.setChecked(true);
        }
        if(sp.contains("Archer2nd") && sp.getBoolean("Archer2nd",true)==true){
            archer2nd.setChecked(true);
        }
        if(sp.contains("Archer3rd") && sp.getBoolean("Archer3rd",true)==true){
            archer3rd.setChecked(true);
        }

        if(sp.contains("Bowman1st") && sp.getBoolean("Bowman1st",true)==true){
            bowman1st.setChecked(true);
        }
        if(sp.contains("Bowman2nd") && sp.getBoolean("Bowman2nd",true)==true){
            bowman2nd.setChecked(true);
        }
        if(sp.contains("Bowman3rd") && sp.getBoolean("Bowman3rd",true)==true){
            bowman3rd.setChecked(true);
        }

        if(sp.contains("MasterBowman") && sp.getBoolean("MasterBowman",true)==true){
            masterBowman.setChecked(true);
        }
        if(sp.contains("GrandMasterBowman") && sp.getBoolean("GrandMasterBowman",true)==true){
            grandMasterBowman.setChecked(true);
        }
        if(sp.contains("EliteMasterBowman") && sp.getBoolean("EliteMasterBowman",true)==true){
            eliteMasterBowman.setChecked(true);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        savePreferences();
    }


}