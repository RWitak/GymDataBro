/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.database;

import androidx.room.*;

import java.util.List;

@Dao
public interface WorkoutDAO {
    @Query("SELECT * FROM workouts")
    List<Workout> getAllWorkouts();

    @Query("SELECT * FROM workouts WHERE program_id=:programID")
    List<Workout> getAllByProgram(int programID);

    @Query("SELECT COUNT(*) FROM workouts WHERE program_id=:programID")
    Integer getNumberOfWorkoutsByProgram(int programID);

    @Query("SELECT * FROM workouts WHERE id=:id")
    Workout getWorkoutByID(int id);

    @Query("SELECT * FROM workouts WHERE name=:name")
    Workout getWorkoutByName(String name);

    @Query("SELECT COUNT(exercise_id) FROM workout_steps " +
            "WHERE exercise_id=:exerciseID AND workout_id=:workoutID")
    Integer getOccurrencesOfExerciseInWorkout(int exerciseID, int workoutID);

    @Query("SELECT number FROM workout_steps " +
            "WHERE exercise_id=:exerciseID AND workout_id=:workoutID " +
            "ORDER BY number ASC")
    List<Integer> getNumbersOfExerciseInWorkout(int exerciseID, int workoutID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertWorkoutForId(Workout workout);

    @Update
    void updateWorkout(Workout workout);

    @Delete
    void deleteWorkout(Workout workout);
}
