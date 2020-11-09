package com.rafaelwitak.gymdatabro.workoutStepHandling.workoutStepRows;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;
import com.rafaelwitak.gymdatabro.workoutStepHandling.WorkoutStepRow;

public class ExerciseNameRow extends WorkoutStepRow {
    private final GymBroDatabase database;

    private final TextView nameView;
    private final TextView progressView;

    private final String progress;


    public ExerciseNameRow(ActivityWorkoutStepBinding binding,
                           WorkoutStep workoutStep,
                           Context context) {
        super(binding, workoutStep);

        this.database = GymBroDatabase.getDatabase(context);

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
        this.nameView.setText(getCurrentWorkoutStepName());
    }

    private String getCurrentWorkoutStepName() {
        String name = currentWorkoutStep.name;

        if (name == null && name.isEmpty()) {
            return database.exerciseDAO().getExerciseByID(currentWorkoutStep.exerciseID).name;
        }
        return name;
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
