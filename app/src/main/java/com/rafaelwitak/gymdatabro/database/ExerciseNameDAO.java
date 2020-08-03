package com.rafaelwitak.gymdatabro.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExerciseNameDAO {
    @Query("SELECT * FROM exercise_names WHERE exercise_id=:id")
    public List<ExerciseName> getAllNamesByID(int id);

    @Query("SELECT name FROM exercise_names WHERE exercise_id=:id AND is_main_name=1")
    public String getMainNameByID(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertExerciseName(ExerciseName exerciseName);

    @Update
    public void updateExerciseName(ExerciseName exerciseName);
}
