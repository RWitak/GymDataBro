package com.rafaelwitak.gymdatabro.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "programs")
public class Program {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo
    public String name;

    @ColumnInfo
    public String source;

    @ColumnInfo
    public String links;

    @ColumnInfo
    public String infos;

    @ColumnInfo
    public String notes;

    @ColumnInfo
    public int number_workouts;
}
