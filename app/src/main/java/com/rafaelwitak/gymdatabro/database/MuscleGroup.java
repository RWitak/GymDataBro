package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "muscle_groups")
public class MuscleGroup {
    @PrimaryKey
    @NonNull
    public String name = "";
}
