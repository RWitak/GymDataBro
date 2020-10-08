package com.rafaelwitak.gymdatabro.workoutStepHandling;

import android.view.View;
import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

public abstract class WorkoutStepRow {
    protected final ActivityWorkoutStepBinding binding;
    protected final WorkoutStep currentWorkoutStep;
    protected final View rowView;

    public WorkoutStepRow(ActivityWorkoutStepBinding binding,
                          WorkoutStep workoutStep) {

        this.currentWorkoutStep = workoutStep;
        this.binding = binding;

        this.rowView = getRowViewFromBinding();
    }

    protected abstract View getRowViewFromBinding();

    public abstract void setup();

    public void makeInvisible() {
        this.rowView.setVisibility(View.GONE);
    }

    protected abstract boolean shouldBeVisible();
}
