package com.rafaelwitak.gymdatabro.exerciseHandling;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.rafaelwitak.gymdatabro.database.Exercise;
import com.rafaelwitak.gymdatabro.database.ExerciseDAO;

public class ExerciseSaveHandler {

    private final Activity activity;

    private final Exercise exercise;
    private final ExerciseDAO exerciseDAO;
    private final boolean isExistingExercise;

    public ExerciseSaveHandler(@NonNull Activity activity,
                               @NonNull ExerciseDAO exerciseDAO,
                               @NonNull Exercise exercise,
                               boolean isExistingExercise) {
        this.activity = activity;
        this.exerciseDAO = exerciseDAO;
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
            exerciseDAO.updateExercise(exercise);
        } else {
            exerciseDAO.insertNewExercise(exercise);
        }
    }
}
