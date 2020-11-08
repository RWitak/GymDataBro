package com.rafaelwitak.gymdatabro.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


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
        version = 10
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
                            .addMigrations(MIGRATION_9_10)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    static final Migration MIGRATION_9_10 = new Migration(9, 10) {
        // Deleting columns img_a & img_b from exercises directly is not possible in SQLite
        // Therefore, we back the table up, drop and recreate it.
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TEMPORARY TABLE exercises_backup(" +
                    "id, pr, cues, links, equipment) ;"
            );
            database.execSQL(
                    "INSERT INTO exercises_backup " +
                    "SELECT id, pr, cues, links, equipment FROM exercises"
            );
            database.execSQL(
                    "DROP TABLE exercises"
            );
            database.execSQL(
                    "CREATE TABLE exercises (" +
                    " id    INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    " pr    REAL, " +
                    " cues  TEXT, " +
                    " links TEXT, " +
                    " equipment     TEXT)"
            );
            database.execSQL(
                    "INSERT INTO exercises " +
                    "SELECT id, pr, cues, links, equipment FROM exercises_backup"
            );
            database.execSQL(
                    "DROP TABLE exercises_backup"
            );
        }
    };
}
