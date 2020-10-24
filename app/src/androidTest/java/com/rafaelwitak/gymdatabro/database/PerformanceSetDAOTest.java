package com.rafaelwitak.gymdatabro.database;

import org.junit.Test;

public class PerformanceSetDAOTest extends DaoTest {

    private PerformanceSetDAO dao;

    @Test
    public void getAllSets() {
        //TODO: implement.
    }

    @Test
    public void getSetByRowId() {
        //TODO: implement.
    }

    @Test
    public void getAllByExerciseID() {
        //TODO: implement.
    }

    @Test
    public void getAllBetweenTimestamps() {
        //TODO: implement.
    }

    @Test
    public void insertSet() {
        //TODO: implement.
    }

    @Test
    public void updateSet() {
        //TODO: implement.
    }

    @Override
    protected void setDao() {
        this.dao = database.performanceSetDAO();
    }
}