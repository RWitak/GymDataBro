package com.rafaelwitak.gymdatabro.viewRows.createProgramRows;

import android.widget.EditText;

import com.rafaelwitak.gymdatabro.databinding.ActivityCreateProgramBinding;
import com.rafaelwitak.gymdatabro.viewRows.CreateProgramRow;

public class NameRow extends CreateProgramRow {
    public NameRow(ActivityCreateProgramBinding binding) {
        super(binding);
    }

    @Override
    protected EditText getEditTextFromBinding(ActivityCreateProgramBinding binding) {
        return binding.createProgramNameEdit;
    }
}