/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders;

import androidx.appcompat.widget.AppCompatSeekBar;

import com.rafaelwitak.gymdatabro.database.PerformanceSet;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;
import com.rafaelwitak.gymdatabro.performanceSetHandling.PerformanceSetDataProvider;

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
