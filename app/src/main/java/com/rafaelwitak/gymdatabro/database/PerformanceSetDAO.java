package com.rafaelwitak.gymdatabro.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface PerformanceSetDAO {
    @Query("SELECT * FROM sets")
    List<PerformanceSet> getAllSets();

    @Query("SELECT * FROM sets WHERE rowid = :rowId")
    PerformanceSet getSetByRowId(long rowId);

    @Query("SELECT * FROM sets WHERE exercise_id=:exerciseID")
    List<PerformanceSet> getAllByExerciseID(int exerciseID);

    @Query("SELECT * FROM sets WHERE timestamp BETWEEN :from AND :to")
    List<PerformanceSet> getAllBetweenTimestamps(Date from, Date to);

    @Insert
    long insertSet(PerformanceSet performanceSet);

    @Update
    void updateSet(PerformanceSet performanceSet);
}
