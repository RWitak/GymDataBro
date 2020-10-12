package com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders;

import android.widget.EditText;

import com.rafaelwitak.gymdatabro.database.PerformanceSet;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

public class PerformanceSetSecondsPerformedProvider extends PerformanceSetProviderEditText {
    public PerformanceSetSecondsPerformedProvider(ActivityWorkoutStepBinding binding) {
        super(binding);
    }

    @Override
    protected EditText getEditTextFromBinding() {
        return binding.stepDurationPerformed;
    }

    @Override
    public PerformanceSet getUpdatedPerformanceSet(PerformanceSet performanceSet) {
        performanceSet.setSecondsPerformed(
                Integer.valueOf(getInputFromEditText().toString()));
        return performanceSet;
    }
}
