/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "workout_instances",
        foreignKeys = {
                @ForeignKey(
                        entity = Program.class,
                        parentColumns = "id",
                        childColumns = "program_id",
                        onDelete = CASCADE),
                @ForeignKey(
                        entity = Workout.class,
                        parentColumns = "id",
                        childColumns = "workout_id",
                        onDelete = CASCADE)
        },
        indices = {
                @Index(
                        value = "program_id",
                        name = "index_workout_instances_program_id"),
                @Index(
                        value = {"workout_id", "workout_number"},
                        unique = true)
        }
)
public class WorkoutInstance {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private String name;

    @ColumnInfo(name = "program_id")
    private int programId;

    @ColumnInfo(name = "workout_id")
    private int workoutId;

    @ColumnInfo(name = "workout_number")
    private int workoutNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public int getWorkoutNumber() {
        return workoutNumber;
    }

    public void setWorkoutNumber(int workoutNumber) {
        this.workoutNumber = workoutNumber;
    }
}
