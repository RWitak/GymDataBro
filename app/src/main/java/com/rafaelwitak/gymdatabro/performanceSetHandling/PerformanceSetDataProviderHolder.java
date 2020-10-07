package com.rafaelwitak.gymdatabro.performanceSetHandling;

import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;
import com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders.PerformanceSetExerciseIdProvider;
import com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders.PerformanceSetNotesProvider;
import com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders.PerformanceSetPainLevelProvider;
import com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders.PerformanceSetRepsProvider;
import com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders.PerformanceSetRpeProvider;
import com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders.PerformanceSetSecondsPerformedProvider;
import com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders.PerformanceSetSecondsRestedProvider;
import com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders.PerformanceSetWeightProvider;

import java.util.Arrays;
import java.util.List;

public class PerformanceSetDataProviderHolder {

    private PerformanceSetExerciseIdProvider exerciseIdProvider;
    private PerformanceSetRepsProvider repsProvider;
    private PerformanceSetWeightProvider weightProvider;
    private PerformanceSetSecondsPerformedProvider secondsPerformedProvider;
    private PerformanceSetSecondsRestedProvider secondsRestedProvider;
    private PerformanceSetRpeProvider rpeProvider;
    private PerformanceSetPainLevelProvider painLevelProvider;
    private PerformanceSetNotesProvider notesProvider;


    public PerformanceSetDataProviderHolder(ActivityWorkoutStepBinding binding,
                                            WorkoutStep currentWorkoutStep) {

        this.exerciseIdProvider = new PerformanceSetExerciseIdProvider(currentWorkoutStep);
        this.repsProvider = new PerformanceSetRepsProvider(binding);
        this.weightProvider = new PerformanceSetWeightProvider(binding);
        this.secondsPerformedProvider = new PerformanceSetSecondsPerformedProvider(binding);
        this.secondsRestedProvider = new PerformanceSetSecondsRestedProvider(binding);
        this.rpeProvider = new PerformanceSetRpeProvider(binding);
        this.painLevelProvider = new PerformanceSetPainLevelProvider(binding);
        this.notesProvider = new PerformanceSetNotesProvider(binding);
    }

    public List<PerformanceSetDataProvider> getDataProviders() {
        return Arrays.asList(
                this.exerciseIdProvider,
                this.repsProvider,
                this.weightProvider,
                this.secondsPerformedProvider,
                this.secondsRestedProvider,
                this.rpeProvider,
                this.painLevelProvider,
                this.notesProvider
        );
    }
}
