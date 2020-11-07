package com.rafaelwitak.gymdatabro.exerciseHandling;

import com.rafaelwitak.gymdatabro.EditRow;
import com.rafaelwitak.gymdatabro.database.Exercise;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditExerciseBinding;

public abstract class EditExerciseRow extends EditRow {

    protected ActivityEditExerciseBinding binding;

    public EditExerciseRow(ActivityEditExerciseBinding binding) {
        super();
        this.binding = binding;
    }

    public abstract void setPreFilledText(Exercise exercise);
}
