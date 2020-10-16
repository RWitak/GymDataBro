package com.rafaelwitak.gymdatabro.viewRows;

import android.widget.EditText;
public abstract class EditProgramRow {
    protected EditText editText;

    public EditProgramRow(com.rafaelwitak.gymdatabro.databinding.ActivityEditProgramBinding binding) {
        editText = getEditTextFromBinding(binding);
    }

    public Object getEditTextValue() {
        if (editText.getText().toString().isEmpty()) {
            return null;
        }
        return editText.getText();
    }

    protected abstract EditText getEditTextFromBinding(com.rafaelwitak.gymdatabro.databinding.ActivityEditProgramBinding binding);
}
