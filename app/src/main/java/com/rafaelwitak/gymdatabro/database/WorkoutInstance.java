/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.NonNull;
import androidx.room.*;

import java.util.Objects;

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
public class WorkoutInstance implements Cloneable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkoutInstance instance = (WorkoutInstance) o;
        return id == instance.id
                && programId == instance.programId
                && workoutId == instance.workoutId
                && workoutNumber == instance.workoutNumber
                && ((name == null && instance.name == null)
                    || Objects.requireNonNull(name).equals(instance.name));
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id, name, programId,
                workoutId, workoutNumber, active);
    }

    @ColumnInfo
    private String name;

    @ColumnInfo(name = "program_id")
    private int programId;

    @ColumnInfo(name = "workout_id")
    private int workoutId;

    @ColumnInfo(name = "workout_number")
    private int workoutNumber;

    @ColumnInfo(name = "active", defaultValue = "1")
    private boolean active;

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

    public WorkoutInstance(int id,
                           String name,
                           int programId,
                           int workoutId,
                           int workoutNumber,
                           boolean active) {
        this.id = id;
        this.name = name;
        this.programId = programId;
        this.workoutId = workoutId;
        this.workoutNumber = workoutNumber;
        this.setActive(active);
    }

    @NonNull
    @Override
    public WorkoutInstance clone() {
        try {
            return (WorkoutInstance) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public WorkoutInstance duplicateWithIdZero() {
        WorkoutInstance duplicate;
        duplicate = this.clone();
        duplicate.setId(0);
    return duplicate;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active != null ? active : true;
    }
}
