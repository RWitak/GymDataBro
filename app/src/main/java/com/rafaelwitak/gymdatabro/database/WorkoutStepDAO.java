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
    LiveData<List<WorkoutStep>> getAllWorkoutSteps();

    @Query("SELECT * FROM workout_steps WHERE workout_id=:workoutID")
    LiveData<List<WorkoutStep>> getAllStepsForWorkoutAsLiveData(int workoutID);

    @Query("SELECT * FROM workout_steps WHERE workout_id=:workoutID "
            + "AND number=:stepNumber")
    LiveData<WorkoutStep> getWorkoutStepAsLiveData(int workoutID, int stepNumber);

    @Query("SELECT * FROM workout_steps WHERE workout_id=:workoutID")
    List<WorkoutStep> getAllStepsForWorkoutSynchronously(int workoutID);

    @Query("SELECT * FROM workout_steps WHERE workout_id=:workoutID "
            + "AND number=:stepNumber")
    WorkoutStep getWorkoutStepSynchronously(int workoutID, int stepNumber);

    @Query("SELECT COUNT(*) FROM workout_steps WHERE workout_id=:workoutID")
    int getNumberOfStepsInWorkout(int workoutID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNewWorkoutStep(WorkoutStep workoutStep);

    @Update
    void updateWorkoutStep(WorkoutStep workoutStep);
}
