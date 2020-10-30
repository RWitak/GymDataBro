package com.rafaelwitak.gymdatabro.performanceSetHandling.performanceSetDataProviders;

import android.text.Editable;
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

    protected Editable getInputFromEditText() {
        return this.editText.getEditableText();
        //FIXME: Returns empty fields as empty Strings -> Integer.valueOf() is undefined!
    }

    @Override
    public abstract PerformanceSet getUpdatedPerformanceSet(PerformanceSet performanceSet);
}
