/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.workoutStepHandling.workoutStepRows;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;
import com.rafaelwitak.gymdatabro.workoutStepHandling.WorkoutStepTextInputRow;

public class RepsRow extends WorkoutStepTextInputRow {
    public RepsRow(ActivityWorkoutStepBinding binding, WorkoutStep workoutStep) {
        super(binding, workoutStep);
    }

    @Override
    protected boolean shouldBeVisible() {
        return currentWorkoutStep.getReps() != null;
    }

    @Override
    protected Number getExpectedValue() {
        return currentWorkoutStep.getReps();
    }

    @Override
    protected View getRowViewFromBinding() {
        return binding.stepRepsRow;
    }

    @Override
    protected TextView getExpectedValueTextView() {
        return binding.stepRepsPrescribed;
    }

    @Override
    protected EditText getActualValueEditText() {
        return binding.stepRepsPerformed;
    }
}
