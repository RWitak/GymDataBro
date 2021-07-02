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
import java8.util.stream.StreamSupport;

import java.util.Collections;
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
                                                @NonNull List<WorkoutInstance> editedWorkoutInstances,
                                                AlertDialog alertDialog) {

        final List<WorkoutInstance> oldOrder = getAllWorkoutInstancesForProgram(programId);

        // TODO: 02.07.2021 Check for safety and completeness.

        // no changes
        if (editedWorkoutInstances.equals(oldOrder)){
            return;
        }

        // persist new duplicates
        final List<WorkoutInstance> finalWorkoutInstanceList =
                StreamSupport.stream(editedWorkoutInstances)
                        .sequential()
                        .map((WorkoutInstance instance) -> {
                            if (Collections.frequency(editedWorkoutInstances, instance) > 1) {
                                final WorkoutInstance duplicate =
                                        instance.duplicateWithIdZero();
                                final long id = insertWorkoutInstanceForId(duplicate);
                                if (id >= Integer.MAX_VALUE) {
                                    throw new AssertionError("ID of WorkoutInstance too large.");
                                }
                                return getWorkoutInstance(duplicate.getProgramId(), (int) id);
                            }
                            return instance;
                        })
                        .toList();

        // set order to list indices
        for (WorkoutInstance instance : finalWorkoutInstanceList) {
            instance.setWorkoutNumber(finalWorkoutInstanceList.indexOf(instance));
            updateWorkoutInstance(instance);
        }

        // handle deletions
        oldOrder.removeAll(finalWorkoutInstanceList);
        for (WorkoutInstance instance : oldOrder) {
            deleteWorkoutInstance(instance);
            // FIXME: 02.07.2021 Leaves orphans if deletion doesn't cascade.
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
