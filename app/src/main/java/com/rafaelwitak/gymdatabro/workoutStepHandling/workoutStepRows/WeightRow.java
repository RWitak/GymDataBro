package com.rafaelwitak.gymdatabro.workoutStepHandling.workoutStepRows;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;
import com.rafaelwitak.gymdatabro.workoutStepHandling.WorkoutStepTextInputRow;

public class WeightRow extends WorkoutStepTextInputRow {
    public WeightRow(ActivityWorkoutStepBinding binding, WorkoutStep workoutStep) {
        super(binding, workoutStep);
    }

    @Override
    protected boolean shouldBeVisible() {
        return currentWorkoutStep.weight != null;
    }

    @Override
    protected Object getExpectedValue() {
        return currentWorkoutStep.weight;
    }

    @Override
    protected View getRowViewFromBinding() {
        return binding.stepWeightRow;
    }

    @Override
    protected TextView getExpectedValueTextView() {
        return binding.stepWeightPrescribed;
    }

    @Override
    protected EditText getActualValueEditText() {
        return binding.stepWeightPerformed;
    }
}
