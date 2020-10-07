package com.rafaelwitak.gymdatabro.performanceSetHandling;

import android.content.Context;

import com.rafaelwitak.gymdatabro.database.PerformanceSet;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

public interface PerformanceSetDataProvider {
    abstract PerformanceSet getUpdatedPerformanceSet(PerformanceSet performanceSet);
}
