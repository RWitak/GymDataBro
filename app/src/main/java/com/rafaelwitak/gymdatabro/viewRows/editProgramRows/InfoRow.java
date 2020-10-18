package com.rafaelwitak.gymdatabro.viewRows.editProgramRows;

import android.widget.EditText;

import com.rafaelwitak.gymdatabro.databinding.ActivityEditProgramBinding;
import com.rafaelwitak.gymdatabro.viewRows.EditProgramRow;

public class InfoRow extends EditProgramRow {
    public InfoRow(ActivityEditProgramBinding binding) {
        super(binding);
    }

    @Override
    protected EditText getEditTextFromBinding(ActivityEditProgramBinding binding) {
        return binding.editProgramInfoEdit;
    }
}
