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
    public List<Program> getAllPrograms();

    @Query("SELECT * FROM programs WHERE id=:id")
    public Program getProgramByID(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertProgram(Program program);

    @Update
    public void updateProgram(Program program);
}
