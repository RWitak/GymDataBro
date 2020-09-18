package com.rafaelwitak.gymdatabro.workoutStepRows;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

public class RestRow extends WorkoutStepRow {
    public RestRow(ActivityWorkoutStepBinding binding, WorkoutStep workoutStep) {
        super(binding, workoutStep);
    }

    @Override
    protected boolean shouldBeVisible() {
        return (currentWorkoutStep.restSeconds != null);
    }

    @Override
    protected Object getExpectedValue() {
        return currentWorkoutStep.restSeconds;
    }

    @Override
    protected View getRowViewFromBinding() {
        return binding.stepRestRow;
    }

    @Override
    protected TextView getExpectedValueView() {
        return binding.stepRestPrescribed;
    }

    @Override
    protected EditText getActualValueView() {
        return binding.stepRestPerformed;
    }
}
