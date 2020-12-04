package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(
        tableName = "performance_sets",
        foreignKeys = {
                @ForeignKey(
                        entity = WorkoutStep.class,
                        parentColumns = "id",
                        childColumns = "workout_step_id"),
                @ForeignKey(
                        entity = WorkoutInstance.class,
                        parentColumns = "id",
                        childColumns = "workout_instance_id")
        }
)
public class PerformanceSet {
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    @ColumnInfo(name = "workout_step_id")
    public Integer workoutStepId;

    @ColumnInfo(name = "workout_instance_id")
    public Integer workoutInstanceId;

    @ColumnInfo
    public Date timestamp;

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
    public Integer painLevel;

    @ColumnInfo
    @Nullable
    public String notes;

    @Ignore
    public PerformanceSet(@Nullable Integer id,
                          @Nullable Integer workoutStepId,
                          @Nullable Integer workoutInstanceId,
                          @Nullable Date timestamp,
                          @Nullable Integer reps,
                          @Nullable Float weight,
                          @Nullable Integer secondsPerformed,
                          @Nullable Integer secondsRested,
                          @Nullable Float rpe,
                          @NonNull Integer painLevel,
                          @Nullable String notes) {
        this.id = id;
        this.workoutStepId = workoutStepId;
        this.workoutInstanceId = workoutInstanceId;
        this.timestamp = timestamp == null ? getTimestamp() : timestamp;
        this.reps = reps;
        this.weight = weight;
        this.secondsPerformed = secondsPerformed;
        this.secondsRested = secondsRested;
        this.rpe = rpe;
        this.painLevel = painLevel;
        this.notes = notes;
    }

    public PerformanceSet() {
        this(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                0,
                null
        );
    }

    private Date getTimestamp() {
        return new Date();
    }

    public void setWorkoutStepId(Integer workoutStepId) {
        this.workoutStepId = workoutStepId;
    }

    public void setWorkoutInstanceId(Integer workoutInstanceId) {
        this.workoutInstanceId = workoutInstanceId;
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
                + this.workoutStepId
                + " (workoutStepId)\n"
                + this.workoutInstanceId
                + " (workoutInstanceId)\n"
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
