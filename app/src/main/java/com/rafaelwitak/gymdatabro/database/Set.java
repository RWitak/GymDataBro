package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "sets",
        foreignKeys = @ForeignKey(
                entity = Exercise.class,
                parentColumns = "id",
                childColumns = "exercise_id"),
        indices = {
                @Index(value = "exercise_id")
        }
)
public class Set {
    @PrimaryKey
    public int id;

    @ColumnInfo
    public long timestamp;

    @ColumnInfo(name = "exercise_id")
    public int exerciseID;

    @ColumnInfo
    @Nullable
    public Integer reps;

    @ColumnInfo
    @Nullable
    public Float weight;

    @ColumnInfo
    @Nullable
    public Integer seconds_performed;

    @ColumnInfo
    @Nullable
    public Integer seconds_rested;

    @ColumnInfo
    @Nullable
    public Float rpe;

    @ColumnInfo(name = "pain_level")
    @NonNull
    public Double painLevel = 0.0;

    @ColumnInfo
    @Nullable
    public String notes;
}
