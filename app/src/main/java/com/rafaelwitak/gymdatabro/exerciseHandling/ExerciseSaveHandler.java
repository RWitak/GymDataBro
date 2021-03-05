/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.exerciseHandling;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.rafaelwitak.gymdatabro.database.Exercise;
import com.rafaelwitak.gymdatabro.database.MasterDao;

public class ExerciseSaveHandler {

    private final Activity activity;

    private final Exercise exercise;
    private final MasterDao dao;
    private final boolean isExistingExercise;

    public ExerciseSaveHandler(@NonNull Activity activity,
                               @NonNull MasterDao dao,
                               @NonNull Exercise exercise,
                               boolean isExistingExercise) {
        this.activity = activity;
        this.dao = dao;
        this.exercise = exercise;
        this.isExistingExercise = isExistingExercise;
    }


    public void saveAndFinish() {
        saveChanges(this.exercise);
        this.activity.finish();
    }

    public void saveChanges(Exercise exercise) {
        trySavingExerciseToDb(exercise);
    }

    public void trySavingExerciseToDb(Exercise exercise) {
        try {
            saveExerciseToDatabase(exercise);
        } catch (Exception e) {
            Toast.makeText(
                    this.activity,
                    "Can't write to database",
                    Toast.LENGTH_LONG)
                    .show();
            Log.e("GDB", "Can't write to database!\n" + e.toString());
        }
    }

    public void saveExerciseToDatabase(Exercise exercise) {
        if (isExistingExercise) {
            dao.updateExercise(exercise);
        } else {
            dao.insertNewExercise(exercise);
        }
    }
}
