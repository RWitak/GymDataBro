package com.rafaelwitak.gymdatabro.workoutStepHandling.workoutStepHandling;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.database.WorkoutStepDAO;

public class WorkoutStepSaveHandler {

    private final Activity activity;

    private final WorkoutStep workoutStep;
    private final WorkoutStepDAO workoutStepDAO;
    private final boolean isExistingWorkoutStep;

    public WorkoutStepSaveHandler(@NonNull Activity activity,
                                  @NonNull WorkoutStepDAO workoutStepDAO,
                                  @NonNull WorkoutStep workoutStep,
                                  boolean isExistingWorkoutStep) {
        this.activity = activity;
        this.workoutStepDAO = workoutStepDAO;
        this.workoutStep = workoutStep;
        this.isExistingWorkoutStep = isExistingWorkoutStep;
    }

    public void save(int numberSets) {
        for (int i = 0; i < numberSets; i++, workoutStep.number++) {
            saveChangesToDb();
        }
    }

    private void saveChangesToDb() {
        trySavingWorkoutStepToDb();
    }

    public void trySavingWorkoutStepToDb() {
        try {
            saveWorkoutStepToDatabase();
        } catch (Exception e) {
            Toast.makeText(
                    activity,
                    "Can't write to database",
                    Toast.LENGTH_LONG)
                    .show();
            Log.e("GDB", "Can't write to database!\n" + e.toString());
        }
    }

    public void saveWorkoutStepToDatabase() {
        if (isExistingWorkoutStep) {
            workoutStepDAO.updateWorkoutStep(workoutStep);
        } else {
            workoutStepDAO.insertNewWorkoutStep(workoutStep);
        }
    }
}
