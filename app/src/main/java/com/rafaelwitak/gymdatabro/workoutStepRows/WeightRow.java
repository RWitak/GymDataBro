package com.rafaelwitak.gymdatabro.workoutStepRows;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

public class WeightRow extends WorkoutStepRow {
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
    protected TextView getExpectedValueView() {
        return binding.stepWeightPrescribed;
    }

    @Override
    protected EditText getActualValueView() {
        return binding.stepWeightPerformed;
    }
}
