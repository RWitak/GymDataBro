package com.rafaelwitak.gymdatabro.workoutStepRows;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

public class DurationRow extends WorkoutStepRow {
    public DurationRow(Context context, ActivityWorkoutStepBinding binding, WorkoutStep workoutStep) {
        super(context, binding, workoutStep);
    }

    @Override
    protected boolean shouldBeVisible() {
        return (currentWorkoutStep.durationSeconds != null);
    }

    @Override
    protected Object getExpectedValue() {
        return currentWorkoutStep.durationSeconds;
    }

    @Override
    protected View getRowViewFromBinding() {
        return binding.stepDurationRow;
    }

    @Override
    protected TextView getExpectedValueView() {
        return binding.stepDurationPrescribed;
    }

    @Override
    protected EditText getActualValueView() {
        return binding.stepDurationPerformed;
    }
}
