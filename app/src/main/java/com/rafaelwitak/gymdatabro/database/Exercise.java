package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercises")
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo
    public float pr;

    @ColumnInfo
    public String cues;

    @ColumnInfo
    public String links;

    @ColumnInfo(name = "img_a")
    public String imgA_URI;

    @ColumnInfo(name = "img_b")
    public String imgB_URI;

    @ColumnInfo
    public String equipment;
}
