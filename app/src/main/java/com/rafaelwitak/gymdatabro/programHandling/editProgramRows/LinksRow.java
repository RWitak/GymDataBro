package com.rafaelwitak.gymdatabro.programHandling.editProgramRows;

import android.widget.EditText;

import com.rafaelwitak.gymdatabro.database.Program;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditProgramBinding;
import com.rafaelwitak.gymdatabro.programHandling.EditProgramRow;

public class LinksRow extends EditProgramRow {
    public LinksRow(ActivityEditProgramBinding binding) {
        super(binding);
    }

    @Override
    protected EditText getEditText() {
        return binding.editProgramLinksEdit;
    }

    @Override
    public void setPreFilledText(Program program) {
        this.editText.setText(program.links);
    }
}
