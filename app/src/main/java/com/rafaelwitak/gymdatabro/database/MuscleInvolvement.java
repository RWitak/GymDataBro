package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "muscle_involvements",
        foreignKeys = {
            @ForeignKey(
                entity = Exercise.class,
                parentColumns = "id",
                childColumns = "exercise_id"
            ),
            @ForeignKey(
                entity = MuscleGroup.class,
                parentColumns = "name",
                childColumns = "muscle_group_name"
            )
        },
        primaryKeys = {"exercise_id", "muscle_group_name"},
        indices = {
            @Index(
                    value = "exercise_id"
            ),
            @Index(
                   value = "muscle_group_name"
            )
        }
        )
public class MuscleInvolvement {
    @ColumnInfo(name = "exercise_id")
    public int exerciseID;

    @ColumnInfo(name = "muscle_group_name")
    @NonNull
    public String muscleGroupName = "";

    @ColumnInfo(name = "involvement_level")
    public float involvementLevel;

    @ColumnInfo
    public String details;
}

