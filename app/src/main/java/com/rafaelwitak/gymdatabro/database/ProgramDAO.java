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
}
