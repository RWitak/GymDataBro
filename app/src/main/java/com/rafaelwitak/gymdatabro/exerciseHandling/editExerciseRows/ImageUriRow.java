package com.rafaelwitak.gymdatabro.exerciseHandling.editExerciseRows;

import android.widget.EditText;

import com.rafaelwitak.gymdatabro.database.Exercise;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditExerciseBinding;
import com.rafaelwitak.gymdatabro.exerciseHandling.EditExerciseRow;

public class ImageUriRow extends EditExerciseRow {
    public ImageUriRow(ActivityEditExerciseBinding binding) {
        super(binding);
    }

    @Override
    public void setPreFilledText(Exercise exercise) {
        //FIXME: correct Database handling of URIS and change this line:
        this.editText.setText(exercise.imgA_URI);
    }

    @Override
    protected EditText getEditText() {
        return this.binding.editExerciseImgUrisEdit;
    }
}
