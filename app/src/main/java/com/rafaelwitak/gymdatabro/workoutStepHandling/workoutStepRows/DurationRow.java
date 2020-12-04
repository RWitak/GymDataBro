package com.rafaelwitak.gymdatabro.workoutStepHandling.workoutStepRows;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;
import com.rafaelwitak.gymdatabro.workoutStepHandling.WorkoutStepTextInputRow;

public class DurationRow extends WorkoutStepTextInputRow {
    public DurationRow(ActivityWorkoutStepBinding binding, WorkoutStep workoutStep) {
        super(binding, workoutStep);
    }

    @Override
    protected boolean shouldBeVisible() {
        return (currentWorkoutStep.durationSeconds != null);
    }

    @Override
    protected Number getExpectedValue() {
        return currentWorkoutStep.durationSeconds;
    }

    @Override
    protected View getRowViewFromBinding() {
        return binding.stepDurationRow;
    }

    @Override
    protected TextView getExpectedValueTextView() {
        return binding.stepDurationPrescribed;
    }

    @Override
    protected EditText getActualValueEditText() {
        return binding.stepDurationPerformed;
    }
}
