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
import com.rafaelwitak.gymdatabro.util.UniqueWorkout;
import java8.util.Comparators;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

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
    public void updateWorkoutInstancesOfProgram(
            int programId,
            @NonNull List<WorkoutInstance> editedInstances,
            AlertDialog alertDialog) {

        new WorkoutInstanceUpdater(
                this, programId, editedInstances)
                .update();
    }


    @Query("SELECT " +
            "instance_id, " +
            "workout_number, " +
            "workout_id, " +
            "program_id, " +
            "instance_name, " +
            "name AS workout_name," +
            "details, " +
            "notes " +
            "FROM (" +
            "SELECT " +
            "id AS instance_id, " +
            "name AS instance_name, " +
            "program_id AS instance_program_id," +
            "workout_id, " +
            "workout_number " +
            "FROM workout_instances WHERE id = :instanceId)" +
            "INNER JOIN workouts " +
            "ON workout_id = workouts.id " +
            "AND instance_program_id = workouts.program_id;")
    public abstract UniqueWorkout getUniqueWorkoutFromInstanceId(int instanceId);

    public List<UniqueWorkout> getUniqueWorkoutsOfProgram(int programId) {
        return StreamSupport.stream(getAllWorkoutInstancesForProgram(programId))
                .map(inst -> getUniqueWorkoutFromInstanceId(inst.getId()))
                .sorted(Comparators.comparing(UniqueWorkout::getWorkoutNumber))
                .collect(Collectors.toList());
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
