package com.rafaelwitak.gymdatabro.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        version = 1,
        exportSchema = false
)
public abstract class GymBroDatabase extends RoomDatabase {
    public abstract ExerciseDAO exerciseDAO();
    public abstract ExerciseNameDAO exerciseNameDAO();
    public abstract MuscleGroupDAO muscleGroupDAO();
    public abstract MuscleInvolvementDAO muscleInvolvementDAO();
    public abstract ProgramDAO programDAO();
    public abstract SetDAO setDAO();
    public abstract WorkoutDAO workoutDAO();
    public abstract WorkoutStepDAO workoutStepDAO();

    private static volatile GymBroDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static GymBroDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GymBroDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GymBroDatabase.class, "gym_bro_database")
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}