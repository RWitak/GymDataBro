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
public class PerformanceSet {
    @PrimaryKey
    public int id;

    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    public long timestamp;

    @ColumnInfo(name = "exercise_id")
    public int exerciseID;

    @ColumnInfo
    @Nullable
    public Integer reps;

    @ColumnInfo
    @Nullable
    public Float weight;

    @ColumnInfo(name = "seconds_performed")
    @Nullable
    public Integer secondsPerformed;

    @ColumnInfo(name = "seconds_rested")
    @Nullable
    public Integer secondsRested;

    @ColumnInfo
    @Nullable
    public Float rpe;

    @ColumnInfo(name = "pain_level")
    @NonNull
    public Integer painLevel = 0;

    @ColumnInfo
    @Nullable
    public String notes;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(int exerciseID) {
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
    public Integer getSecondsPerformed() {
        return secondsPerformed;
    }

    public void setSecondsPerformed(@Nullable Integer secondsPerformed) {
        this.secondsPerformed = secondsPerformed;
    }

    @Nullable
    public Integer getSecondsRested() {
        return secondsRested;
    }

    public void setSecondsRested(@Nullable Integer secondsRested) {
        this.secondsRested = secondsRested;
    }

    @Nullable
    public Float getRpe() {
        return rpe;
    }

    public void setRpe(@Nullable Float rpe) {
        this.rpe = rpe;
    }

    @NonNull
    public Integer getPainLevel() {
        return painLevel;
    }

    public void setPainLevel(@NonNull Integer painLevel) {
        this.painLevel = painLevel;
    }

    @Nullable
    public String getNotes() {
        return notes;
    }

    public void setNotes(@Nullable String notes) {
        this.notes = notes;
    }
}
