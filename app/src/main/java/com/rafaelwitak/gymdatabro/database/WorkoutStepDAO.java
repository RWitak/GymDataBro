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

    // TODO: Include possibilities of finished Workout or even Program.
    /*
    @Query("SELECT * FROM workout_steps WHERE number = 1 + " +
            "IFNULL(" +
                "(SELECT MAX(number) FROM " +
                    "sets " +
                    "JOIN workout_steps ON sets.workout_step_id=workout_steps.id " + // update to workout_instance_id logic
                "WHERE timestamp = " +
                "(SELECT MAX(timestamp) FROM sets) " +
                "LIMIT 1), " +
            "-1);")
    WorkoutStep getNextGlobalWorkoutStep();

    @Query("SELECT * FROM workout_steps WHERE number = 1 + " +
            "(SELECT MAX(number) FROM sets WHERE timestamp = " +
                "(SELECT MAX(timestamp) FROM " +
                    "sets " +
                    "JOIN workout_steps ON sets.workout_step_id=workout_steps.id " + // update to workout_instance_id logic
                    "JOIN workouts ON workout_steps.workout_id=workouts.id " +
                "WHERE program_id=:programId) " +
            "LIMIT 1);")
    WorkoutStep getNextWorkoutStepForProgram(int programId);
    */

    @Query(
            "SELECT :workoutStepId = (" +
                    "SELECT MAX(number) FROM workout_steps WHERE workout_id = (" +
                        "SELECT workout_id FROM workout_steps WHERE id=:workoutStepId));"
    )
    boolean isLastStepOfWorkout(Integer workoutStepId);

    @Query(
            "SELECT * FROM workout_steps WHERE id=:workoutStepId;"
    )
    WorkoutStep getWorkoutStepById(Integer workoutStepId);

    @Query(
            "SELECT MIN(number) FROM workout_steps WHERE number > :latestNumber;"
    )
    Integer getNextNumberUp(Integer latestNumber);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNewWorkoutStep(WorkoutStep workoutStep);

    @Update
    void updateWorkoutStep(WorkoutStep workoutStep);
}
