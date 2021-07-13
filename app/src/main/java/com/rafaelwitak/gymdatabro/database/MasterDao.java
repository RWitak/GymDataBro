/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.database;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.ArrayList;
import java.util.List;


@Dao
public abstract class MasterDao extends WorkoutInstanceDAO
        implements WorkoutStepDAO, WorkoutDAO, PerformanceSetDAO, ProgramDAO, ExerciseDAO  {
    @Nullable
    @Query(
            "SELECT * FROM workout_instances WHERE workout_instances.id = (" +
                    "SELECT workout_instance_id FROM performance_sets WHERE timestamp = (" +
                        "SELECT MAX(timestamp) FROM performance_sets " +
                            "JOIN workout_steps " +
                                "ON performance_sets.workout_step_id = workout_steps.id " +
                            "JOIN workouts " +
                                "ON workout_steps.workout_id = workouts.id " +
                        "WHERE program_id = :programId) " +
                    "LIMIT 1);"
    )
    public abstract WorkoutInstance getLatestWorkoutInstanceForProgram(Integer programId);

    @Nullable
    @Query(
            "SELECT * FROM workout_steps WHERE id = (" +
                "SELECT workout_step_id FROM performance_sets " +
                "WHERE timestamp = (" +
                    "SELECT MAX(timestamp) FROM (" +
                    "SELECT timestamp FROM performance_sets " +
                    "JOIN workout_steps " +
                        "ON performance_sets.workout_step_id = workout_steps.id " +
                    "JOIN workouts " +
                        "ON workout_steps.workout_id = workouts.id " +
                    "WHERE program_id = :programId)))"
    )
    public abstract WorkoutStep getLatestWorkoutStepForProgramId(Integer programId);

    @Nullable
    @Query(
            "SELECT * FROM workout_steps WHERE number = (" +
                    "SELECT MIN(number) FROM workout_steps WHERE workout_id = (" +
                        "SELECT workout_id FROM workout_instances " +
                        "WHERE workout_number = (" +
                            "SELECT MIN(workout_number) FROM workout_instances " +
                            "WHERE program_id = :programId) " +
                            "AND program_id = :programId))" +
                    "AND workout_id = (" +
                        "SELECT workout_id FROM workout_instances " +
                        "WHERE workout_number = (" +
                            "SELECT MIN(workout_number) FROM workout_instances " +
                            "WHERE program_id = :programId) " +
                            "AND program_id = :programId);"
    )
    public abstract WorkoutStep getFirstWorkoutStepOfProgram(int programId);

    @Nullable
    public WorkoutStep getNextWorkoutStepForProgramId(
            Integer programId,
            WorkoutInstance nextInstance) {

        if (nextInstance == null) {
            Log.d("GDB", "nextInstance == null");
            return getFirstWorkoutStepOfProgram(programId);
        }

        WorkoutStep latestWorkoutStep = getLatestWorkoutStepForProgramId(programId);
        if (latestWorkoutStep == null) {
            Log.d("GDB", "latestWorkoutStep == null");
            return getFirstWorkoutStepOfProgram(programId);
        }
        if (isLastStepOfWorkout(
                latestWorkoutStep.getWorkoutID(),
                latestWorkoutStep.getNumber())) {
            Log.d("GDB", "latestWorkoutStep is last step of Workout");
            return getFirstStepOfWorkout(nextInstance.getWorkoutId());
        }
        return getNextStepInWorkout(
                latestWorkoutStep.getId(),
                latestWorkoutStep.getWorkoutID());
    }

    @Nullable
    @Query(
            "SELECT * FROM performance_sets WHERE timestamp = (" +
                    "SELECT MAX(timestamp) FROM performance_sets " +
                        "JOIN workout_instances " +
                        "ON performance_sets.workout_instance_id = workout_instances.id " +
                    "WHERE program_id = :programId) " +
            "LIMIT 1;"
    )
    public abstract PerformanceSet getLatestPerformanceSetForProgramId(Integer programId);

    @Nullable
    @Query(
            "SELECT weight, reps, rpe FROM performance_sets " +
                    "WHERE timestamp = (" +
                    "SELECT MAX(timestamp) from performance_sets " +
                    "JOIN workout_steps " +
                        "ON performance_sets.workout_step_id = workout_steps.id " +
                    "WHERE exercise_id = :exerciseId)"
    )
    public abstract WeightRepsRpe getLatestWeightRepsRpeForExercise(int exerciseId);

    @Query(
            "SELECT * FROM workouts WHERE program_id = :programId ORDER BY id;"
    )
    public abstract List<Workout> getWorkoutsForProgramId(int programId);

    @Transaction
    public void updateWorkoutInstancesOfProgram(int programId,
                                                @NonNull List<WorkoutInstance> editedInstances,
                                                AlertDialog alertDialog) {
        final List<WorkoutInstance> oldOrder =
                getAllWorkoutInstancesForProgram(programId);

        // no changes
        if (editedInstances.equals(oldOrder)){
            return;
        }

        // handle deletions of unused instances
        oldOrder.removeAll(editedInstances);
        for (WorkoutInstance instance : oldOrder) {
            deleteWorkoutInstance(instance);
            // FIXME: 02.07.2021 Leaves orphans if deletion doesn't cascade.
        }

        // persist new duplicates
        List<WorkoutInstance> newInstanceOrder = new ArrayList<>();
        for (WorkoutInstance instance : editedInstances) {
            if (newInstanceOrder.contains(instance)) {
                instance = newPersistedInstanceFromClone(instance);
            }
            newInstanceOrder.add(instance);
        }

        // set order according to list indices and update WO-Instance in DB
        for (WorkoutInstance instance : newInstanceOrder) {
            instance.setWorkoutNumber(newInstanceOrder.indexOf(instance));
            updateWorkoutInstance(instance);
        }
    }

    public static class WeightRepsRpe {
        public Float weight;
        public Integer reps;
        public Float rpe;

        public WeightRepsRpe(Float weight, Integer reps, Float rpe) {
            this.weight = weight;
            this.reps = reps;
            this.rpe = rpe;
        }
    }
}
