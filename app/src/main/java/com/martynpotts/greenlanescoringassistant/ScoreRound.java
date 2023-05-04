package com.martynpotts.greenlanescoringassistant;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.martynpotts.greenlanescoringassistant.databinding.ActivityScoreRoundBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ScoreRound extends DrawerBaseActivity {

    ActivityScoreRoundBinding activityScoreRoundBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityScoreRoundBinding = ActivityScoreRoundBinding.inflate(getLayoutInflater());
        setContentView(activityScoreRoundBinding.getRoot());
        allocateActivityTitle("Score Round");

        Intent getChosenRound = getIntent();
        String roundName = getChosenRound.getStringExtra("RoundName");
        String chosenRound = getChosenRound.getStringExtra("ChosenRound");
        String chosenRoundVariant = getChosenRound.getStringExtra("ChosenRoundVariant");

        TextView distance1, distance2, distance252;
        Button btn10, btn9, btn8, btn7, btn6, btn5, btn4, btn3, btn2, btn1, btnMiss;
        ImageButton ibtnBackspace;
        Button[] buttons;

        Toolbar toolbar;

        HashMap<String, List<Integer>> ends = new HashMap<>();
        String name;
        for (int i = 1; i <= 12; i++) {
            name = "end" + i;
            ends.put(name, new ArrayList<>(6));
        }
        final int MAX_SIZE = 6;
        final int MAX_NUMBER_ENDS;
        int[] index = {0};
        int totalScore = 0;
        HashMap<String, TextView> textViewMap = new HashMap<>();
        boolean allEndsCompleted = false;
        TextView txtScore = findViewById(R.id.totalScore);

        switch (chosenRound) {
            case "Portsmouth":
                setContentView(R.layout.activity_portsmouth);
                toolbar = findViewById(R.id.ScoreRoundToolbar);
                toolbar.setTitleTextColor(Color.WHITE);
                setSupportActionBar(toolbar);
                getSupportActionBar().setTitle(roundName);
                MAX_NUMBER_ENDS = 10;
                buttons = new Button[]{findViewById(R.id.btn10), findViewById(R.id.btn9), findViewById(R.id.btn8), findViewById(R.id.btn7), findViewById(R.id.btn6), findViewById(R.id.btn5), findViewById(R.id.btn4), findViewById(R.id.btn3), findViewById(R.id.btn2), findViewById(R.id.btn1), findViewById(R.id.btnMiss)};
                int[] textViewEndsids = {R.id.end1, R.id.end2, R.id.end3, R.id.end4, R.id.end5, R.id.end6, R.id.end7, R.id.end8, R.id.end9, R.id.end10};
                for (int i = 0; i < textViewEndsids.length; i++) {
                    textViewMap.put("end" + (i + 1), findViewById(textViewEndsids[i]));
                }

                int[] textViewEndTotalIds = {R.id.end1Total, R.id.end2Total, R.id.end3Total, R.id.end4Total, R.id.end5Total, R.id.end6Total, R.id.end7Total, R.id.end8Total, R.id.end9Total, R.id.end10Total};

                for (int i = 0; i < textViewEndTotalIds.length; i++) {
                    textViewMap.put("endTotal" + (i + 1), findViewById(textViewEndTotalIds[i]));
                }

                btn10 = findViewById(R.id.btn10);
                btn10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.size() >= MAX_SIZE) {
                            index[0] = (index[0] + 1) % 10;
                            name = "end" + (index[0] + 1);
                            currentList = new ArrayList<>();
                            ends.put(name, currentList);
                        }

                        currentList.add(10);
                        updateListTextView(name, ends, textViewMap, index);

                        if (name.equals("end10") && currentList.size() == 6) {
                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i].setEnabled(false);
                            }
                            int totalScore = calculateRoundTotal(ends);
                            Round newRound = new Round(roundName, totalScore);
                            DBHandler mDatabase = new DBHandler(getApplicationContext());
                            mDatabase.addRound(newRound);
                            Intent intent = new Intent(ScoreRound.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }

                });

                btn9 = findViewById(R.id.btn9);
                btn9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.size() >= MAX_SIZE) {
                            index[0] = (index[0] + 1) % 10;
                            name = "end" + (index[0] + 1);
                            currentList = new ArrayList<>();
                            ends.put(name, currentList);
                        }

                        currentList.add(9);
                        updateListTextView(name, ends, textViewMap, index);

                        if (name.equals("end10") && currentList.size() == 6) {
                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i].setEnabled(false);
                            }
                            int totalScore = calculateRoundTotal(ends);
                            Round newRound = new Round(roundName, totalScore);
                            DBHandler mDatabase = new DBHandler(getApplicationContext());
                            mDatabase.addRound(newRound);
                            Intent intent = new Intent(ScoreRound.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }

                });

                btn8 = findViewById(R.id.btn8);
                btn8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.size() >= MAX_SIZE) {
                            index[0] = (index[0] + 1) % 10;
                            name = "end" + (index[0] + 1);
                            currentList = new ArrayList<>();
                            ends.put(name, currentList);
                        }

                        currentList.add(8);
                        updateListTextView(name, ends, textViewMap, index);

                        if (name.equals("end10") && currentList.size() == 6) {
                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i].setEnabled(false);
                            }
                            int totalScore = calculateRoundTotal(ends);
                            Round newRound = new Round(roundName, totalScore);
                            DBHandler mDatabase = new DBHandler(getApplicationContext());
                            mDatabase.addRound(newRound);
                            Intent intent = new Intent(ScoreRound.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }

                });

                btn7 = findViewById(R.id.btn7);
                btn7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.size() >= MAX_SIZE) {
                            index[0] = (index[0] + 1) % 10;
                            name = "end" + (index[0] + 1);
                            currentList = new ArrayList<>();
                            ends.put(name, currentList);
                        }

                        currentList.add(7);
                        updateListTextView(name, ends, textViewMap, index);

                        if (name.equals("end10") && currentList.size() == 6) {
                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i].setEnabled(false);
                            }
                            int totalScore = calculateRoundTotal(ends);
                            Round newRound = new Round(roundName, totalScore);
                            DBHandler mDatabase = new DBHandler(getApplicationContext());
                            mDatabase.addRound(newRound);
                            Intent intent = new Intent(ScoreRound.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }
                });

                btn6 = findViewById(R.id.btn6);
                btn6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.size() >= MAX_SIZE) {
                            index[0] = (index[0] + 1) % 10;
                            name = "end" + (index[0] + 1);
                            currentList = new ArrayList<>();
                            ends.put(name, currentList);
                        }

                        currentList.add(6);
                        updateListTextView(name, ends, textViewMap, index);

                        if (name.equals("end10") && currentList.size() == 6) {
                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i].setEnabled(false);
                            }
                            int totalScore = calculateRoundTotal(ends);
                            Round newRound = new Round(roundName, totalScore);
                            DBHandler mDatabase = new DBHandler(getApplicationContext());
                            mDatabase.addRound(newRound);
                            Intent intent = new Intent(ScoreRound.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }

                });

                btn5 = findViewById(R.id.btn5);
                btn5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.size() >= MAX_SIZE) {
                            index[0] = (index[0] + 1) % 10;
                            name = "end" + (index[0] + 1);
                            currentList = new ArrayList<>();
                            ends.put(name, currentList);
                        }

                        currentList.add(5);
                        updateListTextView(name, ends, textViewMap, index);

                        if (name.equals("end10") && currentList.size() == 6) {
                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i].setEnabled(false);
                            }
                            int totalScore = calculateRoundTotal(ends);
                            Round newRound = new Round(roundName, totalScore);
                            DBHandler mDatabase = new DBHandler(getApplicationContext());
                            mDatabase.addRound(newRound);
                            Intent intent = new Intent(ScoreRound.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }
                });

                btn4 = findViewById(R.id.btn4);
                btn4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.size() >= MAX_SIZE) {
                            index[0] = (index[0] + 1) % 10;
                            name = "end" + (index[0] + 1);
                            currentList = new ArrayList<>();
                            ends.put(name, currentList);
                        }

                        currentList.add(4);
                        updateListTextView(name, ends, textViewMap, index);

                        if (name.equals("end10") && currentList.size() == 6) {
                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i].setEnabled(false);
                            }
                            int totalScore = calculateRoundTotal(ends);
                            Round newRound = new Round(roundName, totalScore);
                            DBHandler mDatabase = new DBHandler(getApplicationContext());
                            mDatabase.addRound(newRound);
                            Intent intent = new Intent(ScoreRound.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }

                });

                btn3 = findViewById(R.id.btn3);
                btn3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.size() >= MAX_SIZE) {
                            index[0] = (index[0] + 1) % 10;
                            name = "end" + (index[0] + 1);
                            currentList = new ArrayList<>();
                            ends.put(name, currentList);
                        }

                        currentList.add(3);
                        updateListTextView(name, ends, textViewMap, index);

                        if (name.equals("end10") && currentList.size() == 6) {
                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i].setEnabled(false);
                            }
                            int totalScore = calculateRoundTotal(ends);
                            Round newRound = new Round(roundName, totalScore);
                            DBHandler mDatabase = new DBHandler(getApplicationContext());
                            mDatabase.addRound(newRound);
                            Intent intent = new Intent(ScoreRound.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }
                });

                btn2 = findViewById(R.id.btn2);
                btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.size() >= MAX_SIZE) {
                            index[0] = (index[0] + 1) % 10;
                            name = "end" + (index[0] + 1);
                            currentList = new ArrayList<>();
                            ends.put(name, currentList);
                        }

                        currentList.add(2);
                        updateListTextView(name, ends, textViewMap, index);

                        if (name.equals("end10") && currentList.size() == 6) {
                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i].setEnabled(false);
                            }
                            int totalScore = calculateRoundTotal(ends);
                            Round newRound = new Round(roundName, totalScore);
                            DBHandler mDatabase = new DBHandler(getApplicationContext());
                            mDatabase.addRound(newRound);
                            Intent intent = new Intent(ScoreRound.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }

                });

                btn1 = findViewById(R.id.btn1);
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.size() >= MAX_SIZE) {
                            index[0] = (index[0] + 1) % 10;
                            name = "end" + (index[0] + 1);
                            currentList = new ArrayList<>();
                            ends.put(name, currentList);
                        }

                        currentList.add(1);
                        updateListTextView(name, ends, textViewMap, index);

                        if (name.equals("end10") && currentList.size() == 6) {
                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i].setEnabled(false);
                            }
                            int totalScore = calculateRoundTotal(ends);
                            Round newRound = new Round(roundName, totalScore);
                            DBHandler mDatabase = new DBHandler(getApplicationContext());
                            mDatabase.addRound(newRound);
                            Intent intent = new Intent(ScoreRound.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }
                });

                btnMiss = findViewById(R.id.btnMiss);
                btnMiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.size() >= MAX_SIZE) {
                            index[0] = (index[0] + 1) % 10;
                            name = "end" + (index[0] + 1);
                            currentList = new ArrayList<>();
                            ends.put(name, currentList);
                        }

                        currentList.add(0);
                        updateListTextView(name, ends, textViewMap, index);

                        if (name.equals("end10") && currentList.size() == 6) {
                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i].setEnabled(false);
                            }
                            int totalScore = calculateRoundTotal(ends);
                            Round newRound = new Round(roundName, totalScore);
                            DBHandler mDatabase = new DBHandler(getApplicationContext());
                            mDatabase.addRound(newRound);
                            Intent intent = new Intent(ScoreRound.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }
                });

                ibtnBackspace = findViewById(R.id.ibtnBackspace);
                ibtnBackspace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.isEmpty()) {
                            return;
                        }

                        int totalHitsBefore = calculateHits(ends);
                        int totalGoldsBefore = calculateGolds(ends);

                        currentList.remove(currentList.size() - 1);

                        Collections.sort(currentList, Collections.reverseOrder());
                        updateListTextView(name, ends, textViewMap, index);

                        int totalHitsAfter = calculateHits(ends);

                        if (totalHitsBefore != totalHitsAfter) {
                            TextView roundTotalHits = findViewById(R.id.totalHits);
                            roundTotalHits.setText(String.valueOf(totalHitsAfter));
                        }

                        int totalGoldsAfter = calculateGolds(ends);

                        if (totalGoldsBefore != totalGoldsAfter) {
                            TextView roundTotalGolds = findViewById(R.id.totalGolds);
                            roundTotalGolds.setText(String.valueOf(totalGoldsAfter));
                        }

                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }
                });
                break;
            case "National":
                setContentView(R.layout.activity_national);
                toolbar = findViewById(R.id.ScoreRoundToolbar);
                toolbar.setTitleTextColor(Color.WHITE);
                setSupportActionBar(toolbar);
                getSupportActionBar().setTitle(roundName);
                distance1 = findViewById(R.id.distance1);
                distance2 = findViewById(R.id.distance2);

                textViewEndsids = new int[]{R.id.end1, R.id.end2, R.id.end3, R.id.end4, R.id.end5, R.id.end6, R.id.end7, R.id.end8, R.id.end9, R.id.end10, R.id.end11, R.id.end12};
                buttons = new Button[]{findViewById(R.id.btn9), findViewById(R.id.btn7), findViewById(R.id.btn5), findViewById(R.id.btn3), findViewById(R.id.btn1), findViewById(R.id.btnMiss)};
                for (int i = 0; i < textViewEndsids.length; i++) {
                    textViewMap.put("end" + (i + 1), findViewById(textViewEndsids[i]));
                }

                textViewEndTotalIds = new int[]{R.id.end1Total, R.id.end2Total, R.id.end3Total, R.id.end4Total, R.id.end5Total, R.id.end6Total, R.id.end7Total, R.id.end8Total, R.id.end9Total, R.id.end10Total, R.id.end11Total, R.id.end12Total};

                for (int i = 0; i < textViewEndTotalIds.length; i++) {
                    textViewMap.put("endTotal" + (i + 1), findViewById(textViewEndTotalIds[i]));
                }

                MAX_NUMBER_ENDS = 12;
                btn9 = findViewById(R.id.btn9);
                btn9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.size() >= MAX_SIZE) {
                            index[0] = (index[0] + 1) % 12;
                            name = "end" + (index[0] + 1);
                            currentList = new ArrayList<>();
                            ends.put(name, currentList);
                        }

                        currentList.add(9);
                        updateListTextView(name, ends, textViewMap, index);

                        if (name.equals("end12") && currentList.size() == 6) {
                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i].setEnabled(false);
                            }
                            int totalScore = calculateRoundTotal(ends);
                            Round newRound = new Round(roundName, totalScore);
                            DBHandler mDatabase = new DBHandler(getApplicationContext());
                            mDatabase.addRound(newRound);
                            Intent intent = new Intent(ScoreRound.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }

                });

                btn7 = findViewById(R.id.btn7);
                btn7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.size() >= MAX_SIZE) {
                            index[0] = (index[0] + 1) % 12;
                            name = "end" + (index[0] + 1);
                            currentList = new ArrayList<>();
                            ends.put(name, currentList);
                        }

                        currentList.add(7);
                        updateListTextView(name, ends, textViewMap, index);

                        if (name.equals("end12") && currentList.size() == 6) {
                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i].setEnabled(false);
                            }
                            int totalScore = calculateRoundTotal(ends);
                            Round newRound = new Round(roundName, totalScore);
                            DBHandler mDatabase = new DBHandler(getApplicationContext());
                            mDatabase.addRound(newRound);
                            Intent intent = new Intent(ScoreRound.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }
                });

                btn5 = findViewById(R.id.btn5);
                btn5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.size() >= MAX_SIZE) {
                            index[0] = (index[0] + 1) % 12;
                            name = "end" + (index[0] + 1);
                            currentList = new ArrayList<>();
                            ends.put(name, currentList);
                        }

                        currentList.add(5);
                        updateListTextView(name, ends, textViewMap, index);

                        if (name.equals("end12") && currentList.size() == 6) {
                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i].setEnabled(false);
                            }
                            int totalScore = calculateRoundTotal(ends);
                            Round newRound = new Round(roundName, totalScore);
                            DBHandler mDatabase = new DBHandler(getApplicationContext());
                            mDatabase.addRound(newRound);
                            Intent intent = new Intent(ScoreRound.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }
                });

                btn3 = findViewById(R.id.btn3);
                btn3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.size() >= MAX_SIZE) {
                            index[0] = (index[0] + 1) % 12;
                            name = "end" + (index[0] + 1);
                            currentList = new ArrayList<>();
                            ends.put(name, currentList);
                        }

                        currentList.add(3);
                        updateListTextView(name, ends, textViewMap, index);

                        if (name.equals("end12") && currentList.size() == 6) {
                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i].setEnabled(false);
                            }
                            int totalScore = calculateRoundTotal(ends);
                            Round newRound = new Round(roundName, totalScore);
                            DBHandler mDatabase = new DBHandler(getApplicationContext());
                            mDatabase.addRound(newRound);

                            Intent intent = new Intent(ScoreRound.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }
                });

                btn1 = findViewById(R.id.btn1);
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.size() >= MAX_SIZE) {
                            index[0] = (index[0] + 1) % 12;
                            name = "end" + (index[0] + 1);
                            currentList = new ArrayList<>();
                            ends.put(name, currentList);
                        }

                        currentList.add(1);
                        updateListTextView(name, ends, textViewMap, index);

                        if (name.equals("end12") && currentList.size() == 6) {
                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i].setEnabled(false);
                            }
                            int totalScore = calculateRoundTotal(ends);
                            Round newRound = new Round(roundName, totalScore);
                            DBHandler mDatabase = new DBHandler(getApplicationContext());
                            mDatabase.addRound(newRound);
                            Intent intent = new Intent(ScoreRound.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }
                });

                btnMiss = findViewById(R.id.btnMiss);
                btnMiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.size() >= MAX_SIZE) {
                            index[0] = (index[0] + 1) % 12;
                            name = "end" + (index[0] + 1);
                            currentList = new ArrayList<>();
                            ends.put(name, currentList);
                        }

                        currentList.add(0);
                        updateListTextView(name, ends, textViewMap, index);

                        if (name.equals("end12") && currentList.size() == 6) {
                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i].setEnabled(false);
                            }
                            int totalScore = calculateRoundTotal(ends);
                            Round newRound = new Round(roundName, totalScore);
                            DBHandler mDatabase = new DBHandler(getApplicationContext());
                            mDatabase.addRound(newRound);
                            Intent intent = new Intent(ScoreRound.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }
                });

                ibtnBackspace = findViewById(R.id.ibtnBackspace);
                ibtnBackspace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.isEmpty()) {
                            return;
                        }

                        int totalHitsBefore = calculateHits(ends);
                        int totalGoldsBefore = calculateGolds(ends);

                        currentList.remove(currentList.size() - 1);

                        Collections.sort(currentList, Collections.reverseOrder());
                        updateListTextView(name, ends, textViewMap, index);

                        int totalHitsAfter = calculateHits(ends);

                        if (totalHitsBefore != totalHitsAfter) {
                            TextView roundTotalHits = findViewById(R.id.totalHits);
                            roundTotalHits.setText(String.valueOf(totalHitsAfter));
                        }

                        int totalGoldsAfter = calculateGolds(ends);

                        if (totalGoldsBefore != totalGoldsAfter) {
                            TextView roundTotalGolds = findViewById(R.id.totalGolds);
                            roundTotalGolds.setText(String.valueOf(totalGoldsAfter));
                        }

                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }
                });

                switch (chosenRoundVariant) {
                    case "New National":
                        distance1.setText(R.string.distance_1_100_yards);
                        distance2.setText(R.string.distance_2_80_yards);
                        break;
                    case "Long National":
                        distance1.setText(R.string.distance_1_80_yards);
                        distance2.setText(R.string.distance_2_60_yards);
                        break;
                    case "National":
                        distance1.setText(R.string.distance_1_60_yards);
                        distance2.setText(R.string.distance_2_50_yards);
                        break;
                    case "Short National":
                        distance1.setText(R.string.distance_1_50_yards);
                        distance2.setText(R.string.distance_2_40_yards);
                        break;
                    case "Junior National":
                        distance1.setText(R.string.distance_1_40_yards);
                        distance2.setText(R.string.distance_2_30_yards);
                        break;
                    case "Short Junior National":
                        distance1.setText(R.string.distance_1_30_yards);
                        distance2.setText(R.string.distance_2_20_yards);
                        break;
                }
                break;
            case "252":
                setContentView(R.layout.activity_252);
                toolbar = findViewById(R.id.ScoreRoundToolbar);
                toolbar.setTitleTextColor(Color.WHITE);
                setSupportActionBar(toolbar);
                getSupportActionBar().setTitle(roundName);
                distance252 = findViewById(R.id.distance252);
                Log.d("ChosenRoundVariant", chosenRoundVariant);

                textViewEndsids = new int[]{R.id.end1, R.id.end2, R.id.end3, R.id.end4, R.id.end5, R.id.end6, R.id.end7, R.id.end8, R.id.end9, R.id.end10, R.id.end11, R.id.end12};
                buttons = new Button[]{findViewById(R.id.btn9), findViewById(R.id.btn7), findViewById(R.id.btn5), findViewById(R.id.btn3), findViewById(R.id.btn1), findViewById(R.id.btnMiss)};
                for (int i = 0; i < textViewEndsids.length; i++) {
                    textViewMap.put("end" + (i + 1), findViewById(textViewEndsids[i]));
                }

                textViewEndTotalIds = new int[]{R.id.end1Total, R.id.end2Total, R.id.end3Total, R.id.end4Total, R.id.end5Total, R.id.end6Total, R.id.end7Total, R.id.end8Total, R.id.end9Total, R.id.end10Total, R.id.end11Total, R.id.end12Total};

                for (int i = 0; i < textViewEndTotalIds.length; i++) {
                    textViewMap.put("endTotal" + (i + 1), findViewById(textViewEndTotalIds[i]));
                }

                MAX_NUMBER_ENDS = 6;
                btn9 = findViewById(R.id.btn9);
                btn9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.size() >= MAX_SIZE) {
                            index[0] = (index[0] + 1) % 6;
                            name = "end" + (index[0] + 1);
                            currentList = new ArrayList<>();
                            ends.put(name, currentList);
                        }

                        currentList.add(9);
                        updateListTextView(name, ends, textViewMap, index);

                        if (name.equals("end6") && currentList.size() == 6) {
                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i].setEnabled(false);
                            }
                            int totalScore = calculateRoundTotal(ends);
                            Round newRound = new Round(roundName, totalScore);
                            DBHandler mDatabase = new DBHandler(getApplicationContext());
                            mDatabase.addRound(newRound);
                            Intent intent = new Intent(ScoreRound.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }

                });

                btn7 = findViewById(R.id.btn7);
                btn7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.size() >= MAX_SIZE) {
                            index[0] = (index[0] + 1) % 6;
                            name = "end" + (index[0] + 1);
                            currentList = new ArrayList<>();
                            ends.put(name, currentList);
                        }

                        currentList.add(7);
                        updateListTextView(name, ends, textViewMap, index);

                        if (name.equals("end6") && currentList.size() == 6) {
                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i].setEnabled(false);
                            }
                            int totalScore = calculateRoundTotal(ends);
                            Round newRound = new Round(roundName, totalScore);
                            DBHandler mDatabase = new DBHandler(getApplicationContext());
                            mDatabase.addRound(newRound);
                            Intent intent = new Intent(ScoreRound.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }
                });

                btn5 = findViewById(R.id.btn5);
                btn5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.size() >= MAX_SIZE) {
                            index[0] = (index[0] + 1) % 6;
                            name = "end" + (index[0] + 1);
                            currentList = new ArrayList<>();
                            ends.put(name, currentList);
                        }

                        currentList.add(5);
                        updateListTextView(name, ends, textViewMap, index);

                        if (name.equals("end6") && currentList.size() == 6) {
                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i].setEnabled(false);
                            }
                            int totalScore = calculateRoundTotal(ends);
                            Round newRound = new Round(roundName, totalScore);
                            DBHandler mDatabase = new DBHandler(getApplicationContext());
                            mDatabase.addRound(newRound);
                            Intent intent = new Intent(ScoreRound.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }
                });

                btn3 = findViewById(R.id.btn3);
                btn3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.size() >= MAX_SIZE) {
                            index[0] = (index[0] + 1) % 6;
                            name = "end" + (index[0] + 1);
                            currentList = new ArrayList<>();
                            ends.put(name, currentList);
                        }

                        currentList.add(3);
                        updateListTextView(name, ends, textViewMap, index);

                        if (name.equals("end6") && currentList.size() == 6) {
                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i].setEnabled(false);
                            }
                            int totalScore = calculateRoundTotal(ends);
                            Round newRound = new Round(roundName, totalScore);
                            DBHandler mDatabase = new DBHandler(getApplicationContext());
                            mDatabase.addRound(newRound);
                            Intent intent = new Intent(ScoreRound.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }
                });

                btn1 = findViewById(R.id.btn1);
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.size() >= MAX_SIZE) {
                            index[0] = (index[0] + 1) % 6;
                            name = "end" + (index[0] + 1);
                            currentList = new ArrayList<>();
                            ends.put(name, currentList);
                        }

                        currentList.add(1);
                        updateListTextView(name, ends, textViewMap, index);

                        if (name.equals("end6") && currentList.size() == 6) {
                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i].setEnabled(false);
                            }
                            int totalScore = calculateRoundTotal(ends);
                            Round newRound = new Round(roundName, totalScore);
                            DBHandler mDatabase = new DBHandler(getApplicationContext());
                            mDatabase.addRound(newRound);
                            Intent intent = new Intent(ScoreRound.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }
                });

                btnMiss = findViewById(R.id.btnMiss);
                btnMiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.size() >= MAX_SIZE) {
                            index[0] = (index[0] + 1) % 6;
                            name = "end" + (index[0] + 1);
                            currentList = new ArrayList<>();
                            ends.put(name, currentList);
                        }

                        currentList.add(0);
                        updateListTextView(name, ends, textViewMap, index);

                        if (name.equals("end6") && currentList.size() == 6) {
                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i].setEnabled(false);
                            }
                            int totalScore = calculateRoundTotal(ends);
                            Round newRound = new Round(roundName, totalScore);
                            DBHandler mDatabase = new DBHandler(getApplicationContext());
                            mDatabase.addRound(newRound);
                            Intent intent = new Intent(ScoreRound.this, HomeActivity.class);
                            startActivity(intent);

                        }
                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }
                });

                ibtnBackspace = findViewById(R.id.ibtnBackspace);
                ibtnBackspace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = "end" + (index[0] + 1);
                        List<Integer> currentList = ends.get(name);

                        if (currentList.isEmpty()) {
                            return;
                        }

                        int totalHitsBefore = calculateHits(ends);
                        int totalGoldsBefore = calculateGolds(ends);

                        currentList.remove(currentList.size() - 1);

                        Collections.sort(currentList, Collections.reverseOrder());
                        updateListTextView(name, ends, textViewMap, index);

                        int totalHitsAfter = calculateHits(ends);

                        if (totalHitsBefore != totalHitsAfter) {
                            TextView roundTotalHits = findViewById(R.id.totalHits);
                            roundTotalHits.setText(String.valueOf(totalHitsAfter));
                        }

                        int totalGoldsAfter = calculateGolds(ends);

                        if (totalGoldsBefore != totalGoldsAfter) {
                            TextView roundTotalGolds = findViewById(R.id.totalGolds);
                            roundTotalGolds.setText(String.valueOf(totalGoldsAfter));
                        }

                    }

                    private void updateListTextView(String name, HashMap<String, List<Integer>> ends, HashMap<String, TextView> textViewMap, int[] index) {
                        List<Integer> currentList = ends.get(name);
                        Collections.sort(currentList, Collections.reverseOrder());
                        TextView textView = textViewMap.get(name);
                        String text = "";
                        for (int i = 0; i < currentList.size(); i++) {
                            text += currentList.get(i);
                        }
                        textView.setText(text);

                        TextView totalTextView = textViewMap.get("endTotal" + (index[0] + 1));
                        int sum = calculateScore(currentList);
                        totalTextView.setText(String.valueOf(sum));

                        int totalHits = calculateHits(ends);
                        TextView roundTotalHits = findViewById(R.id.totalHits);
                        roundTotalHits.setText(String.valueOf(totalHits));

                        int totalGolds = calculateGolds(ends);
                        TextView roundTotalGolds = findViewById(R.id.totalGolds);
                        roundTotalGolds.setText(String.valueOf(totalGolds));

                        int roundTotal = calculateRoundTotal(ends);
                        TextView roundTotalTextView = findViewById(R.id.totalScore);
                        roundTotalTextView.setText(String.valueOf(roundTotal));
                    }

                    private int calculateScore(List<Integer> list) {
                        int sum = 0;
                        for (int i = 0; i < list.size(); i++) {
                            sum += list.get(i);
                        }
                        return sum;
                    }

                    private int calculateRoundTotal(HashMap<String, List<Integer>> ends) {
                        int roundTotal = 0;
                        for (List<Integer> currentList : ends.values()) {
                            roundTotal += calculateScore(currentList);
                        }
                        return roundTotal;
                    }

                    public int calculateHits(HashMap<String, List<Integer>> scores) {
                        int totalHits = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score > 0) {
                                    totalHits++;
                                }
                            }
                        }
                        return totalHits;
                    }

                    public int calculateGolds(HashMap<String, List<Integer>> scores) {
                        int totalGolds = 0;
                        for (List<Integer> scoreList : ends.values()) {
                            for (int score : scoreList) {
                                if (score >= 9) {
                                    totalGolds++;
                                }
                            }
                        }
                        return totalGolds;
                    }
                });

                switch (chosenRoundVariant) {
                    case "252 100 Yards":
                        distance252.setText("Distance: 100 Yards");
                        break;
                    case "252 80 Yards":
                        distance252.setText("Distance: 80 Yards");
                        break;
                    case "252 60 Yards":
                        distance252.setText("Distance: 60 Yards");
                        break;
                    case "252 50 Yards":
                        distance252.setText("Distance: 50 Yards");
                        break;
                    case "252 40 yards":
                        distance252.setText("Distance: 40 Yards");
                        break;
                    case "252 30 Yards":
                        distance252.setText("Distance: 30 Yards");
                        break;
                    case "252 20 Yards":
                        distance252.setText("Distance: 20 Yards");
                        break;
                    default:
                        distance252.setText(chosenRoundVariant);
                }
                break;
            default:
                MAX_NUMBER_ENDS = 0;

        }
    }



}

