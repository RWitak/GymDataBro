package com.rafaelwitak.gymdatabro.exerciseHandling.editExerciseRows;

import android.widget.EditText;

import com.rafaelwitak.gymdatabro.databinding.ActivityEditExerciseBinding;
import com.rafaelwitak.gymdatabro.exerciseHandling.EditExerciseRow;

public class PrRow extends EditExerciseRow {
    public PrRow(ActivityEditExerciseBinding binding) {
        super(binding);
    }

    @Override
    public EditText getEditText(ActivityEditExerciseBinding binding) {
        return binding.editExercisePrEdit;
    }
}
