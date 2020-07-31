package com.rafaelwitak.gymdatabro.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "muscle_groups")
public class MuscleGroup {
    @PrimaryKey
    public String name;
}
