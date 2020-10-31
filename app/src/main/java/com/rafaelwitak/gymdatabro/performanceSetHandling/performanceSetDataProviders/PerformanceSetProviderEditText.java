package com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders;

import android.widget.EditText;

import com.rafaelwitak.gymdatabro.database.PerformanceSet;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;
import com.rafaelwitak.gymdatabro.performanceSetHandling.PerformanceSetDataProvider;

public abstract class PerformanceSetProviderEditText implements PerformanceSetDataProvider {
    protected ActivityWorkoutStepBinding binding;
    protected EditText editText;

    PerformanceSetProviderEditText(ActivityWorkoutStepBinding binding) {
        this.binding = binding;
        this.editText = getEditTextFromBinding();
    }

    protected abstract EditText getEditTextFromBinding();

    protected String getStringFromEditText() {
        return this.editText.getEditableText().toString();
    }

    protected Integer getIntegerOrNullFromString(String s) {
        return (s == null || s.isEmpty()) ? null : Integer.valueOf(s);
    }

    protected Float getFloatOrNullFromString(String s) {
        return (s == null || s.isEmpty()) ? null : Float.parseFloat(s);
    }

    protected String getStringOrNullFromString(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }

    @Override
    public abstract PerformanceSet getUpdatedPerformanceSet(PerformanceSet performanceSet);
}
