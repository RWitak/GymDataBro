/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.database;

import android.database.sqlite.SQLiteConstraintException;

import org.junit.Test;

import java.util.Date;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class PerformanceSetDAOTest extends DaoTest {
    private static final int TEST_EXERCISE_ID = 123;

    private static final int EMPTY_PS_EXERCISE_ID = 56789;
    final int TEST_SIZE = 42;
    private PerformanceSetDAO dao;

    @Override
    protected void setDao() {
        this.dao = database.performanceSetDAO();
    }

    @Override
    public void setup() {
        super.setup();
        createEmptyExerciseWithId(TEST_EXERCISE_ID);
    }


    @Test
    public void insertSetShouldWorkWithFullInput() {
        PerformanceSet psAllValues = new PerformanceSet(
                666,
                2,
                3,
                new Date(),
                10,
                22.5f,
                12,
                30,
                8.5f,
                0,
                "Notes");

        assertThat(dao.insertSet(psAllValues)).isNotNull();
    }

    @Test
    public void insertSetShouldNotWorkWithBarePerformanceSet() {
        PerformanceSet psEmpty = new PerformanceSet();

        SQLiteConstraintException expectedException = null;

        try {
            dao.insertSet(psEmpty);
        }
        catch (SQLiteConstraintException thrownException) {
            expectedException = thrownException;
        }
        finally {
            assertThat(expectedException).isNotNull();
        }
    }

    @Test
    public void nullIdShouldGetAutoFilled() {
        insertSetWithNullId();

        List<PerformanceSet> performanceSetList = dao.getAllSets();

        assertThat(performanceSetList.get(0).getId()).isNotNull();
    }

    @Test
    public void nullIdShouldGetAutoIncremented() {
        insertSetWithNullId();
        insertSetWithNullId();

        List<PerformanceSet> performanceSetList = dao.getAllSets();

        assertThat(performanceSetList.get(1).getId())
                .isNotEqualTo(performanceSetList.get(0).getId());
    }

    @Test
    public void nullTimestampShouldBeSetToCurrentDatetime() {
        Date timestampBefore = new Date();

        long rowId = dao.insertSet(new PerformanceSet(
                15,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                0,
                null
                )
        );

        PerformanceSet savedSet = dao.getSetByRowId(rowId);

        assertThat(savedSet.getTimestamp()).isAtLeast(timestampBefore);
        assertThat(savedSet.getTimestamp()).isAtMost(new Date());
    }

    @Test
    public void getAllSets() {
        createEmptyExerciseWithId(EMPTY_PS_EXERCISE_ID);

        insertNumberOfEmptySets(TEST_SIZE);

        assertThat(dao.getAllSets().size()).isEqualTo(TEST_SIZE);
    }

    @Test
    public void getSetByRowId() {
        createEmptyExerciseWithId(EMPTY_PS_EXERCISE_ID);

        insertNumberOfEmptySets(TEST_SIZE);

        PerformanceSet testSet = new PerformanceSet();
        testSet.setSecondsPerformed(365);

        long rowId = dao.insertSet(testSet);

        assertThat(dao.getSetByRowId(rowId).getSecondsPerformed())
                .isEqualTo(testSet.getSecondsPerformed());
    }

    @Test
    public void getAllBetweenTimestamps() {
        Date d1 = new Date();
        d1.setTime(d1.getTime() - 10000);

        createEmptyExerciseWithId(EMPTY_PS_EXERCISE_ID);
        insertNumberOfEmptySets(TEST_SIZE);

        Date d2 = new Date();

        PerformanceSet beforeD1 = new PerformanceSet();
        beforeD1.getTimestamp().setTime(beforeD1.getTimestamp().getTime() - 50000);
        dao.insertSet(beforeD1);

        PerformanceSet afterD2 = new PerformanceSet();
        afterD2.getTimestamp().setTime(afterD2.getTimestamp().getTime() + 50000);
        dao.insertSet(afterD2);

        assertThat(dao.getAllBetweenTimestamps(d1, d2).size()).isEqualTo(TEST_SIZE);
    }

    @Test
    public void updateSet() {
        createEmptyExerciseWithId(EMPTY_PS_EXERCISE_ID);

        insertNumberOfEmptySets(TEST_SIZE);
        long rowId = dao.insertSet(new PerformanceSet());
        insertNumberOfEmptySets(TEST_SIZE);

        PerformanceSet testSet = dao.getSetByRowId(rowId);

        testSet.setPainLevel(7);

        dao.updateSet(testSet);

        assertThat(dao.getSetByRowId(rowId)).isNotEqualTo(testSet);
    }

    private void createEmptyExerciseWithId(int exerciseId) {
        database.exerciseDAO().insertNewExercise(
                new Exercise(
                        exerciseId,
                        null,
                        null,
                        null,
                        null,
                        null
                ));
    }

    private void insertSetWithNullId() {
        dao.insertSet(new PerformanceSet(
                null,
                null,
                null,
                null,
                12,
                30f,
                60,
                120,
                8f,
                0,
                null));
    }

    private void insertNumberOfEmptySets(int n) {
        for (int i = 0; i < n; i++) {
            dao.insertSet(new PerformanceSet());
        }
    }
}