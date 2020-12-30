/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders;

import android.widget.EditText;

import com.rafaelwitak.gymdatabro.database.PerformanceSet;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

public class PerformanceSetWeightProvider extends PerformanceSetProviderEditText {
    public PerformanceSetWeightProvider(ActivityWorkoutStepBinding binding) {
        super(binding);
    }

    @Override
    protected EditText getEditTextFromBinding() {
        return binding.stepWeightPerformed;
    }

    @Override
    public PerformanceSet getUpdatedPerformanceSet(PerformanceSet performanceSet) {
        performanceSet.setWeight(getWeightAsFloat());
        return performanceSet;
    }

    private Float getWeightAsFloat() {
        return getFloatOrNullFromString(getStringFromEditText());
    }
}
