package com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders;

import android.widget.EditText;

import com.rafaelwitak.gymdatabro.database.PerformanceSet;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

public class PerformanceSetSecondsRestedProvider extends PerformanceSetProviderEditText {
    public PerformanceSetSecondsRestedProvider(ActivityWorkoutStepBinding binding) {
        super(binding);
    }

    @Override
    protected EditText getEditTextFromBinding() {
        return binding.stepRestPerformed;
    }

    @Override
    public PerformanceSet getUpdatedPerformanceSet(PerformanceSet performanceSet) {
        performanceSet.setSecondsRested(getSecondsRestedAsInteger());
        return performanceSet;
    }

    private Integer getSecondsRestedAsInteger() {
        return getIntegerOrNullFromString(getStringFromEditText());
    }
}
