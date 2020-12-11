package com.rafaelwitak.gymdatabro.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface PerformanceSetDAO {
    @Query("SELECT * FROM performance_sets")
    List<PerformanceSet> getAllSets();

    @Query("SELECT * FROM performance_sets WHERE rowid = :rowId")
    PerformanceSet getSetByRowId(long rowId);

    @Query("SELECT * FROM performance_sets WHERE id = (" +
            "SELECT id FROM workout_steps " +
            "WHERE exercise_id=:exerciseID);")
    List<PerformanceSet> getAllByExerciseID(int exerciseID);

    @Query("SELECT * FROM performance_sets WHERE timestamp BETWEEN :from AND :to")
    List<PerformanceSet> getAllBetweenTimestamps(Date from, Date to);

    @Query("SELECT * FROM performance_sets WHERE timestamp = (" +
                "SELECT MAX(timestamp) FROM performance_sets) " +
            "LIMIT 1;")
    PerformanceSet getLatestPerformanceSet();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertSet(PerformanceSet performanceSet);

    @Update
    void updateSet(PerformanceSet performanceSet);
}
