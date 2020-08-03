package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "exercise_names",
        foreignKeys = @ForeignKey(
                entity = Exercise.class,
                parentColumns = "id",
                childColumns = "exercise_id"
        )
)
public class ExerciseName {
    @PrimaryKey
    public String name;

    @ColumnInfo(name = "is_main_name")
    public Boolean isMainName;

    @ColumnInfo(name = "exercise_id")
    public int exerciseID;
}
