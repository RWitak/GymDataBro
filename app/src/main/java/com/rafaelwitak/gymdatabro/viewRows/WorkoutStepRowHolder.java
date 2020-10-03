package com.rafaelwitak.gymdatabro.viewRows;

import java.util.Arrays;
import java.util.List;

import com.rafaelwitak.gymdatabro.PerformanceSetDataProvider;
import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;
import com.rafaelwitak.gymdatabro.viewRows.workoutStepRows.DurationRow;
import com.rafaelwitak.gymdatabro.viewRows.workoutStepRows.RPERow;
import com.rafaelwitak.gymdatabro.viewRows.workoutStepRows.RepsRow;
import com.rafaelwitak.gymdatabro.viewRows.workoutStepRows.RestRow;
import com.rafaelwitak.gymdatabro.viewRows.workoutStepRows.WeightRow;


public abstract class WorkoutStepRowHolder implements PerformanceSetDataProvider {

    //TODO implement PerformanceSetDataProvider methods

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

    @Override
    public int getExerciseID() {
        return 0;
    }

    @Override
    public Integer getReps() {
        return 0;
    }

    @Override
    public Float getWeight() {
        return null;
    }

    @Override
    public Integer getSecondsPerformed() {
        return null;
    }

    @Override
    public Integer getSecondsRested() {
        return null;
    }

    @Override
    public Float getRpe() {
        return null;
    }

    @Override
    public Integer getPainLevel() {
        return null;
    }

    @Override
    public String getNotes() {
        return null;
    }
}
