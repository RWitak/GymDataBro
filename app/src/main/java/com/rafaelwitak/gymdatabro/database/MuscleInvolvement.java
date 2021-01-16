/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private int exerciseID;

    @ColumnInfo(name = "muscle_group_name")
    @NonNull
    private String muscleGroupName = "";

    @ColumnInfo(name = "involvement_level")
    @Nullable
    private Float involvementLevel;

    @ColumnInfo
    @Nullable
    private String details;

    public int getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(int exerciseID) {
        this.exerciseID = exerciseID;
    }

    @NonNull
    public String getMuscleGroupName() {
        return muscleGroupName;
    }

    public void setMuscleGroupName(@NonNull String muscleGroupName) {
        this.muscleGroupName = muscleGroupName;
    }

    @Nullable
    public Float getInvolvementLevel() {
        return involvementLevel;
    }

    public void setInvolvementLevel(@Nullable Float involvementLevel) {
        this.involvementLevel = involvementLevel;
    }

    @Nullable
    public String getDetails() {
        return details;
    }

    public void setDetails(@Nullable String details) {
        this.details = details;
    }
}

