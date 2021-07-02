/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.NonNull;
import androidx.room.*;

@Entity(
        tableName = "workout_instances",
        foreignKeys = {
                // TODO: 02.07.2021 Consider changing FKs to CASCADE onDelete/onUpdate
                //  (will probably change DB scheme!)
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
                @Index(
                        value = "program_id",
                        name = "index_workout_instances_program_id"),
                @Index(
                        value = {"workout_id", "workout_number"},
                        unique = true)
        }
)
public class WorkoutInstance implements Cloneable {
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

    @NonNull
    @Override
    public WorkoutInstance clone() throws CloneNotSupportedException {
        return (WorkoutInstance) super.clone();
    }

    public WorkoutInstance duplicateWithIdZero() {
        WorkoutInstance duplicate;
        try {
            duplicate = this.clone();
            duplicate.setId(0);
            return duplicate;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
