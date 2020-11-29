package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "workout_steps",
        foreignKeys = {
                @ForeignKey(
                        entity = Workout.class,
                        parentColumns = "id",
                        childColumns = "workout_id"
                ),
                @ForeignKey(
                        entity = Exercise.class,
                        parentColumns = "id",
                        childColumns = "exercise_id"
                )
        },
        indices = {
                @Index(value = {"workout_id", "number"}, unique = true)
        })
public class WorkoutStep {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "workout_id")
    public int workoutID;

    @ColumnInfo
    @NonNull
    public Integer number = -1;

    @ColumnInfo
    @Nullable
    public String name;

    @ColumnInfo(name = "exercise_id")
    @NonNull
    public Integer exerciseID = -1;

    @ColumnInfo
    @Nullable
    public Integer reps;

    @ColumnInfo
    @Nullable
    public Float weight;

    @ColumnInfo
    @Nullable
    public Float rpe;

    @ColumnInfo(name = "duration_seconds")
    @Nullable
    public Integer durationSeconds;

    @ColumnInfo(name = "rest_seconds")
    @Nullable
    public Integer restSeconds;

    @ColumnInfo
    public String details;

    @ColumnInfo
    public String notes;
}