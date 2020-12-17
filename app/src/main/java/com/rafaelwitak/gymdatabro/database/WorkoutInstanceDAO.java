package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public abstract class WorkoutInstanceDAO {
    @Nullable
    @Query("SELECT name FROM workout_instances WHERE id=:id")
    public abstract String getNameByInstanceId(Integer id);

    @Nullable
    @Query(
            "SELECT MAX(workout_instance_id) FROM performance_sets WHERE timestamp = (" +
                "SELECT MAX(timestamp) FROM performance_sets " +
                    "JOIN workout_instances " +
                    "ON performance_sets.workout_instance_id = workout_instances.id " +
                "WHERE program_id=:programId);")
    public abstract Integer getLatestWorkoutInstanceIdForProgramId(int programId);

    @Nullable
    @Query("SELECT * FROM workout_instances WHERE id = (" +
                "SELECT MAX(workout_instance_id) FROM performance_sets WHERE timestamp = (" +
                   "SELECT MAX(timestamp) FROM performance_sets));")
    public abstract WorkoutInstance getLatestWorkoutInstance();

    @Nullable
    @Query("SELECT * FROM workout_instances " +
            "WHERE program_id=:programId AND workout_number=:workoutNumber")
    public abstract WorkoutInstance getWorkoutInstance(int programId, int workoutNumber);

    @Nullable
    @Query(
            "SELECT * FROM workout_instances " +
                    "WHERE program_id = :programId " +
                    "AND workout_number = (" +
                        "SELECT MIN(workout_number) FROM workout_instances " +
                        "WHERE program_id = :programId " +
                        "AND workout_number > :previousWorkoutNumber);"
    )
    public abstract WorkoutInstance getNextWorkoutInstanceForProgram(
            Integer programId, Integer previousWorkoutNumber);

    @Nullable
    @Query(
            "SELECT * FROM workout_instances " +
            "WHERE program_id=:programId " +
            "AND workout_number = (" +
                "SELECT MIN(workout_number) FROM workout_instances " +
                "WHERE program_id=:programId)")
    public abstract WorkoutInstance getFirstWorkoutInstanceForProgram(Integer programId);

    @Query(
            "SELECT :instanceId = (" +
                    "SELECT id FROM workout_instances WHERE workout_number = (" +
                        "SELECT MAX(workout_number) FROM workout_instances " +
                        "WHERE program_id = :programId))"
    )
    public abstract boolean isLastInstanceOfProgram(int instanceId, Integer programId);


    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract long insertWorkoutInstance(WorkoutInstance workoutInstance);

    @Update(onConflict = OnConflictStrategy.ABORT)
    public abstract void updateWorkoutInstance(WorkoutInstance workoutInstance);
}
