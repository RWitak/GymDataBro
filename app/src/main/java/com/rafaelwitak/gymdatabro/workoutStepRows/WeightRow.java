package com.rafaelwitak.gymdatabro.workoutStepRows;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

public class WeightRow extends WorkoutStepRow {
    public WeightRow(Context context, ActivityWorkoutStepBinding binding, WorkoutStep workoutStep) {
        super(context, binding, workoutStep);
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
    protected TextView getExpectedValueView() {
        return binding.stepWeightPrescribed;
    }

    @Override
    protected EditText getActualValueView() {
        return binding.stepWeightPerformed;
    }
}
