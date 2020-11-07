package com.rafaelwitak.gymdatabro.programHandling;

import com.rafaelwitak.gymdatabro.EditRow;
import com.rafaelwitak.gymdatabro.database.Program;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditProgramBinding;

public abstract class EditProgramRow extends EditRow {
    protected ActivityEditProgramBinding binding;

    public EditProgramRow(com.rafaelwitak.gymdatabro.databinding.ActivityEditProgramBinding binding) {
        super();
        this.binding = binding;
    }

    public abstract void setPreFilledText(Program program);
}
