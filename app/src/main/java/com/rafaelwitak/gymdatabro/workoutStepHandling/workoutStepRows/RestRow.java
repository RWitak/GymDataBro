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

public class RestRow extends WorkoutStepTextInputRow {
    public RestRow(ActivityWorkoutStepBinding binding, WorkoutStep workoutStep) {
        super(binding, workoutStep);
    }

    @Override
    protected boolean shouldBeVisible() {
        return (currentWorkoutStep.restSeconds != null);
    }

    @Override
    protected Number getExpectedValue() {
        return currentWorkoutStep.restSeconds;
    }

    @Override
    protected View getRowViewFromBinding() {
        return binding.stepRestRow;
    }

    @Override
    protected TextView getExpectedValueTextView() {
        return binding.stepRestPrescribed;
    }

    @Override
    protected EditText getActualValueEditText() {
        return binding.stepRestPerformed;
    }
}
