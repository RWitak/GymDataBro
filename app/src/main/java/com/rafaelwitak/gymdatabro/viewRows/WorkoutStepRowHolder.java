package com.rafaelwitak.gymdatabro.viewRows;

import java.util.Arrays;
import java.util.List;

import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;
import com.rafaelwitak.gymdatabro.viewRows.workoutStepRows.DurationRow;
import com.rafaelwitak.gymdatabro.viewRows.workoutStepRows.RPERow;
import com.rafaelwitak.gymdatabro.viewRows.workoutStepRows.RepsRow;
import com.rafaelwitak.gymdatabro.viewRows.workoutStepRows.RestRow;
import com.rafaelwitak.gymdatabro.viewRows.workoutStepRows.WeightRow;


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
