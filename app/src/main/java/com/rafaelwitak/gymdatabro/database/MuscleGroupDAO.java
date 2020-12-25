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
public interface MuscleGroupDAO {
    @Query("SELECT * FROM muscle_groups")
    public List<MuscleGroup> getAllMuscleGroups();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertMuscleGroup(MuscleGroup muscleGroup);

    @Update
    public void updateMuscleGroup(MuscleGroup muscleGroup);
}
