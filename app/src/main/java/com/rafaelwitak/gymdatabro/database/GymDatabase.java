package com.rafaelwitak.gymdatabro.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@androidx.room.Database(
        entities={
                Exercise.class,
                ExerciseName.class,
                MuscleGroup.class,
                MuscleInvolvement.class,
                Program.class,
                Set.class,
                Workout.class,
                WorkoutStep.class
        },
        version = 1
)
public abstract class GymDatabase extends RoomDatabase {
    public abstract ExerciseDAO exerciseDAO();
    public abstract ExerciseNameDAO exerciseNameDAO();
    public abstract MuscleGroupDAO muscleGroupDAO();
    public abstract MuscleInvolvementDAO muscleInvolvementDAO();
    public abstract ProgramDAO programDAO();
    public abstract SetDAO setDAO();
    public abstract WorkoutDAO workoutDAO();
    public abstract WorkoutStepDAO workoutStepDAO();
}
