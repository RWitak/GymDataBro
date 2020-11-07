package com.rafaelwitak.gymdatabro.exerciseHandling.editExerciseRows;

import android.widget.EditText;

import com.rafaelwitak.gymdatabro.database.Exercise;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditExerciseBinding;
import com.rafaelwitak.gymdatabro.exerciseHandling.EditExerciseRow;

public class EquipmentRow extends EditExerciseRow {
    public EquipmentRow(ActivityEditExerciseBinding binding) {
        super(binding);
    }

    @Override
    public void setPreFilledText(Exercise exercise) {
        this.editText.setText(exercise.equipment);
    }

    @Override
    protected EditText getEditText() {
        return this.binding.editExerciseEquipmentEdit;
    }
}
