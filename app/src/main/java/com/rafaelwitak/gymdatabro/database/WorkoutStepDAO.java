package com.rafaelwitak.gymdatabro.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WorkoutStepDAO {
    @Query("SELECT * FROM workout_steps")
    public LiveData<List<WorkoutStep>> getAllWorkoutSteps();

    @Query("SELECT * FROM workout_steps WHERE workout_id=:workoutID")
    public LiveData<List<WorkoutStep>> getAllStepsForWorkout(int workoutID);

    @Query("SELECT * FROM workout_steps WHERE workout_id=:workoutID "
            + "AND number=:stepNumber")
    public LiveData<WorkoutStep> getWorkoutStep(int workoutID, int stepNumber);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertWorkoutStep(WorkoutStep workoutStep);

    @Update
    public void updateWorkoutStep(WorkoutStep workoutStep);
}