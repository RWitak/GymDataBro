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
    protected EditText getEditText() {
        return binding.editProgramNotesEdit;
    }

    @Override
    public void setPreFilledText(Program program) {
        this.editText.setText(program.notes);
    }
}
