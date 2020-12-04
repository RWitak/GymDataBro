package com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders;

import com.rafaelwitak.gymdatabro.database.PerformanceSet;
import com.rafaelwitak.gymdatabro.performanceSetHandling.PerformanceSetDataProvider;

public class PerformanceSetWorkoutInstanceIdProvider implements PerformanceSetDataProvider {
    private final Integer workoutInstanceId;

    public PerformanceSetWorkoutInstanceIdProvider(Integer workoutInstanceId) {
        this.workoutInstanceId = workoutInstanceId;
    }

    @Override
    public PerformanceSet getUpdatedPerformanceSet(PerformanceSet performanceSet) {
        performanceSet.workoutInstanceId = workoutInstanceId;
        return performanceSet;
    }
}
