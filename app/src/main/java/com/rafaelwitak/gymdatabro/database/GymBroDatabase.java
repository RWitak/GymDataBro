package com.rafaelwitak.gymdatabro.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

// FIXME: Many instances without error handling!
@androidx.room.Database(
        entities={
                Exercise.class,
                MuscleGroup.class,
                MuscleInvolvement.class,
                Program.class,
                PerformanceSet.class,
                Workout.class,
                WorkoutStep.class
        },
        version = 14
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
                            .addMigrations(
                                    MIGRATION_9_10,
                                    MIGRATION_10_11,
                                    MIGRATION_11_12,
                                    MIGRATION_12_13,
                                    MIGRATION_13_14)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    static final Migration MIGRATION_13_14 = new Migration(13, 14) {
        // PerformanceSets can now reference their WorkoutStep's ID (if any).
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TABLE sets_backup ( " +
                            "id INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT, " +
                            "timestamp INTEGER, " +
                            "exercise_id INTEGER NOT NULL, " +
                            "reps INTEGER, " +
                            "weight REAL, " +
                            "seconds_performed INTEGER, " +
                            "seconds_rested INTEGER, " +
                            "rpe REAL, " +
                            "pain_level INTEGER NOT NULL, " +
                            "notes TEXT, " +
                            "FOREIGN KEY(exercise_id) REFERENCES exercises(id) " +
                            "ON UPDATE NO ACTION ON DELETE NO ACTION " +
                            ");"
            );
            database.execSQL(
                    "INSERT INTO sets_backup SELECT * FROM sets;"
            );
            database.execSQL(
                    "DROP TABLE sets;"
            );
            database.execSQL(
                    "CREATE TABLE sets ( " +
                            "id INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT, " +
                            "workout_step_id INTEGER, " +
                            "timestamp INTEGER, " +
                            "exercise_id INTEGER NOT NULL, " +
                            "reps INTEGER, " +
                            "weight REAL, " +
                            "seconds_performed INTEGER, " +
                            "seconds_rested INTEGER, " +
                            "rpe REAL, " +
                            "pain_level INTEGER NOT NULL, " +
                            "notes TEXT, " +
                            "FOREIGN KEY(exercise_id) REFERENCES exercises(id) " +
                            "ON UPDATE NO ACTION ON DELETE NO ACTION, " +
                            "FOREIGN KEY(workout_step_id) REFERENCES workout_steps(id) " +
                            "ON UPDATE NO ACTION ON DELETE NO ACTION " +
                            ");"
            );
            database.execSQL(
                    "DROP TABLE sets_backup;"
            );
        }
    };

    static final Migration MIGRATION_12_13 = new Migration(12, 13) {
        // 'workout_steps' get an auto-incrementing int 'id' as primary key,
        // preventing persistence conflicts (no reassignment can overwrite uniqueness).
        // 'workout_id' and 'number' still function as unique identifiers,
        // but aren't persistent; they get uniquely indexed together.
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TABLE workout_steps_backup(" +
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
                    "INSERT INTO workout_steps_backup SELECT * FROM workout_steps;"
            );
            database.execSQL(
                    "DROP TABLE workout_steps;"
            );
            database.execSQL(
                    "CREATE TABLE workout_steps(" +
                            "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
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
                            "ON UPDATE NO ACTION ON DELETE NO ACTION" +
                            ");"
            );
            database.execSQL(
                    "INSERT INTO workout_steps (" +
                            "workout_id," +
                            "number," +
                            "name," +
                            "exercise_id," +
                            "reps," +
                            "weight," +
                            "rpe," +
                            "duration_seconds," +
                            "rest_seconds," +
                            "details," +
                            "notes) " +
                    "SELECT " +
                            "workout_id," +
                            "number," +
                            "name," +
                            "exercise_id," +
                            "reps," +
                            "weight," +
                            "rpe," +
                            "duration_seconds," +
                            "rest_seconds," +
                            "details," +
                            "notes " +
                    "FROM workout_steps_backup;"
            );
            database.execSQL(
                    "CREATE UNIQUE INDEX index_workout_steps_workout_id_number" +
                            " ON workout_steps (workout_id, number);"
            );
            database.execSQL(
                    "DROP TABLE workout_steps_backup;"
            );
        }
    };

    static final Migration MIGRATION_11_12 = new Migration(11, 12) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL(
                    "DROP TABLE exercise_names"
            );
        }
    };

    static final Migration MIGRATION_10_11 = new Migration(10, 11) {
        // Tables 'exercises' and 'workout_steps' both get a 'name' column
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            // Recreate 'exercises' table with added 'name' column
            database.execSQL(
                    "CREATE TABLE exercises_backup(" +
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
                    "CREATE TABLE workout_steps_backup(" +
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

    static final Migration MIGRATION_9_10 = new Migration(9, 10) {
        // Deleting columns img_a & img_b from exercises directly is not possible in SQLite
        // Therefore, we back the table up, drop and recreate it.
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TABLE exercises_backup(" +
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
}
