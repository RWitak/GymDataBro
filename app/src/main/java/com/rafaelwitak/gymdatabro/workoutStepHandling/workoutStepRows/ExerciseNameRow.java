package com.rafaelwitak.gymdatabro.workoutStepHandling.workoutStepRows;

import android.view.View;
import android.widget.TextView;

import com.rafaelwitak.gymdatabro.activities.MainActivity;
import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;
import com.rafaelwitak.gymdatabro.workoutStepHandling.WorkoutStepRow;

public class ExerciseNameRow extends WorkoutStepRow {
    private GymBroDatabase database = MainActivity.database;

    private TextView nameView;
    private TextView progressView;

    private String progress;


    public ExerciseNameRow(ActivityWorkoutStepBinding binding, WorkoutStep workoutStep) {
        super(binding, workoutStep);

        this.nameView = getStepExerciseNameTitle();
        this.progressView = getStepExerciseNameProgressRatio();
        this.progress = getProgress();
    }


    @Override
    protected View getRowViewFromBinding() {
        return binding.stepExerciseNameRow;
    }

    @Override
    protected boolean shouldBeVisible() {
        // currently no adverse conditions
        return true;
    }

    @Override
    public void setup() {
        setupNameView();
        setupProgressView();
    }

    private TextView getStepExerciseNameTitle() {
        return binding.stepExerciseNameTitle;
    }

    private TextView getStepExerciseNameProgressRatio() {
        return binding.stepExerciseNameProgressRatio;
    }


    private void setupNameView() {
        this.nameView.setText(getCurrentExerciseName());
    }

    private String getCurrentExerciseName() {
        String name = database.exerciseNameDAO().getMainNameByID(currentWorkoutStep.exerciseID);

        if (name != null) {
            return name;
        }

        return "Unnamed Exercise";
    }


    private void setupProgressView() {
        if (progressShouldBeVisible()) {
            this.progressView.setText(this.progress);
        }
        else {
            this.progressView.setVisibility(View.GONE);
        }
    }

    private boolean progressShouldBeVisible() {
        return this.progress != null;
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
}