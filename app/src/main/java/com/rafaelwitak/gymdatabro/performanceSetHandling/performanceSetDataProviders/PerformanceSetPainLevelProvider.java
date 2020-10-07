package com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders;

import androidx.appcompat.widget.AppCompatSeekBar;

import com.rafaelwitak.gymdatabro.performanceSetHandling.PerformanceSetDataProvider;
import com.rafaelwitak.gymdatabro.database.PerformanceSet;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

public class PerformanceSetPainLevelProvider implements PerformanceSetDataProvider {
    private final AppCompatSeekBar painSlider;

    public PerformanceSetPainLevelProvider(ActivityWorkoutStepBinding binding) {
        this.painSlider = binding.stepPainSlider;
    }

    @Override
    public PerformanceSet getUpdatedPerformanceSet(PerformanceSet performanceSet) {
        performanceSet.setPainLevel(painSlider.getProgress());
        return performanceSet;
    }
}
