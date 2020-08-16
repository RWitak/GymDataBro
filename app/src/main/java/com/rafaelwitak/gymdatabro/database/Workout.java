package com.rafaelwitak.gymdatabro.database;

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
    public int programID;

    @ColumnInfo
    public String name;

    @ColumnInfo
    public String details;

    @ColumnInfo
    public String notes;
}
