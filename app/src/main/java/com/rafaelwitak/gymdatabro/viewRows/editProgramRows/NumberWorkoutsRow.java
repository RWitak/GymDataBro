package com.rafaelwitak.gymdatabro.viewRows.editProgramRows;

import android.widget.EditText;

import com.rafaelwitak.gymdatabro.database.Program;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditProgramBinding;
import com.rafaelwitak.gymdatabro.viewRows.EditProgramRow;

public class NumberWorkoutsRow extends EditProgramRow {
    public NumberWorkoutsRow(ActivityEditProgramBinding binding) {
        super(binding);
    }

    @Override
    protected EditText getEditText() {
        return binding.editProgramNumberWorkoutsEdit;
    }

    @Override
    public void setPreFilledText(Program program) {
        this.editText.setText(program.number_workouts);
    }
}
