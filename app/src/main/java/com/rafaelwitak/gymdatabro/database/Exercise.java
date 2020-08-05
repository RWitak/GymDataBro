package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.File;
import java.util.List;

@Entity(tableName = "exercises")
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo
    public float pr;

    @ColumnInfo
    public List<String> cues;

    @ColumnInfo
    public List<String> links;

    @ColumnInfo(name = "img_a")
    public File imgA;

    @ColumnInfo(name = "img_b")
    public File imgB;

    @ColumnInfo
    public String equipment;
}
