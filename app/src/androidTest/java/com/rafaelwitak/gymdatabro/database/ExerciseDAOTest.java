/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.database;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class ExerciseDAOTest extends DaoTest {
    List<Exercise> exercises = new ArrayList<>();
    private ExerciseDAO dao;

    public void populateDb() {
        Exercise regularCase = new Exercise(
                2,
                "Name",
                7.125f,
                "Cues",
                "Links",
                "Equipment"
        );
        exercises.add(regularCase);

        Exercise idIsZero = new Exercise();
        idIsZero.setId(0);
        idIsZero.setPr(3f);
        exercises.add(idIsZero);

        Exercise idRepeats = new Exercise();
        idRepeats.setId(0); // already in database!
        exercises.add(idRepeats);

        Exercise idIsHigh = new Exercise();
        idIsHigh.setId(1000000000);
        exercises.add(idIsHigh);

        Exercise idIsNotIncremental = new Exercise();
        idIsNotIncremental.setId(69);
        exercises.add(idIsNotIncremental);

        Exercise idIsOneWhichShouldBeReservedByIdRepeatsByNow = new Exercise();
        idIsOneWhichShouldBeReservedByIdRepeatsByNow.setId(1);
        exercises.add(idIsOneWhichShouldBeReservedByIdRepeatsByNow);

        for (Exercise exercise : exercises) {
            database.exerciseDAO().insertNewExercise(exercise);
        }
    }

    @Test
    public void insertNewExercise() {
        Exercise newestExercise = new Exercise();
        newestExercise.setCues("Test for insertNewExercise");

        dao.insertNewExercise(newestExercise);

        List<Exercise> daoEntries = dao.getAllExercises();

        Exercise createdExercise = daoEntries.get(0);
        assertThat(createdExercise.getCues()).isEqualTo(newestExercise.getCues());
    }

    @Test
    public void getAllExercises() {
        populateDb();
        assertThat(dao.getAllExercises().size()).isEqualTo(exercises.size());
    }

    @Test
    public void getExerciseByID() {
        Exercise newestExercise = new Exercise();
        newestExercise.setId(247365);
        newestExercise.setCues("Test for getExerciseById");
        dao.insertNewExercise(newestExercise);

        Exercise insertedExercise = dao.getExerciseByID(newestExercise.getId());

        assertThat(insertedExercise).isNotNull();
        assertThat(insertedExercise.getCues()).isEqualTo(newestExercise.getCues());
    }

    @Test
    public void updateExercise() {
        populateDb();
        Exercise testExercise = dao.getExerciseByID(2);

        String testString = "Exercise update successful";
        testExercise.setCues(testString);

        dao.updateExercise(testExercise);

        Exercise updatedTestExercise = dao.getExerciseByID(2);

        assertThat(updatedTestExercise.getCues()).isEqualTo(testString);
    }

    @Override
    protected void setDao() {
        this.dao = database.exerciseDAO();
    }
}