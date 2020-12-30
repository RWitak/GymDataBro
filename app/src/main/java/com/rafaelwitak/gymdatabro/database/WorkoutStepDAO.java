/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WorkoutStepDAO {
    @Nullable
    @Query("SELECT * FROM workout_steps WHERE workout_id=:workoutID")
    List<WorkoutStep> getAllStepsForWorkout(int workoutID);

    @Nullable
    @Query("SELECT * FROM workout_steps WHERE workout_id=:workoutID "
            + "AND number=:stepNumber")
    WorkoutStep getWorkoutStep(int workoutID, int stepNumber);

    @Query("SELECT COUNT(*) FROM workout_steps WHERE workout_id=:workoutID")
    int getNumberOfStepsInWorkout(int workoutID);

    @Query(
            "SELECT :workoutStepNumber = (" +
                    "SELECT MAX(number) FROM workout_steps WHERE workout_id = :workoutId);"
    )
    boolean isLastStepOfWorkout(Integer workoutId, Integer workoutStepNumber);

    @Query(
            "SELECT * FROM workout_steps WHERE id=:workoutStepId;"
    )
    WorkoutStep getWorkoutStepById(Integer workoutStepId);

    @Nullable
    @Query(
            "SELECT * FROM workout_steps WHERE workout_id = :workoutId " +
                    "AND number = (" +
                    "SELECT MIN(number) FROM workout_steps WHERE workout_id = :workoutId " +
                    "AND number > (" +
                    "SELECT number FROM workout_steps WHERE id = :latestWorkoutStepId));"
    )
    WorkoutStep getNextStepInWorkout(
            Integer latestWorkoutStepId, Integer workoutId);

    @Nullable
    @Query(
            "SELECT * FROM workout_steps " +
                    "WHERE workout_id = :workoutId " +
                    "AND number = (" +
                        "SELECT MIN(number) FROM workout_steps " +
                        "WHERE workout_id = :workoutId);"
    )
    WorkoutStep getFirstStepOfWorkout(Integer workoutId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNewWorkoutStep(WorkoutStep workoutStep);

    @Update
    void updateWorkoutStep(WorkoutStep workoutStep);
}
