package com.rafaelwitak.gymdatabro.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
        tableName = "workouts_in_programs",
        foreignKeys = {
                @ForeignKey(
                        entity = Workout.class,
                        parentColumns = "id",
                        childColumns = "workout_id"),
                @ForeignKey(
                        entity = Program.class,
                        parentColumns = "id",
                        childColumns = "program_id")
        },
        primaryKeys = {"program_id", "workout_number"}
)
public class WorkoutInProgram {
    @ColumnInfo(name = "workout_id")
    public int workoutId;

    @ColumnInfo(name = "program_id")
    public int programId;

    @ColumnInfo(name = "workout_number")
    public int workoutNumber;
}
