package com.rafaelwitak.gymdatabro.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PerformanceSetDAO {
    @Query("SELECT * FROM sets")
    LiveData<List<PerformanceSet>> getAllSets();

    @Query("SELECT * FROM sets WHERE rowid = :rowId")
    PerformanceSet getSetByRowId(long rowId);

    @Query("SELECT * FROM sets WHERE exercise_id=:exerciseID")
    LiveData<List<PerformanceSet>> getAllByExerciseID(int exerciseID);

    @Query("SELECT * FROM sets WHERE timestamp BETWEEN :from AND :to")
    LiveData<List<PerformanceSet>> getAllBetweenTimestamps(int from, int to);

    @Query("INSERT INTO sets " +
                "(exercise_id, " +
                "reps, " +
                "weight, " +
                "seconds_performed, " +
                "seconds_rested, " +
                "rpe, " +
                "pain_level, " +
                "notes) " +
            "VALUES(" +
                ":exercise_id, " +
                ":reps, " +
                ":weight, " +
                ":seconds_performed, " +
                ":seconds_rested, " +
                ":rpe, " +
                ":pain_level, " +
                ":notes) ")
    long insertSet(int exercise_id,
                   Integer reps,
                   Float weight,
                   Integer seconds_performed,
                   Integer seconds_rested,
                   Float rpe,
                   Integer pain_level,
                   String notes);

    @Update
    void updateSet(PerformanceSet performanceSet);
}
