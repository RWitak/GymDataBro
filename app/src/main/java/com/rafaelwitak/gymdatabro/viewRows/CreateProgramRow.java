package com.rafaelwitak.gymdatabro.viewRows;

import android.widget.EditText;

import com.rafaelwitak.gymdatabro.databinding.ActivityCreateProgramBinding;
public abstract class CreateProgramRow {
    protected EditText editText;

    public CreateProgramRow(ActivityCreateProgramBinding binding) {
        editText = getEditTextFromBinding(binding);
    }

    public Object getEditTextValue() {
        if (editText.getText().toString().isEmpty()) {
            return null;
        }
        return editText.getText();
    }

    protected abstract EditText getEditTextFromBinding(ActivityCreateProgramBinding binding);
}
