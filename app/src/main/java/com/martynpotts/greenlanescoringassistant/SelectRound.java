package com.martynpotts.greenlanescoringassistant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.martynpotts.greenlanescoringassistant.databinding.ActivitySelectRoundBinding;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SelectRound extends DrawerBaseActivity{

    ActivitySelectRoundBinding activitySelectRoundBinding;
    String chosenRound, chosenRoundVariant;
    EditText roundName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySelectRoundBinding = ActivitySelectRoundBinding.inflate(getLayoutInflater());
        setContentView(activitySelectRoundBinding.getRoot());
        allocateActivityTitle("Select Round");

        List<String> rounds = Arrays.asList("Portsmouth", "National", "252");
        List<String> nationalVariants = Arrays.asList("New National","Long National","National","Short National","Junior National","Short Junior National");
        List<String> variants252 = Arrays.asList("252 100 Yards","252 80 Yards", "252 60 Yards", "252 50 Yards", "252 40 Yards", "252 30 Yards", "252 20 Yards");


        Spinner spinSelectRound = findViewById(R.id.spnSelectRound);
        Spinner spinSelectRoundVariant = findViewById(R.id.spnSelectRoundVariant);

        ArrayAdapter<String> selectRoundAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.my_spinner, rounds);
        selectRoundAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        spinSelectRound.setAdapter(selectRoundAdapter);

        ArrayAdapter<String> selectRoundVariantAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.my_spinner);
        selectRoundVariantAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        spinSelectRoundVariant.setAdapter(selectRoundVariantAdapter);

        roundName = findViewById(R.id.edtRoundTitle);

        spinSelectRound.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosenRound = spinSelectRound.getSelectedItem().toString();
                switch (chosenRound) {
                    case "Portsmouth":
                        selectRoundVariantAdapter.clear();
                        selectRoundVariantAdapter.notifyDataSetChanged();
                        spinSelectRoundVariant.setEnabled(false);
                        roundName.setText(chosenRound);
                        break;
                    case "National":
                        spinSelectRoundVariant.setEnabled(true);
                        selectRoundVariantAdapter.clear();
                        selectRoundVariantAdapter.notifyDataSetChanged();
                        selectRoundVariantAdapter.addAll(nationalVariants);
                        break;
                    case "252":
                        spinSelectRoundVariant.setEnabled(true);
                        selectRoundVariantAdapter.clear();
                        selectRoundVariantAdapter.notifyDataSetChanged();
                        selectRoundVariantAdapter.addAll(variants252);
                        break;
                }
                selectRoundVariantAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinSelectRoundVariant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosenRoundVariant = spinSelectRoundVariant.getSelectedItem().toString();
                roundName.setText(chosenRoundVariant);

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


        ImageButton launchScoreRound = findViewById(R.id.ibtnScoreRound);
        launchScoreRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startRound = new Intent(SelectRound.this, ScoreRound.class);
                chosenRound = spinSelectRound.getSelectedItem().toString();
                startRound.putExtra("ChosenRound", chosenRound);
                startRound.putExtra("ChosenRoundVariant",chosenRoundVariant);
                startRound.putExtra("RoundName", roundName.getText().toString());
                startActivity(startRound);
            }
        });
    }


}