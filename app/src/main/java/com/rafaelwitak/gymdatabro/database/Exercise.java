package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercises")
public class Exercise {

    public Exercise() {}

    @Ignore
    public Exercise(
            int id,
            @Nullable Float pr,
            @Nullable String cues,
            @Nullable String links,
            @Nullable String equipment) {
        this.id = id;
        this.pr = pr;
        this.cues = cues;
        this.links = links;
        this.equipment = equipment;
    }

    @Ignore
    public Exercise(
            @Nullable Float pr,
            @Nullable String cues,
            @Nullable String links,
            @Nullable String equipment) {
        this.pr = pr;
        this.cues = cues;
        this.links = links;
        this.equipment = equipment;
    }

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

    @ColumnInfo
    @Nullable
    public String equipment;
}
