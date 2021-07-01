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
import com.rafaelwitak.gymdatabro.BuildConfig;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;


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
    public void updateWorkoutsOfProgram(int programId,
                                        @NonNull Collection<Workout> editedWorkoutList,
                                        AlertDialog alertDialog)
            throws CloneNotSupportedException {

        final List<Workout> oldOrder = getWorkoutsForProgramId(programId);

        // TODO: 01.07.2021 Re-evaluate as soon as new Workout field for order is implemented
        // EASY CASES

        if (editedWorkoutList.equals(oldOrder)){
            return;
        }

        if (editedWorkoutList.isEmpty()) {
            for (Workout w : oldOrder) {
                deleteWorkout(w);
                return;
            }
        }

        // REGULAR CASE

        // Rebuild necessary Workouts and their WorkoutSteps with new IDs
        for (Workout workout : editedWorkoutList) {
            final List<WorkoutStep> workoutSteps =
                    getAllStepsForWorkoutId(workout.getId());

            long id = insertWorkoutForId(workout.duplicateWithIdZero());
            if (BuildConfig.DEBUG && id >= Integer.MAX_VALUE) {
                throw new AssertionError("Too many entries for Workouts!");
            }
            assert workoutSteps != null;
            rebuildWorkoutSteps(workoutSteps, (int) id);
        }

        // Collect obsolete Workouts
        final HashSet<Integer> obsoleteWorkoutEntries = new HashSet<>();
        obsoleteWorkoutEntries.addAll(StreamSupport.stream(oldOrder)
                .map(Workout::getId)
                .collect(Collectors.toSet()));
        obsoleteWorkoutEntries.addAll(StreamSupport.stream(editedWorkoutList)
                .map(Workout::getId)
                .collect(Collectors.toSet()));

        // Delete WorkoutSteps for obsolete Workouts
        StreamSupport.stream(obsoleteWorkoutEntries)
                .flatMap(workoutId -> StreamSupport.stream(Objects.requireNonNull(
                        getAllStepsForWorkoutId(workoutId))))
                .distinct()
                .forEach(this::deleteWorkoutStep);

        // delete all obsolete workouts and steps
        StreamSupport.stream(obsoleteWorkoutEntries)
                .map(this::getWorkoutByID)
                .forEach(this::deleteWorkout);
    }

    private void rebuildWorkoutSteps(List<WorkoutStep> workoutSteps,
                                     int workoutId)
            throws CloneNotSupportedException {

        for (WorkoutStep workoutStep : workoutSteps) {
            WorkoutStep duplicate = workoutStep.duplicateWithIdZero();
            duplicate.setWorkoutID(workoutId);
            insertNewWorkoutStep(duplicate);
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
