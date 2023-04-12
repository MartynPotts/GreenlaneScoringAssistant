package com.martynpotts.greenlanescoringassistant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.martynpotts.greenlanescoringassistant.databinding.ActivityArcherDetailsBinding;
import com.martynpotts.greenlanescoringassistant.databinding.ActivitySelectRoundBinding;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SelectRound extends DrawerBaseActivity{

    ActivitySelectRoundBinding activitySelectRoundBinding;
    String chosenRound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySelectRoundBinding = ActivitySelectRoundBinding.inflate(getLayoutInflater());
        setContentView(activitySelectRoundBinding.getRoot());
        allocateActivityTitle("Select Round");

        List<String> rounds = Arrays.asList("Portsmouth", "National", "252");
        List<String> nationalVariants = Arrays.asList("New National","Long National","National","Short National","Junior National","Short Junior National");
        List<String> variants252 = Arrays.asList("20 Yards","30 Yards", "40 Yards", "50 Yards", "60 Yards", "80 Yards", "100 Yards");


        Spinner spinSelectRound = findViewById(R.id.spnSelectRound);
        Spinner spinSelectRoundVariant = findViewById(R.id.spnSelectRoundVariant);

        ArrayAdapter selectRoundAdapter = new ArrayAdapter(getApplicationContext(), R.layout.my_spinner, rounds);
        selectRoundAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        spinSelectRound.setAdapter(selectRoundAdapter);

        spinSelectRound.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosenRound = spinSelectRound.getSelectedItem().toString();
                switch (chosenRound) {
                    case "Portsmouth":
                        spinSelectRoundVariant.setEnabled(false);
                        break;
                    case "National":
                        spinSelectRoundVariant.setEnabled(true);
                        ArrayAdapter selectNationalVariant = new ArrayAdapter(getApplicationContext(), R.layout.my_spinner, nationalVariants);
                        selectNationalVariant.setDropDownViewResource(R.layout.my_dropdown_item);
                        spinSelectRoundVariant.setAdapter(selectNationalVariant);
                        break;
                    case "252":
                        spinSelectRoundVariant.setEnabled(true);
                        ArrayAdapter select252Variant = new ArrayAdapter(getApplicationContext(), R.layout.my_spinner, variants252);
                        select252Variant.setDropDownViewResource(R.layout.my_dropdown_item);
                        spinSelectRoundVariant.setAdapter(select252Variant);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SharedPreferences sp = getSharedPreferences("SharedPreferences", MODE_PRIVATE);

        TextView setArcherName = findViewById(R.id.lblChosenArcher);
        String nameString = sp.getString("Name","");
        setArcherName.setText(nameString);

        TextView setBowType = findViewById(R.id.lblChosenBow);
        String bowTypeString = sp.getString("BowType", "");
        setBowType.setText(bowTypeString);


        FloatingActionButton launchScoreRound = findViewById(R.id.btnStartRound);
        launchScoreRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startRound = new Intent(SelectRound.this, ScoreRound.class);
                chosenRound = spinSelectRound.getSelectedItem().toString();
                startRound.putExtra("ChosenRound", chosenRound);
                startActivity(startRound);

            }
        });
    }


}