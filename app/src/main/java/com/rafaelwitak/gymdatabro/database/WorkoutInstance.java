package com.rafaelwitak.gymdatabro.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "workout_instances",
        foreignKeys = {
                @ForeignKey(
                        entity = Program.class,
                        parentColumns = "id",
                        childColumns = "program_id"),
                @ForeignKey(
                        entity = Workout.class,
                        parentColumns = "id",
                        childColumns = "workout_id")
        },
        indices = {
                @Index(value = {"workout_id", "workout_number"}, unique = true)
        }
)
public class WorkoutInstance {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo
    public String name;

    @ColumnInfo(name = "program_id")
    public int programId;

    @ColumnInfo(name = "workout_id")
    public int workoutId;

    @ColumnInfo(name = "workout_number")
    public int workoutNumber;
}
