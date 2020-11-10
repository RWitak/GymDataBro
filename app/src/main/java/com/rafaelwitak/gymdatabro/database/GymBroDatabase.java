package com.rafaelwitak.gymdatabro.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@androidx.room.Database(
        entities={
                Exercise.class,
                ExerciseName.class,
                MuscleGroup.class,
                MuscleInvolvement.class,
                Program.class,
                PerformanceSet.class,
                Workout.class,
                WorkoutStep.class
        },
        version = 9
)
@TypeConverters({Converters.class})
public abstract class GymBroDatabase extends RoomDatabase {
    public abstract ExerciseDAO exerciseDAO();
    public abstract ExerciseNameDAO exerciseNameDAO();
    public abstract MuscleGroupDAO muscleGroupDAO();
    public abstract MuscleInvolvementDAO muscleInvolvementDAO();
    public abstract ProgramDAO programDAO();
    public abstract PerformanceSetDAO performanceSetDAO();
    public abstract WorkoutDAO workoutDAO();
    public abstract WorkoutStepDAO workoutStepDAO();

    private static volatile GymBroDatabase INSTANCE;
    /*
    // Only needed if DB should be created freshly at each start.
    // Callback must be added,
    // see https://developer.android.com/codelabs/android-room-with-a-view#12

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    */
    public static GymBroDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GymBroDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room
                            .databaseBuilder(
                                    context.getApplicationContext(),
                                    GymBroDatabase.class,
                                    "gym_data")
                            .createFromAsset("gymdata.db")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
