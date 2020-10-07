package com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders;

import com.rafaelwitak.gymdatabro.performanceSetHandling.PerformanceSetDataProvider;
import com.rafaelwitak.gymdatabro.database.PerformanceSet;
import com.rafaelwitak.gymdatabro.database.WorkoutStep;

public class PerformanceSetExerciseIdProvider implements PerformanceSetDataProvider {
    private final Integer exerciseID;

    public PerformanceSetExerciseIdProvider(WorkoutStep currentWorkoutStep) {
        this.exerciseID = currentWorkoutStep.exerciseID;
    }

    @Override
    public PerformanceSet getUpdatedPerformanceSet(PerformanceSet performanceSet) {
        performanceSet.setExerciseID(this.exerciseID);
        return performanceSet;
    }
}
