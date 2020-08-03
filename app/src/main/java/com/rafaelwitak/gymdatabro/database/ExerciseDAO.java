package com.rafaelwitak.gymdatabro.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExerciseDAO {
    @Query("SELECT * FROM exercises")
    public List<Exercise> getAllExercises();

    @Query("SELECT * FROM exercises WHERE id = :id")
    public Exercise getExerciseByID(int id);

    @Query("SELECT * FROM exercises WHERE equipment = :equipment")
    public List<Exercise> getExerciseListByEquipment(String equipment);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertNewExercise(Exercise exercise);

    @Update
    public void updateExercise(Exercise exercise);
}
