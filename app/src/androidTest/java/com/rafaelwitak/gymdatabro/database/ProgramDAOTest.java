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
            p.id = i + 1;
            dao.insertProgram(p);
        }

        assertThat(dao.getProgramByID(TEST_SIZE).id).isEqualTo(TEST_SIZE);
    }

    @Test
    public void insertProgram() {
        SQLiteException expectedException = null;

        try {
            dao.insertProgram(new Program());
        } catch (SQLiteException e) {
            expectedException = e;
        }

        assertThat(expectedException).isNull();
    }

    @Test
    public void updateProgram() {
    }

    @Override
    protected void setDao() {
        this.dao = database.programDAO();
    }
}