/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "programs")
public class Program {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(defaultValue = "Freestyle Program")
    @NonNull
    private String name = "Freestyle Program";

    @ColumnInfo
    @Nullable
    private String source;

    @ColumnInfo
    @Nullable
    private String links;

    @ColumnInfo
    @Nullable
    private String info;

    @ColumnInfo
    @Nullable
    private String notes;


    @Ignore
    private Integer number_workouts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @Nullable
    public String getSource() {
        return source;
    }

    public void setSource(@Nullable String source) {
        this.source = source;
    }

    @Nullable
    public String getLinks() {
        return links;
    }

    public void setLinks(@Nullable String links) {
        this.links = links;
    }

    @Nullable
    public String getInfo() {
        return info;
    }

    public void setInfo(@Nullable String info) {
        this.info = info;
    }

    @Nullable
    public String getNotes() {
        return notes;
    }

    public void setNotes(@Nullable String notes) {
        this.notes = notes;
    }

    public Integer getNumber_workouts() {
        return number_workouts;
    }

    public void setNumber_workouts(Integer number_workouts) {
        this.number_workouts = number_workouts;
    }
}
