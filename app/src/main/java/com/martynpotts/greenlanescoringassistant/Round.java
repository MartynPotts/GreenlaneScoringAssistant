package com.martynpotts.greenlanescoringassistant;

public class Round {
    private int ID;
    private String roundName;
    private int roundScore;

    public Round(String roundName, int roundScore) {
        this.roundName = roundName;
        this.roundScore = roundScore;
    }

    public Round(int ID, String roundName, int roundScore) {
        this.ID = ID;
        this.roundName = roundName;
        this.roundScore = roundScore;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public int getRoundScore() {
        return roundScore;
    }

    public void setRoundScore(int roundScore) {
        this.roundScore = roundScore;
    }
}

