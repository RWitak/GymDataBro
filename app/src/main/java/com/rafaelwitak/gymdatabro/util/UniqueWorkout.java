/*
 * Copyright (c) 2021, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.util;

import androidx.room.ColumnInfo;
import com.rafaelwitak.gymdatabro.database.Workout;
import com.rafaelwitak.gymdatabro.database.WorkoutInstance;
import java8.util.Comparators;

import java.util.Comparator;

public class UniqueWorkout implements Comparable<UniqueWorkout>{
    @ColumnInfo(name = "instance_id")
    private final int instanceId;
    @ColumnInfo(name = "workout_number")
    private final int workoutNumber;
    @ColumnInfo(name = "workout_id")
    private final int workoutId;
    @ColumnInfo(name = "program_id")
    private final Integer programId;
    @ColumnInfo(name = "instance_name")
    private final String instanceName;
    @ColumnInfo(name = "workout_name")
    private final String workoutName;
    @ColumnInfo(name = "details")
    private final String details;
    @ColumnInfo(name = "notes")
    private final String notes;

    public int getInstanceId() {
        return instanceId;
    }

    public int getWorkoutNumber() {
        return workoutNumber;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public Integer getProgramId() {
        return programId;
    }

    public String computeName() {
        return instanceName != null ? instanceName : workoutName;
    }

    public String getDetails() {
        return details;
    }

    public String getNotes() {
        return notes;
    }

    public UniqueWorkout(Workout workout, WorkoutInstance instance) {
        if (workout.getId() != instance.getWorkoutId()) {
            throw new AssertionError("WorkoutID doesn't match.");
        }
        this.instanceId = instance.getId();
        this.workoutNumber = instance.getWorkoutNumber();
        this.workoutId = workout.getId();
        this.programId = workout.getProgramID();
        this.instanceName = instance.getName();
        this.workoutName = workout.getName();
        this.details = workout.getDetails();
        this.notes = workout.getNotes();
    }

    public UniqueWorkout(int instanceId,
                         int workoutNumber,
                         int workoutId,
                         Integer programId,
                         String instanceName,
                         String workoutName,
                         String details,
                         String notes) {
        this.instanceId = instanceId;
        this.workoutNumber = workoutNumber;
        this.workoutId = workoutId;
        this.programId = programId;
        this.instanceName = instanceName;
        this.workoutName = workoutName;
        this.details = details;
        this.notes = notes;
    }

    @Override
    public int compareTo(UniqueWorkout o) {
        final Comparator<UniqueWorkout> byWorkoutId =
                Comparators.comparing(UniqueWorkout::getWorkoutId);
        final Comparator<UniqueWorkout> byWorkoutNumber =
                Comparators.comparing(UniqueWorkout::getWorkoutNumber);

            return Comparators.thenComparing(
                    byWorkoutId,
                    byWorkoutNumber)
                    .compare(this, o);
    }

    public WorkoutInstance toInstance() {
        return new WorkoutInstance(
                instanceId,
                instanceName,
                programId,
                workoutId,
                workoutNumber
                );
    }
}
