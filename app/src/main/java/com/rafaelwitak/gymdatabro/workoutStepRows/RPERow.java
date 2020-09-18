package com.rafaelwitak.gymdatabro.workoutStepRows;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

public class RPERow extends WorkoutStepRow {
    public RPERow(ActivityWorkoutStepBinding binding, WorkoutStep workoutStep) {
        super(binding, workoutStep);
    }

    @Override
    protected boolean shouldBeVisible() {
        return (currentWorkoutStep.rpe != null);
    }

    @Override
    protected Object getExpectedValue() {
        return currentWorkoutStep.rpe;
    }

    @Override
    protected View getRowViewFromBinding() {
        return binding.stepRpeRow;
    }

    @Override
    protected TextView getExpectedValueView() {
        return binding.stepRpePrescribed;
    }

    @Override
    protected EditText getActualValueView() {
        return binding.stepRpePerformed;
    }
}
