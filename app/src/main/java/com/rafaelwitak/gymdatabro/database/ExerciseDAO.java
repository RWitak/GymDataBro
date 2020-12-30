/*
 * Copyright (c) 2020, Rafael Witak.
 */

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
    List<Exercise> getAllExercises();

    @Query("SELECT * FROM exercises WHERE id = :id")
    Exercise getExerciseByID(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNewExercise(Exercise exercise);

    @Update
    void updateExercise(Exercise exercise);

    @Query("SELECT * FROM exercises WHERE name LIKE :wildcardableName")
    List<Exercise> getExercisesByName(String wildcardableName);
}
