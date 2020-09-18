package com.rafaelwitak.gymdatabro.workoutStepRows;

import java.util.Arrays;
import java.util.List;

import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;


public abstract class WorkoutStepRowHolder {

    public static List<WorkoutStepRow> getRows(ActivityWorkoutStepBinding binding,
                                               WorkoutStep currentWorkoutStep) {
        return Arrays.asList(
                new RepsRow(binding, currentWorkoutStep),
                new WeightRow(binding, currentWorkoutStep),
                new RPERow(binding, currentWorkoutStep),
                new DurationRow(binding, currentWorkoutStep),
                new RestRow(binding, currentWorkoutStep)
        );
    }
}
