package com.rafaelwitak.gymdatabro.viewRows.createProgramRows;

import android.widget.EditText;

import com.rafaelwitak.gymdatabro.databinding.ActivityCreateProgramBinding;
import com.rafaelwitak.gymdatabro.viewRows.CreateProgramRow;

public class InfoRow extends CreateProgramRow {
    public InfoRow(ActivityCreateProgramBinding binding) {
        super(binding);
    }

    @Override
    protected EditText getEditTextFromBinding(ActivityCreateProgramBinding binding) {
        return binding.createProgramInfoEdit;
    }
}
