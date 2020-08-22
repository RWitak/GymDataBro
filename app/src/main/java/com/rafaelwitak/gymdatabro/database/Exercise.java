package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercises")
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo
    @Nullable
    public Float pr;

    @ColumnInfo
    @Nullable
    public String cues;

    @ColumnInfo
    @Nullable
    public String links;

    @ColumnInfo(name = "img_a")
    @Nullable
    public String imgA_URI;

    @ColumnInfo(name = "img_b")
    @Nullable
    public String imgB_URI;

    @ColumnInfo
    @Nullable
    public String equipment;
}
