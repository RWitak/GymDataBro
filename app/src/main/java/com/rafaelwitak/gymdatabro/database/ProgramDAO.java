/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.database;

import androidx.room.*;

import java.util.List;

@Dao
public interface ProgramDAO {
    @Query("SELECT * FROM programs")
    List<Program> getAllPrograms();

    @Query("SELECT * FROM programs WHERE id=:id")
    Program getProgramByID(int id);

    @Query("SELECT COUNT(*) FROM programs")
    int getTotalNumberOfPrograms();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertProgram(Program program);

    @Update
    void updateProgram(Program program);

    @Delete
    void deleteProgram(Program program);
    // FIXME: 25.09.2021 Works with program that already has Workouts / W-Instances,
    //  but fails if Steps or PerformanceSets exist.
}
