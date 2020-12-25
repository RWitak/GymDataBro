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
public interface MuscleInvolvementDAO {
    @Query("SELECT involvement_level FROM muscle_involvements "
            + "WHERE muscle_group_name=:muscleGroupName "
            + "AND exercise_id=:id")
    public float getMuscleInvolvement(String muscleGroupName, int id);

    @Query("SELECT * FROM muscle_involvements "
            + "WHERE muscle_group_name = :muscleGroupName")
    public List<MuscleInvolvement> getAllByMuscleGroupName(String muscleGroupName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertMuscleInvolvement(MuscleInvolvement muscleInvolvement);

    @Update
    public void updateMuscleInvolvement(MuscleInvolvement muscleInvolvement);
}
