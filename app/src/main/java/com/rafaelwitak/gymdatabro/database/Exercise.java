package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "exercises",
        indices = {@Index(value = "name", unique = true)})
public class Exercise {

    public Exercise(
            int id,
            @Nullable String name,
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

    public Exercise(
            @Nullable String name,
            @Nullable Float pr,
            @Nullable String cues,
            @Nullable String links,
            @Nullable String equipment) {
        this.pr = pr;
        this.cues = cues;
        this.links = links;
        this.equipment = equipment;
    }

    public Exercise() {}

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo
    @Nullable
    public String name;

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
