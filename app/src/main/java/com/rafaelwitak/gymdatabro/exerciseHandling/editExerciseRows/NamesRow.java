package com.rafaelwitak.gymdatabro.exerciseHandling.editExerciseRows;

import android.widget.EditText;

import com.rafaelwitak.gymdatabro.database.Exercise;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditExerciseBinding;
import com.rafaelwitak.gymdatabro.exerciseHandling.EditExerciseRow;

public class NamesRow extends EditExerciseRow {
    public NamesRow(ActivityEditExerciseBinding binding) {
        super(binding);
    }

    @Override
    public void setPreFilledText(Exercise exercise) {
        //TODO: implement
    }

    @Override
    protected EditText getEditText() {
        return this.binding.editExerciseNamesEdit;
    }
}
