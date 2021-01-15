/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "exercises",
        indices = {@Index(value = "name", unique = true)})
public class Exercise {

    public Exercise() {}

    @Ignore
    public Exercise(
            int id,
            @Nullable String name,
            @Nullable Float pr,
            @Nullable String cues,
            @Nullable String links,
            @Nullable String equipment) {
        this.setId(id);
        this.setPr(pr);
        this.setCues(cues);
        this.setLinks(links);
        this.setEquipment(equipment);
    }

    @Ignore
    public Exercise(
            @Nullable String name,
            @Nullable Float pr,
            @Nullable String cues,
            @Nullable String links,
            @Nullable String equipment) {
        this.setPr(pr);
        this.setCues(cues);
        this.setLinks(links);
        this.setEquipment(equipment);
    }

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    @Nullable
    private String name;

    @ColumnInfo
    @Nullable
    private Float pr;

    @ColumnInfo
    @Nullable
    private String cues;

    @ColumnInfo
    @Nullable
    private String links;

    @ColumnInfo
    @Nullable
    private String equipment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public Float getPr() {
        return pr;
    }

    public void setPr(@Nullable Float pr) {
        this.pr = pr;
    }

    @Nullable
    public String getCues() {
        return cues;
    }

    public void setCues(@Nullable String cues) {
        this.cues = cues;
    }

    @Nullable
    public String getLinks() {
        return links;
    }

    public void setLinks(@Nullable String links) {
        this.links = links;
    }

    @Nullable
    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(@Nullable String equipment) {
        this.equipment = equipment;
    }
}
