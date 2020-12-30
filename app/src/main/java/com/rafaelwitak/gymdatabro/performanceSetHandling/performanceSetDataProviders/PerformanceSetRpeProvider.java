/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders;

import android.widget.EditText;

import com.rafaelwitak.gymdatabro.database.PerformanceSet;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

public class PerformanceSetRpeProvider extends PerformanceSetProviderEditText {
    public PerformanceSetRpeProvider(ActivityWorkoutStepBinding binding) {
        super(binding);
    }

    @Override
    protected EditText getEditTextFromBinding() {
        return binding.stepRpePerformed;
    }

    @Override
    public PerformanceSet getUpdatedPerformanceSet(PerformanceSet performanceSet) {
        performanceSet.setRpe(getRpeAsFloat());
        return performanceSet;
    }

    private Float getRpeAsFloat() {
        return getFloatOrNullFromString(getStringFromEditText());
    }
}
