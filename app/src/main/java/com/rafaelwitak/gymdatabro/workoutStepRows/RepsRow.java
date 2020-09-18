package com.rafaelwitak.gymdatabro.workoutStepRows;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

public class RepsRow extends WorkoutStepRow {
    public RepsRow(ActivityWorkoutStepBinding binding, WorkoutStep workoutStep) {
        super(binding, workoutStep);
    }

    @Override
    protected boolean shouldBeVisible() {
        return currentWorkoutStep.reps != null;
    }

    @Override
    protected Object getExpectedValue() {
        return currentWorkoutStep.reps;
    }

    @Override
    protected View getRowViewFromBinding() {
        return binding.stepRepsRow;
    }

    @Override
    protected TextView getExpectedValueView() {
        return binding.stepRepsPrescribed;
    }

    @Override
    protected EditText getActualValueView() {
        return binding.stepRepsPerformed;
    }
}
