package com.rafaelwitak.gymdatabro.workoutStepHandling;

import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;
import com.rafaelwitak.gymdatabro.workoutStepHandling.workoutStepRows.DurationRow;
import com.rafaelwitak.gymdatabro.workoutStepHandling.workoutStepRows.RPERow;
import com.rafaelwitak.gymdatabro.workoutStepHandling.workoutStepRows.RepsRow;
import com.rafaelwitak.gymdatabro.workoutStepHandling.workoutStepRows.RestRow;
import com.rafaelwitak.gymdatabro.workoutStepHandling.workoutStepRows.WeightRow;

import java.util.Arrays;
import java.util.List;


public class WorkoutStepRowHolder{

    //TODO find way to integrate other Rows

    private RepsRow repsRow;
    private WeightRow weightRow;
    private RPERow rpeRow;
    private DurationRow durationRow;
    private RestRow restRow;
    private PainSlider painSlider;

    public WorkoutStepRowHolder(ActivityWorkoutStepBinding binding,
                                WorkoutStep currentWorkoutStep) {
        this.repsRow = new RepsRow(binding, currentWorkoutStep);
        this.weightRow = new WeightRow(binding, currentWorkoutStep);
        this.rpeRow = new RPERow(binding, currentWorkoutStep);
        this.durationRow = new DurationRow(binding, currentWorkoutStep);
        this.restRow = new RestRow(binding, currentWorkoutStep);
        this.painSlider = new PainSlider(binding, currentWorkoutStep);
    }

    public List<WorkoutStepRow> getRows() {
        return Arrays.asList(
            this.repsRow,
            this.weightRow,
            this.rpeRow,
            this.durationRow,
            this.restRow,
            this.painSlider
        );
    }
}
