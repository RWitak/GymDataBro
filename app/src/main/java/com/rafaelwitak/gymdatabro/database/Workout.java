/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

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
public class Workout {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "program_id")
    @Nullable
    public Integer programID;

    @ColumnInfo
    @NonNull
    public String name = "Unnamed Workout";

    @ColumnInfo
    @Nullable
    public String details;

    @ColumnInfo
    @Nullable
    public String notes;
}
