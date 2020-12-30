/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.workoutStepHandling;

import android.content.Context;

import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;
import com.rafaelwitak.gymdatabro.workoutStepHandling.workoutStepRows.DurationRow;
import com.rafaelwitak.gymdatabro.workoutStepHandling.workoutStepRows.ExerciseNameRow;
import com.rafaelwitak.gymdatabro.workoutStepHandling.workoutStepRows.RPERow;
import com.rafaelwitak.gymdatabro.workoutStepHandling.workoutStepRows.RepsRow;
import com.rafaelwitak.gymdatabro.workoutStepHandling.workoutStepRows.RestRow;
import com.rafaelwitak.gymdatabro.workoutStepHandling.workoutStepRows.WeightRow;

import java.util.Arrays;
import java.util.List;

//import com.rafaelwitak.gymdatabro.workoutStepHandling.workoutStepRows.PainSlider;


public class WorkoutStepRowHolder{

    private final ExerciseNameRow exerciseNameRow;
    private final RepsRow repsRow;
    private final WeightRow weightRow;
    private final RPERow rpeRow;
    private final DurationRow durationRow;
    private final RestRow restRow;
//    private final PainSlider painSlider;

    public WorkoutStepRowHolder(ActivityWorkoutStepBinding binding,
                                WorkoutStep currentWorkoutStep,
                                Context context) {
        this.exerciseNameRow = new ExerciseNameRow(binding, currentWorkoutStep, context);
        this.repsRow = new RepsRow(binding, currentWorkoutStep);
        this.weightRow = new WeightRow(binding, currentWorkoutStep);
        this.rpeRow = new RPERow(binding, currentWorkoutStep);
        this.durationRow = new DurationRow(binding, currentWorkoutStep);
        this.restRow = new RestRow(binding, currentWorkoutStep);
//        this.painSlider = new PainSlider(binding, currentWorkoutStep);
    }

    public List<WorkoutStepRow> getRows() {
        return Arrays.asList(
            this.exerciseNameRow,
            this.repsRow,
            this.weightRow,
            this.rpeRow,
            this.durationRow,
            this.restRow
//            this.painSlider
        );
    }

    public void setupAllRows() {
        for ( WorkoutStepRow row : getRows() ) {
            row.setup();
        }
    }
}
