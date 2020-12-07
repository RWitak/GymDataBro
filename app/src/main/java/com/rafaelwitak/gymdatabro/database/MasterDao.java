package com.rafaelwitak.gymdatabro.database;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public abstract class MasterDao extends WorkoutInstanceDAO
        implements WorkoutStepDAO, WorkoutDAO, PerformanceSetDAO, ProgramDAO  {
    @Query(
            "SELECT * FROM workout_instances WHERE workout_instances.id = (" +
                    "SELECT workout_instance_id FROM performance_sets WHERE timestamp = (" +
                        "SELECT MAX(timestamp) FROM performance_sets " +
                            "JOIN workout_steps " +
                                "ON performance_sets.workout_step_id = workout_steps.id " +
                            "JOIN workouts " +
                                "ON workout_steps.workout_id = workouts.id " +
                        "WHERE program_id = :programId) " +
                    "LIMIT 1);"
    )
    public abstract WorkoutInstance getLatestWorkoutInstanceForProgram(Integer programId);

    @Query(
            "SELECT * FROM workout_instances WHERE workout_instances.id = (" +
                    "SELECT workout_instances.id FROM workout_instances WHERE workout_number = (" +
                        "SELECT MIN(workout_number) FROM workout_instances " +
                            "WHERE program_id = :programId " +
                            "AND workout_number > :workoutNumber))"
    )
    public abstract WorkoutInstance getNextWorkoutInstanceForProgram(
            Integer programId, Integer workoutNumber);

    @Query(
            "SELECT * FROM workout_instances WHERE workout_number = (" +
                    "SELECT MIN(workout_number) FROM workout_instances " +
                    "WHERE program_id = :programId);"
    )
    public abstract WorkoutInstance getFirstWorkoutInstanceForProgram(Integer programId);

    @Query(
            "SELECT * FROM workout_steps " +
                "JOIN performance_sets " +
                    "ON workout_steps.id = performance_sets.workout_step_id " +
                "WHERE timestamp = (" +
                    "SELECT MAX(timestamp) FROM (" +
                    "SELECT timestamp FROM performance_sets " +
                    "JOIN workout_steps ON performance_sets.workout_step_id = workout_steps.id " +
                    "JOIN workouts ON workout_steps.workout_id = workouts.id " +
                    "WHERE program_id = :programId))"
    )
    public abstract WorkoutStep getNextWorkoutStepForProgramId(Integer programId);

    @Query(
            "SELECT * FROM workout_steps WHERE number = (" +
                    "SELECT MIN(number) FROM workout_steps WHERE workout_id = (" +
                        "SELECT workout_id FROM workout_instances " +
                        "WHERE workout_number = (" +
                            "SELECT MIN(workout_number) FROM workout_instances " +
                            "WHERE program_id = :programId))" +
            "AND workout_id = (SELECT workout_id FROM workout_instances " +
                    "WHERE workout_number = (" +
                        "SELECT MIN(workout_number) FROM workout_instances " +
                        "WHERE program_id = :programId)));"
    )
    public abstract WorkoutStep getFirstWorkoutStepOfProgram(int programId);
}
