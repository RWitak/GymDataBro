/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.performanceSetHandling;

import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;
import com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders.PerformanceSetNotesProvider;
import com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders.PerformanceSetPainLevelProvider;
import com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders.PerformanceSetRepsProvider;
import com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders.PerformanceSetRpeProvider;
import com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders.PerformanceSetSecondsPerformedProvider;
import com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders.PerformanceSetSecondsRestedProvider;
import com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders.PerformanceSetWeightProvider;
import com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders.PerformanceSetWorkoutInstanceIdProvider;
import com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders.PerformanceSetWorkoutStepIdProvider;

import java.util.Arrays;
import java.util.List;

public class PerformanceSetDataProviderHolder {

    private final PerformanceSetWorkoutStepIdProvider workoutStepIdProvider;
    private final PerformanceSetWorkoutInstanceIdProvider workoutInstanceIdProvider;
    private final PerformanceSetRepsProvider repsProvider;
    private final PerformanceSetWeightProvider weightProvider;
    private final PerformanceSetSecondsPerformedProvider secondsPerformedProvider;
    private final PerformanceSetSecondsRestedProvider secondsRestedProvider;
    private final PerformanceSetRpeProvider rpeProvider;
    private final PerformanceSetPainLevelProvider painLevelProvider;
    private final PerformanceSetNotesProvider notesProvider;


    public PerformanceSetDataProviderHolder(ActivityWorkoutStepBinding binding,
                                            WorkoutStep currentWorkoutStep,
                                            Integer workoutInstanceId) {

        this.workoutStepIdProvider =
                new PerformanceSetWorkoutStepIdProvider(currentWorkoutStep);
        this.workoutInstanceIdProvider =
                new PerformanceSetWorkoutInstanceIdProvider(workoutInstanceId);
        this.repsProvider =
                new PerformanceSetRepsProvider(binding);
        this.weightProvider =
                new PerformanceSetWeightProvider(binding);
        this.secondsPerformedProvider =
                new PerformanceSetSecondsPerformedProvider(binding);
        this.secondsRestedProvider =
                new PerformanceSetSecondsRestedProvider(binding);
        this.rpeProvider =
                new PerformanceSetRpeProvider(binding);
        this.painLevelProvider =
                new PerformanceSetPainLevelProvider(binding);
        this.notesProvider =
                new PerformanceSetNotesProvider(binding);
    }

    public List
            <PerformanceSetDataProvider> getDataProviders() {
        return Arrays.asList(
                workoutStepIdProvider,
                workoutInstanceIdProvider,
                repsProvider,
                weightProvider,
                secondsPerformedProvider,
                secondsRestedProvider,
                rpeProvider,
                painLevelProvider,
                notesProvider
        );
    }
}
