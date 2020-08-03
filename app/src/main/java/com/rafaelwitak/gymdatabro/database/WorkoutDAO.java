package com.rafaelwitak.gymdatabro.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WorkoutDAO {
    @Query("SELECT * FROM workouts")
    public List<Workout> getAllWorkouts();

    @Query("SELECT * FROM workouts WHERE program_id=:programID")
    public List<Workout> getAllByProgram(int programID);

    @Query("SELECT * FROM workouts WHERE id=:id")
    public Workout getWorkoutByID(int id);

    @Query("SELECT * FROM workouts WHERE name=:name")
    public Workout getWorkoutbyName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertWorkout(Workout workout);

    @Update
    public void updateWorkout(Workout workout);
}
