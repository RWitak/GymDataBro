package com.rafaelwitak.gymdatabro.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SetDAO {
    @Query("SELECT * FROM sets")
    public LiveData<List<Set>> getAllSets();

    @Query("SELECT * FROM sets WHERE exercise_id=:exerciseID")
    public LiveData<List<Set>> getAllByExerciseID(int exerciseID);

    @Query("SELECT * FROM sets WHERE timestamp BETWEEN :from AND :to")
    public LiveData<List<Set>> getAllBetweenTimestamps(int from, int to);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insertSet(Set set);

    @Update
    public void updateSet(Set set);
}
