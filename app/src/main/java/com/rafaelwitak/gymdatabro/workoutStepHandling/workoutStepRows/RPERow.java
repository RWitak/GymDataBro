package com.rafaelwitak.gymdatabro.workoutStepHandling.workoutStepRows;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;
import com.rafaelwitak.gymdatabro.workoutStepHandling.WorkoutStepTextInputRow;

public class RPERow extends WorkoutStepTextInputRow {
    public RPERow(ActivityWorkoutStepBinding binding, WorkoutStep workoutStep) {
        super(binding, workoutStep);
    }

    @Override
    protected boolean shouldBeVisible() {
        return (currentWorkoutStep.rpe != null);
    }

    @Override
    protected Number getExpectedValue() {
        return currentWorkoutStep.rpe;
    }

    @Override
    protected View getRowViewFromBinding() {
        return binding.stepRpeRow;
    }

    @Override
    protected TextView getExpectedValueTextView() {
        return binding.stepRpePrescribed;
    }

    @Override
    protected EditText getActualValueEditText() {
        return binding.stepRpePerformed;
    }
}
