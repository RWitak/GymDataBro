package com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders;

import com.rafaelwitak.gymdatabro.database.PerformanceSet;
import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.performanceSetHandling.PerformanceSetDataProvider;

public class PerformanceSetWorkoutStepIdProvider implements PerformanceSetDataProvider {
    private final Integer workoutStepId;

    public PerformanceSetWorkoutStepIdProvider(WorkoutStep currentWorkoutStep) {
        this.workoutStepId = currentWorkoutStep.id;
    }

    @Override
    public PerformanceSet getUpdatedPerformanceSet(PerformanceSet performanceSet) {
        performanceSet.setWorkoutStepId(workoutStepId);
        return performanceSet;
    }
}