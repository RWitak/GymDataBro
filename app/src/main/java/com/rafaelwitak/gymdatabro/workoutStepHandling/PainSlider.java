package com.rafaelwitak.gymdatabro.workoutStepHandling;

import android.view.View;

import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

public class PainSlider extends WorkoutStepRow{

    public PainSlider(ActivityWorkoutStepBinding binding, WorkoutStep workoutStep) {
        super(binding, workoutStep);
    }

    @Override
    protected View getRowViewFromBinding() {
        return binding.stepPainRow;
    }

    @Override
    public void setup() {
        // Currently no setup necessary
    }


    @Override
    protected boolean shouldBeVisible() {
        // Currently no adverse conditions
        return true;
    }
}
