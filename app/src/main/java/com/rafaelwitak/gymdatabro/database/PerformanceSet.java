/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.*;

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
                        childColumns = "workout_instance_id"),
        },
        indices = {
                @Index(
                        value = "workout_step_id",
                        name = "index_performance_sets_workout_step_id"),
                @Index(
                        value = "workout_instance_id",
                        name = "index_performance_sets_workout_instance_id"
                )
        }
)
public class PerformanceSet {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "workout_step_id")
    private Integer workoutStepId;

    @ColumnInfo(name = "workout_instance_id")
    private Integer workoutInstanceId;

    @ColumnInfo
    private Date timestamp;

    @ColumnInfo
    @Nullable
    private Integer reps;

    @ColumnInfo
    @Nullable
    private Float weight;

    @ColumnInfo(name = "seconds_performed")
    @Nullable
    private Integer secondsPerformed;

    @ColumnInfo(name = "seconds_rested")
    @Nullable
    private Integer secondsRested;

    @ColumnInfo
    @Nullable
    private Float rpe;

    @ColumnInfo(name = "pain_level")
    @NonNull
    private Integer painLevel = 0;

    @ColumnInfo
    @Nullable
    private String notes;

    @ColumnInfo(defaultValue = "1")
    private boolean active;

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
                          @Nullable String notes,
                          @Nullable Boolean active) {
        this.setActive(active);
        this.setId(id);
        this.setWorkoutStepId(workoutStepId);
        this.setWorkoutInstanceId(workoutInstanceId);
        this.setTimestamp(timestamp == null ? getTimestamp() : timestamp);
        this.setReps(reps);
        this.setWeight(weight);
        this.setSecondsPerformed(secondsPerformed);
        this.setSecondsRested(secondsRested);
        this.setRpe(rpe);
        this.setPainLevel(painLevel);
        this.setNotes(notes);
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
                null,
                null
        );
    }

    public Date getTimestamp() {
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
        return this.getId()
                + " (ID)\n"
                + this.getTimestamp()
                + " (timestamp)\n"
                + this.getWorkoutStepId()
                + " (workoutStepId)\n"
                + this.getWorkoutInstanceId()
                + " (workoutInstanceId)\n"
                + this.getReps()
                + " (reps)\n"
                + this.getWeight()
                + " (weight)\n"
                + this.getSecondsPerformed()
                + " (secondsPerformed)\n"
                + this.getSecondsRested()
                + " (secondsRested)\n"
                + this.getRpe()
                + " (rpe)\n"
                + this.getPainLevel()
                + " (painLevel)\n"
                + this.getNotes()
                + " (notes)\n";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWorkoutStepId() {
        return workoutStepId;
    }

    public Integer getWorkoutInstanceId() {
        return workoutInstanceId;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Nullable
    public Integer getReps() {
        return reps;
    }

    @Nullable
    public Float getWeight() {
        return weight;
    }

    @Nullable
    public Integer getSecondsPerformed() {
        return secondsPerformed;
    }

    @Nullable
    public Integer getSecondsRested() {
        return secondsRested;
    }

    @Nullable
    public Float getRpe() {
        return rpe;
    }

    @NonNull
    public Integer getPainLevel() {
        return painLevel;
    }

    @Nullable
    public String getNotes() {
        return notes;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active != null ? active : true;
    }}
