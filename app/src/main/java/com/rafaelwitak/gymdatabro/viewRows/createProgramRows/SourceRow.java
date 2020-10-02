package com.rafaelwitak.gymdatabro.viewRows.createProgramRows;

import android.widget.EditText;

import com.rafaelwitak.gymdatabro.databinding.ActivityCreateProgramBinding;
import com.rafaelwitak.gymdatabro.viewRows.CreateProgramRow;

public class SourceRow extends CreateProgramRow {
    public SourceRow(ActivityCreateProgramBinding binding) {
        super(binding);
    }

    @Override
    protected EditText getEditTextFromBinding(ActivityCreateProgramBinding binding) {
        return binding.createProgramSourceEdit;
    }
}
