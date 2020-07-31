package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "sets",
        foreignKeys = @ForeignKey(
                entity = Exercise.class,
                parentColumns = "id",
                childColumns = "exercise_id"))
public class Set {
    @PrimaryKey
    public int id;

    @ColumnInfo
    public long timestamp;

    @ColumnInfo(name = "exercise_id")
    public int exerciseID;

    @ColumnInfo
    public int reps;

    @ColumnInfo
    public float weight;

    @ColumnInfo
    public int seconds;

    @ColumnInfo
    public float rpe;

    @ColumnInfo(name = "pain_level")
    public float painLevel;

    @ColumnInfo
    public String notes;
}
