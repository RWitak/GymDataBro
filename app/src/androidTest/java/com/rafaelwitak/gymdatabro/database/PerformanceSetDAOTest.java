package com.rafaelwitak.gymdatabro.database;

import android.database.sqlite.SQLiteConstraintException;

import org.junit.Test;

import java.util.Date;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class PerformanceSetDAOTest extends DaoTest {

    private PerformanceSetDAO dao;
    private static final int TEST_EXERCISE_ID = 123;

    @Override
    public void setup() {
        super.setup();
        createEmptyExerciseWithTestExerciseId();
    }

    @Test
    public void constructorShouldWorkWithFullInput() {
        PerformanceSet psAllValues = new PerformanceSet(
                23,
                new Date(),
                TEST_EXERCISE_ID,
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
    public void constructorShouldNotWorkWithBarePerformanceSet() {
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

    private void createEmptyExerciseWithTestExerciseId() {
        database.exerciseDAO().insertNewExercise(
                new Exercise(
                        TEST_EXERCISE_ID,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                        ));
    }

    @Test
    public void nullTimestampShouldBeSetToCurrentDatetime() {
        Date timestampBefore = new Date();

        long rowId = dao.insertSet(new PerformanceSet(
                15,
                null,
                TEST_EXERCISE_ID,
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

        assertThat(savedSet.timestamp).isAtLeast(timestampBefore);
        assertThat(savedSet.timestamp).isAtMost(new Date());
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
        insertSetWithNullId();

        List<PerformanceSet> performanceSetList = dao.getAllSets();

        assertThat(performanceSetList.get(0).id).isNotNull();
    }

    @Test
    public void nullIdGetsAutoIncremented() {
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