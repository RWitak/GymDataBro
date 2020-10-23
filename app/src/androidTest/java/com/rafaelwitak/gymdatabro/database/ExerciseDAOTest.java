package com.rafaelwitak.gymdatabro.database;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class ExerciseDAOTest extends DaoTest {
    List<Exercise> exercises = new ArrayList<>();

    public void populateDb() {
        Exercise regularCase = new Exercise(
                2,
                7.125f,
                "Cues",
                "Links",
                "imgA",
                "imgB",
                "Equipment"
        );
        exercises.add(regularCase);

        Exercise idIsZero = new Exercise();
        idIsZero.id = 0;
        idIsZero.pr = 3f;
        exercises.add(idIsZero);

        Exercise idRepeats = new Exercise();
        idRepeats.id = 0; // already in database!
        exercises.add(idRepeats);

        Exercise idIsHigh = new Exercise();
        idIsHigh.id = 1000000000;
        exercises.add(idIsHigh);

        Exercise idIsNotIncremental = new Exercise();
        idIsNotIncremental.id = 69;
        exercises.add(idIsNotIncremental);

        Exercise idIsOneWhichShouldBeReservedByIdRepeatsByNow = new Exercise();
        idIsOneWhichShouldBeReservedByIdRepeatsByNow.id = 1;
        exercises.add(idIsOneWhichShouldBeReservedByIdRepeatsByNow);

        for (Exercise exercise : exercises) {
            database.exerciseDAO().insertNewExercise(exercise);
        }
    }

    @Test
    public void insertNewExercise() {
        Exercise newestExercise = new Exercise();
        newestExercise.cues = "Test for insertNewExercise";

        dao.insertNewExercise(newestExercise);

        List<Exercise> daoEntries = dao.getAllExercises();

        Exercise createdExercise = daoEntries.get(0);
        assertThat(createdExercise.cues).isEqualTo(newestExercise.cues);
    }

    @Test
    public void getAllExercises() {
        populateDb();
        assertThat(dao.getAllExercises().size()).isEqualTo(exercises.size());
    }

    @Test
    public void getExerciseByID() {
        //FIXME: Currently deals with LiveData!
        Exercise newestExercise = new Exercise();
        newestExercise.id = 247365;
        newestExercise.cues = "Test for getExerciseById";
        dao.insertNewExercise(newestExercise);

        Exercise insertedExercise = dao.getExerciseByID(newestExercise.id).getValue();

        assertThat(insertedExercise).isNotNull();
        assertThat(insertedExercise.cues).isEqualTo(newestExercise.cues);
    }

    @Test
    public void getExerciseListByEquipment() {
        //TODO
    }

    @Test
    public void updateExercise() {
        //TODO
    }
}