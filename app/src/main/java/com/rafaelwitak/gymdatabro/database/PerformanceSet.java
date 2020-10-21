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
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    public String timestamp;

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


    public void setExerciseID(int exerciseID) {
        this.exerciseID = exerciseID;
    }

    public void setReps(@Nullable Integer reps) {
        this.reps = reps;
    }

    public void setWeight(@Nullable Float weight) {
        this.weight = weight;
    }

    public void setSecondsPerformed(@Nullable Integer secondsPerformed) {
        this.secondsPerformed = secondsPerformed;
    }

    public void setSecondsRested(@Nullable Integer secondsRested) {
        this.secondsRested = secondsRested;
    }

    public void setRpe(@Nullable Float rpe) {
        this.rpe = rpe;
    }

    public void setPainLevel(@NonNull Integer painLevel) {
        this.painLevel = painLevel;
    }

    public void setNotes(@Nullable String notes) {
        this.notes = notes;
    }


    @Override
    @NonNull
    public String toString() {
        return this.id
                + " (ID)\n"
                + this.timestamp
                + " (timestamp)\n"
                + this.exerciseID
                + " (exerciseID)\n"
                + this.reps
                + " (reps)\n"
                + this.weight
                + " (weight)\n"
                + this.secondsPerformed
                + " (secondsPerformed)\n"
                + this.secondsRested
                + " (secondsRested)\n"
                + this.rpe
                + " (rpe)\n"
                + this.painLevel
                + " (painLevel)\n"
                + this.notes
                + " (notes)\n";
    }
}
