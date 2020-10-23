package com.rafaelwitak.gymdatabro.database;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SmallTest
public abstract class DaoTest {

    protected GymBroDatabase database;
    protected ExerciseDAO dao;

    @Before
    public void setup() {
        database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                GymBroDatabase.class
        ).allowMainThreadQueries()
        .build();

        this.dao = database.exerciseDAO();
    }

    @After
    public void teardown() {
        database.close();
    }
}