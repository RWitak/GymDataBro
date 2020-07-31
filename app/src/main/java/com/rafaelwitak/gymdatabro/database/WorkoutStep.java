package com.rafaelwitak.gymdatabro.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "workout_steps",
        primaryKeys = {"workout_id", "number"},
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
        })
public class WorkoutStep {
    @ColumnInfo(name = "workout_id")
    public int workoutID;

    @ColumnInfo
    public int number;

    @ColumnInfo(name = "exercise_id")
    public int exerciseID;

    @ColumnInfo
    public int reps;

    @ColumnInfo
    public float weight;

    @ColumnInfo
    public float rpe;

    @ColumnInfo(name = "duration_seconds")
    public int durationSeconds;

    @ColumnInfo(name = "rest_seconds")
    public int restSeconds;

    @ColumnInfo
    public String details;

    @ColumnInfo
    public String notes;
}
