package com.rafaelwitak.gymdatabro.database;

import org.junit.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class PerformanceSetDAOTest extends DaoTest {

    private PerformanceSetDAO dao;

    @Test
    public void constructorShouldWorkWithPartialInput() {
        PerformanceSet psEmpty = new PerformanceSet();
        PerformanceSet psNullEverywhere = new PerformanceSet(
                null,
                "TIMESTAMP",
                123,
                null,
                null,
                null,
                null,
                null,
                null,
                null
                );
        PerformanceSet psAllValues = new PerformanceSet(
                23,
                "2020-08-13",
                123,
                10,
                22.5f,
                12,
                30,
                8.5f,
                0,
                "Notes");

        createExerciseWithId123();

        assertThat(dao.insertSet(psAllValues)).isNotNull();
        assertThat(dao.insertSet(psNullEverywhere)).isNotNull();
        assertThat(dao.insertSet(psEmpty)).isNotNull();
    }

    private void createExerciseWithId123() {
        database.exerciseDAO().insertNewExercise(
                new Exercise(
                        123,
                        88.6f,
                        null,
                        null,
                        null,
                        null,
                        null
                        ));
    }

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
    public void nullIdGetsAutoFilled() {
        createExerciseWithId123();
        insertSetWithNullId();

        List<PerformanceSet> performanceSetList = dao.getAllSets();

        assertThat(performanceSetList.get(0).id).isNotNull();
    }

    @Test
    public void nullIdGetsAutoIncremented() {
        createExerciseWithId123();
        insertSetWithNullId();
        insertSetWithNullId();

        List<PerformanceSet> performanceSetList = dao.getAllSets();

        assertThat(performanceSetList.get(1).id).isNotEqualTo(performanceSetList.get(0).id);
    }

    private void insertSetWithNullId() {
        dao.insertSet(new PerformanceSet(
                null,
                null,
                123,
                12,
                30f,
                60,
                120,
                8f,
                0,
                null));
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