package com.rafaelwitak.gymdatabro.viewRows;

import com.rafaelwitak.gymdatabro.PerformanceSetDataProvider;
import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;
import com.rafaelwitak.gymdatabro.viewRows.workoutStepRows.DurationRow;
import com.rafaelwitak.gymdatabro.viewRows.workoutStepRows.RPERow;
import com.rafaelwitak.gymdatabro.viewRows.workoutStepRows.RepsRow;
import com.rafaelwitak.gymdatabro.viewRows.workoutStepRows.RestRow;
import com.rafaelwitak.gymdatabro.viewRows.workoutStepRows.WeightRow;

import java.util.Arrays;
import java.util.List;


public class WorkoutStepRowHolder implements PerformanceSetDataProvider {

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

    @Override
    public int getExerciseID() {
        //TODO implement
        return 0;
    }

    @Override
    public Integer getReps() {
        return (Integer) this.repsRow.getActualValue();
    }

    @Override
    public Float getWeight() {
        return (Float) this.weightRow.getActualValue();
    }

    @Override
    public Integer getSecondsPerformed() {
        return (Integer) this.durationRow.getActualValue();
    }

    @Override
    public Integer getSecondsRested() {
        return (Integer) this.restRow.getActualValue();
    }

    @Override
    public Float getRpe() {
        return (Float) this.rpeRow.getActualValue();
    }

    @Override
    public Integer getPainLevel() {
        //TODO implement
        return 0;
    }

    @Override
    public String getNotes() {
        return null;
    }
}
