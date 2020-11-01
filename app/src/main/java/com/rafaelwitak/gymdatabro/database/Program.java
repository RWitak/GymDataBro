package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "programs")
public class Program {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(defaultValue = "Freestyle Program")
    @NonNull
    public String name = "Freestyle Program";

    @ColumnInfo
    @Nullable
    public String source;

    @ColumnInfo
    @Nullable
    public String links;

    @ColumnInfo
    @Nullable
    public String info;

    @ColumnInfo
    @Nullable
    public String notes;

    @ColumnInfo(defaultValue = "1")
    @NonNull
    public Integer number_workouts = 1;
}
