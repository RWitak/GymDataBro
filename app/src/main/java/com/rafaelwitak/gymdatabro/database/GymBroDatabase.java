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
        version = 11
)
@TypeConverters({Converters.class})
public abstract class GymBroDatabase extends RoomDatabase {
    public abstract ExerciseDAO exerciseDAO();
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
                            .addMigrations(MIGRATION_9_10, MIGRATION_10_11)
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
                            " id    INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                            " pr    REAL, " +
                            " cues  TEXT, " +
                            " links TEXT, " +
                            " equipment     TEXT)"
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

    static final Migration MIGRATION_10_11 = new Migration(10, 11) {
        // Tables 'exercises' and 'workout_steps' both get a 'name' column
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            // Recreate 'exercises' table with added 'name' column
            database.execSQL(
                    "CREATE TEMPORARY TABLE exercises_backup(" +
                    " id    INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    " name  TEXT UNIQUE, " +
                    " pr    REAL, " +
                    " cues  TEXT, " +
                    " links TEXT, " +
                    " equipment     TEXT) ;"
            );
            database.execSQL(
                    "INSERT INTO exercises_backup SELECT * FROM exercises"
            );
            database.execSQL(
                    "DROP TABLE exercises"
            );
            database.execSQL(
                    "CREATE TABLE exercises (" +
                    " id    INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    " name  TEXT UNIQUE, " +
                    " pr    REAL, " +
                    " cues  TEXT, " +
                    " links TEXT, " +
                    " equipment     TEXT)"
            );
            database.execSQL(
                    "INSERT INTO exercises(id, pr, cues, links, equipment) " +
                    "SELECT id, pr, cues, links, equipment FROM exercises_backup"
            );
            database.execSQL(
                    "DROP TABLE exercises_backup"
            );

            // Recreate 'workout_steps' table with added 'name' column
            database.execSQL(
                    "CREATE TEMPORARY TABLE workout_steps_backup(" +
                            "workout_id INTEGER NOT NULL, " +
                            "number INTEGER NOT NULL, " +
                            "exercise_id INTEGER NOT NULL, " +
                            "reps INTEGER, " +
                            "weight REAL, " +
                            "rpe REAL, " +
                            "duration_seconds INTEGER, " +
                            "rest_seconds INTEGER, " +
                            "details TEXT, " +
                            "notes TEXT, " +
                            "FOREIGN KEY(exercise_id) REFERENCES exercises(id) " +
                            "ON UPDATE NO ACTION ON DELETE NO ACTION, " +
                            "FOREIGN KEY(workout_id) REFERENCES workouts(id) " +
                            "ON UPDATE NO ACTION ON DELETE NO ACTION, " +
                            "PRIMARY KEY(workout_id,number) " +
                    ");"
            );
            database.execSQL(
                    "INSERT INTO workout_steps_backup SELECT * FROM workout_steps"
            );
            database.execSQL(
                    "DROP TABLE workout_steps"
            );
            database.execSQL(
                    "CREATE TABLE workout_steps(" +
                            "workout_id INTEGER NOT NULL, " +
                            "number INTEGER NOT NULL, " +
                            "name TEXT, " +
                            "exercise_id INTEGER NOT NULL, " +
                            "reps INTEGER, " +
                            "weight REAL, " +
                            "rpe REAL, " +
                            "duration_seconds INTEGER, " +
                            "rest_seconds INTEGER, " +
                            "details TEXT, " +
                            "notes TEXT, " +
                            "FOREIGN KEY(exercise_id) REFERENCES exercises(id) " +
                            "ON UPDATE NO ACTION ON DELETE NO ACTION, " +
                            "FOREIGN KEY(workout_id) REFERENCES workouts(id) " +
                            "ON UPDATE NO ACTION ON DELETE NO ACTION, " +
                            "PRIMARY KEY(workout_id,number) " +
                    ");"
            );
            database.execSQL(
                    "INSERT INTO workout_steps(" +
                            "workout_id, " +
                            "number, " +
                            "exercise_id, " +
                            "reps, " +
                            "weight, " +
                            "rpe, " +
                            "duration_seconds, " +
                            "rest_seconds, " +
                            "details, " +
                            "notes" +
                    ") SELECT * FROM workout_steps_backup"
            );
            database.execSQL(
                    "DROP TABLE workout_steps_backup"
            );
        }
    };
}
