package com.rafaelwitak.gymdatabro.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface WorkoutInstanceDAO {
    @Query("SELECT name FROM workout_instances WHERE id=:id")
    String getNameByInstanceId(Integer id);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertWorkoutInstance(WorkoutInstance workoutInstance);

    @Update(onConflict = OnConflictStrategy.ABORT)
    void updateWorkoutInstance(WorkoutInstance workoutInstance);
}
