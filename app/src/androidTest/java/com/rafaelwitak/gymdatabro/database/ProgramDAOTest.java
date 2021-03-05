/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.database;

import android.database.sqlite.SQLiteException;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class ProgramDAOTest extends DaoTest {
    private static final int TEST_SIZE = 69;
    private ProgramDAO dao;

    @Test
    public void getAllPrograms() {
        for (int i = 0; i < TEST_SIZE; i++) {
            dao.insertProgram(new Program());
        }

        assertThat(dao.getAllPrograms().size()).isEqualTo(TEST_SIZE);
    }

    @Test
    public void getProgramByID() {
        for (int i = 0; i < TEST_SIZE; i++) {
            Program p = new Program();
            p.setId(i + 1);
            dao.insertProgram(p);
        }

        assertThat(dao.getProgramByID(TEST_SIZE).getId()).isEqualTo(TEST_SIZE);
    }

    @Test
    public void insertEmptyProgramShouldNotThrowException() {
        SQLiteException expectedException = null;

        try {
            dao.insertProgram(new Program());
        } catch (SQLiteException e) {
            expectedException = e;
        }

        assertThat(expectedException).isNull();
    }

    @Test
    public void insertShouldReplaceOnConflict() {
        Program p1 = new Program();
        p1.setId(1);
        p1.setNumber_workouts(7);

        Program pAlso1 = new Program();
        pAlso1.setId(1);
        pAlso1.setNumber_workouts(p1.getNumber_workouts() - 1);

        dao.insertProgram(p1);
        dao.insertProgram(pAlso1);

        assertThat(dao.getProgramByID(1).getNumber_workouts())
                .isEqualTo(pAlso1.getNumber_workouts());
    }
    @Test
    public void updateProgram() {
        Program p = new Program();
        p.setId(33);
        dao.insertProgram(p);

        Program pFromDb = dao.getProgramByID(33);
        pFromDb.setNumber_workouts(22);
        dao.updateProgram(pFromDb);

        assertThat(dao.getProgramByID(33).getNumber_workouts())
                .isNotEqualTo(p.getNumber_workouts());
    }

    @Override
    protected void setDao() {
        this.dao = database.programDAO();
    }
}