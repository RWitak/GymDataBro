package com.rafaelwitak.gymdatabro.viewRows;

import android.view.View;
import android.widget.TextView;

import com.rafaelwitak.gymdatabro.activities.MainActivity;
import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

public class ExerciseNameRow {
    private WorkoutStep currentWorkoutStep;
    private ActivityWorkoutStepBinding binding;
    private GymBroDatabase database = MainActivity.database;

    private TextView nameView;
    private TextView progressView;

    String progress;

    public ExerciseNameRow(
            ActivityWorkoutStepBinding binding,
            WorkoutStep workoutStep) {

        this.currentWorkoutStep = workoutStep;
        this.binding = binding;

        this.nameView = getStepExerciseNameTitle();
        this.progressView = getStepExerciseNameProgressRatio();
        this.progress = getProgress();
        }

    public void setup() {
        setupNameView();
        setupProgressView();
    }

    private void setupNameView() {
        this.nameView.setText(getCurrentExerciseName());
    }

    private void setupProgressView() {
        if (progressShouldBeVisible()) {
            this.progressView.setText(this.progress);
        }
        else {
            this.progressView.setVisibility(View.GONE);
        }
    }

    private String getProgress() {
        int currentNumber = this.currentWorkoutStep.number;
        int totalNumber = database.workoutDAO().getOccurrencesOfExerciseInWorkout(
                currentWorkoutStep.exerciseID, currentWorkoutStep.workoutID);

        if (totalNumber <= 1) {
            return null;
        }

        return currentNumber + "/" + totalNumber;
    }

    private boolean progressShouldBeVisible() {
        return this.progress != null;
    }


    private TextView getStepExerciseNameTitle() {
        return binding.stepExerciseNameTitle;
    }

    private TextView getStepExerciseNameProgressRatio() {
        return binding.stepExerciseNameProgressRatio;
    }

    private String getCurrentExerciseName() {
        String name = database.exerciseNameDAO().getMainNameByID(currentWorkoutStep.exerciseID);

        if (name != null) {
            return name;
        }

        return "Unnamed Exercise";
    }
}

