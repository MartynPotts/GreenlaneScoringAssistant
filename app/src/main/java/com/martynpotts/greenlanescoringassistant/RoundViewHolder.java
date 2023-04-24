package com.martynpotts.greenlanescoringassistant;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
class RoundViewHolder extends RecyclerView.ViewHolder {
    TextView tvRoundName, tvRoundScore;
    ImageView deleteRound;

    RoundViewHolder(View itemView) {
        super(itemView);
        tvRoundName = itemView.findViewById(R.id.RoundName);
        tvRoundScore = itemView.findViewById(R.id.RoundScore);
        deleteRound = itemView.findViewById(R.id.deleteRound);
    }
}