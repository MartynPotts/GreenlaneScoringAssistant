package com.martynpotts.greenlanescoringassistant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class RoundAdapter extends RecyclerView.Adapter<RoundViewHolder> {
    private Context context;
    private ArrayList<Round> listRounds;
    private ArrayList<Round> mArrayList;
    private DBHandler mDatabase;

    public RoundAdapter(Context context, ArrayList<Round> listRounds) {
        this.context = context;
        this.listRounds = listRounds;
        this.mArrayList = mArrayList;
        this.mDatabase = new DBHandler(context);
    }


    @Override
    public RoundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.round_list_layout, parent, false);
        return new RoundViewHolder(view);
    }
    @Override
    public void onBindViewHolder(RoundViewHolder holder, int position) {
        final Round rounds = listRounds.get(position);
        holder.tvRoundName.setText(String.valueOf(rounds.getRoundName()));
        holder.tvRoundScore.setText(String.valueOf(rounds.getRoundScore()));
        holder.deleteRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.deleteRound(rounds.getID());
                listRounds.remove(rounds);
                notifyDataSetChanged();
                if (listRounds.isEmpty()) {
                    Toast.makeText(context, "There are no rounds in the database. Start shooting now", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listRounds.size();
    }
}
