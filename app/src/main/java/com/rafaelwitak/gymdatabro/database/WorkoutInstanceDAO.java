package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public abstract class WorkoutInstanceDAO {
    @Query("SELECT name FROM workout_instances WHERE id=:id")
    public abstract String getNameByInstanceId(Integer id);

    @Query("SELECT MAX(workout_instance_id) FROM performance_sets WHERE timestamp = (" +
            "SELECT MAX(timestamp) FROM performance_sets " +
            "JOIN workout_instances " +
            "ON performance_sets.workout_instance_id = workout_instances.id " +
            "WHERE program_id=:programId);")
    public abstract Integer getLatestWorkoutInstanceIdForProgramId(int programId);

    @Query("SELECT * FROM workout_instances WHERE id = (" +
                "SELECT MAX(workout_instance_id) FROM performance_sets WHERE timestamp = (" +
                   "SELECT MAX(timestamp) FROM performance_sets));")
    public abstract WorkoutInstance getLatestWorkoutInstance();

    @Query("SELECT * FROM workout_instances " +
            "WHERE program_id=:programId AND workout_number=:workoutNumber")
    public abstract WorkoutInstance getWorkoutInstance(int programId, int workoutNumber);

    @Nullable
    public WorkoutInstance getNextWorkoutInstance() {
        WorkoutInstance latestInstance = getLatestWorkoutInstance();
        if (latestInstance == null) {
            return null;
        }

        return getWorkoutInstance(
                latestInstance.programId,
                latestInstance.workoutNumber + 1);
    }

    @Query("SELECT * FROM workout_instances " +
            "WHERE program_id=:programId " +
            "AND workout_number = (" +
                "SELECT MIN(workout_number) FROM workout_instances " +
                "WHERE program_id=:programId)")
    public abstract WorkoutInstance getFirstInstanceOfProgram(Integer programId);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract long insertWorkoutInstance(WorkoutInstance workoutInstance);

    @Update(onConflict = OnConflictStrategy.ABORT)
    public abstract void updateWorkoutInstance(WorkoutInstance workoutInstance);
}
