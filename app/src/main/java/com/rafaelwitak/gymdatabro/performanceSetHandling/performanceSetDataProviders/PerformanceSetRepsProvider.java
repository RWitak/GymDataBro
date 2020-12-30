/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders;

import android.widget.EditText;

import com.rafaelwitak.gymdatabro.database.PerformanceSet;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

public class PerformanceSetRepsProvider extends PerformanceSetProviderEditText {

    public PerformanceSetRepsProvider(ActivityWorkoutStepBinding binding) {
        super(binding);
    }

    @Override
    protected EditText getEditTextFromBinding() {
        return binding.stepRepsPerformed;
    }

    @Override
    public PerformanceSet getUpdatedPerformanceSet(PerformanceSet performanceSet) {
        performanceSet.setReps(getRepsAsInteger());
        return performanceSet;
    }

    private Integer getRepsAsInteger() {
        return getIntegerOrNullFromString(getStringFromEditText());
    }
}
