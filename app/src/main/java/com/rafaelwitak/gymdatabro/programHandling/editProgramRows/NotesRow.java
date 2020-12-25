/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.programHandling.editProgramRows;

import android.widget.EditText;

import com.rafaelwitak.gymdatabro.database.Program;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditProgramBinding;
import com.rafaelwitak.gymdatabro.programHandling.EditProgramRow;

public class NotesRow extends EditProgramRow {
    public NotesRow(ActivityEditProgramBinding binding) {
        super(binding);
    }

    @Override
    public EditText getEditText(ActivityEditProgramBinding binding) {
        return this.binding.editProgramNotesEdit;
    }

    @Override
    public void setPreFilledText(Program program) {
        this.editText.setText(program.notes);
    }
}
