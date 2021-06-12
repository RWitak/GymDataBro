/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.*;

@Entity(
        tableName = "workouts",
        foreignKeys = @ForeignKey(
                entity = Program.class,
                parentColumns = "id",
                childColumns = "program_id"),
        indices = {
                @Index(value = "program_id")
        }
)
public class Workout implements Cloneable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "program_id")
    @Nullable
    private Integer programID;

    @ColumnInfo
    @NonNull
    private String name = "Unnamed Workout";

    @ColumnInfo
    @Nullable
    private String details;

    @ColumnInfo
    @Nullable
    private String notes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Nullable
    public Integer getProgramID() {
        return programID;
    }

    public void setProgramID(@Nullable Integer programID) {
        this.programID = programID;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @Nullable
    public String getDetails() {
        return details;
    }

    public void setDetails(@Nullable String details) {
        this.details = details;
    }

    @Nullable
    public String getNotes() {
        return notes;
    }

    public void setNotes(@Nullable String notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Workout duplicateWithIdZero() throws CloneNotSupportedException {
        Workout duplicate = (Workout) this.clone();
        duplicate.setId(0); // auto-incremented id sets next value
        return duplicate;
    }
}
