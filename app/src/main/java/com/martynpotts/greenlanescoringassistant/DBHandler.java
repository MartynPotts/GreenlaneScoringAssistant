package com.martynpotts.greenlanescoringassistant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Rounds.db";
    private static final String TABLE_ROUNDS = "Rounds";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "roundName";
    private static final String COLUMN_SCORE = "roundScore";
    DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE "
                + TABLE_ROUNDS + "(" + COLUMN_ID
                + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_SCORE + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUNDS);
        onCreate(db);
    }
    ArrayList<Round> listRounds() {
        String sql = "select * from " + TABLE_ROUNDS;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Round> storeRounds = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String roundName = cursor.getString(1);
                int roundScore = cursor.getInt(2);
                storeRounds.add(new Round(id, roundName, roundScore));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeRounds;
    }
    void addRound(Round rounds) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, rounds.getRoundName());
        values.put(COLUMN_SCORE, rounds.getRoundScore());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_ROUNDS, null, values);
    }

    void deleteRound(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ROUNDS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

}

