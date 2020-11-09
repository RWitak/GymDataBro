package com.rafaelwitak.gymdatabro.exerciseHandling.editExerciseRows;

import android.widget.EditText;

import com.rafaelwitak.gymdatabro.databinding.ActivityEditExerciseBinding;
import com.rafaelwitak.gymdatabro.exerciseHandling.EditExerciseRow;

public class NameRow extends EditExerciseRow {
    public NameRow(ActivityEditExerciseBinding binding) {
        super(binding);
    }

    @Override
    protected EditText getEditText() {
        return this.binding.editExerciseNameEdit;
    }
}