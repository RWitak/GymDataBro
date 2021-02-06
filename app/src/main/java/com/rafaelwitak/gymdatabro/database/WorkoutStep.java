/*
 * Copyright (c) 2020, Rafael Witak.
 */

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
                @Index(
                        value = "exercise_id",
                        name = "index_workout_steps_exercise_id"),
                @Index(
                        value = {"workout_id", "number"},
                        unique = true)
        })
public class WorkoutStep {
// TODO: Use optionals instead of Nullables in Getters/Setters. Possible?
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "workout_id")
    private int workoutID;

    @ColumnInfo
    @NonNull
    private Integer number = -1;

    @ColumnInfo
    @Nullable
    private String name;

    @ColumnInfo(name = "exercise_id")
    @NonNull
    private Integer exerciseID = -1;

    @ColumnInfo
    @Nullable
    private Integer reps;

    @ColumnInfo
    @Nullable
    private Float weight;

    @ColumnInfo
    @Nullable
    private Float rpe;

    @ColumnInfo(name = "duration_seconds")
    @Nullable
    private Integer durationSeconds;

    @ColumnInfo(name = "rest_seconds")
    @Nullable
    private Integer restSeconds;

    @ColumnInfo
    private String details;

    @ColumnInfo
    private String notes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWorkoutID() {
        return workoutID;
    }

    public void setWorkoutID(int workoutID) {
        this.workoutID = workoutID;
    }

    @NonNull
    public Integer getNumber() {
        return number;
    }

    public void setNumber(@NonNull Integer number) {
        this.number = number;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @NonNull
    public Integer getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(@NonNull Integer exerciseID) {
        this.exerciseID = exerciseID;
    }

    @Nullable
    public Integer getReps() {
        return reps;
    }

    public void setReps(@Nullable Integer reps) {
        this.reps = reps;
    }

    @Nullable
    public Float getWeight() {
        return weight;
    }

    public void setWeight(@Nullable Float weight) {
        this.weight = weight;
    }

    @Nullable
    public Float getRpe() {
        return rpe;
    }

    public void setRpe(@Nullable Float rpe) {
        this.rpe = rpe;
    }

    @Nullable
    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(@Nullable Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    @Nullable
    public Integer getRestSeconds() {
        return restSeconds;
    }

    public void setRestSeconds(@Nullable Integer restSeconds) {
        this.restSeconds = restSeconds;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}