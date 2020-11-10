package com.rafaelwitak.gymdatabro.exerciseHandling;

import android.widget.EditText;

import com.rafaelwitak.gymdatabro.EditRow;
import com.rafaelwitak.gymdatabro.EditRowTextGetter;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditExerciseBinding;

public abstract class EditExerciseRow
        extends EditRow
        implements EditRowTextGetter<ActivityEditExerciseBinding> {

    protected ActivityEditExerciseBinding binding;

    public EditExerciseRow(ActivityEditExerciseBinding binding) {
        super();
        this.binding = binding;
        this.editText = getEditText(binding);
    }

    @Override
    public abstract EditText getEditText(ActivityEditExerciseBinding binding);

    public void setPreFilledText(String text) {
        this.editText.setText(text);
    }
}
