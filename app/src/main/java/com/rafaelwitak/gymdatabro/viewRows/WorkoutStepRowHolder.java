package com.rafaelwitak.gymdatabro.viewRows;

import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;
import com.rafaelwitak.gymdatabro.viewRows.workoutStepRows.DurationRow;
import com.rafaelwitak.gymdatabro.viewRows.workoutStepRows.RPERow;
import com.rafaelwitak.gymdatabro.viewRows.workoutStepRows.RepsRow;
import com.rafaelwitak.gymdatabro.viewRows.workoutStepRows.RestRow;
import com.rafaelwitak.gymdatabro.viewRows.workoutStepRows.WeightRow;

import java.util.Arrays;
import java.util.List;


public class WorkoutStepRowHolder{

    //TODO implement PerformanceSetDataProvider methods
    //TODO find way to integrate other Rows

    private RepsRow repsRow;
    private WeightRow weightRow;
    private RPERow rpeRow;
    private DurationRow durationRow;
    private RestRow restRow;

    public WorkoutStepRowHolder(ActivityWorkoutStepBinding binding,
                                WorkoutStep currentWorkoutStep) {
        this.repsRow = new RepsRow(binding, currentWorkoutStep);
        this.weightRow = new WeightRow(binding, currentWorkoutStep);
        this.rpeRow = new RPERow(binding, currentWorkoutStep);
        this.durationRow = new DurationRow(binding, currentWorkoutStep);
        this.restRow = new RestRow(binding, currentWorkoutStep);
    }

    public List<WorkoutStepRow> getRows() {
        return Arrays.asList(
            this.repsRow,
            this.weightRow,
            this.rpeRow,
            this.durationRow,
            this.restRow
        );
    }
}
