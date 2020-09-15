package com.rafaelwitak.gymdatabro.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PerformanceSetDAO {
    @Query("SELECT * FROM sets")
    public LiveData<List<PerformanceSet>> getAllSets();

    @Query("SELECT * FROM sets WHERE exercise_id=:exerciseID")
    public LiveData<List<PerformanceSet>> getAllByExerciseID(int exerciseID);

    @Query("SELECT * FROM sets WHERE timestamp BETWEEN :from AND :to")
    public LiveData<List<PerformanceSet>> getAllBetweenTimestamps(int from, int to);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insertSet(PerformanceSet performanceSet);

    @Update
    public void updateSet(PerformanceSet performanceSet);
}
