package com.rafaelwitak.gymdatabro.viewRows;

import android.widget.EditText;

import com.rafaelwitak.gymdatabro.database.Program;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditProgramBinding;

public abstract class EditProgramRow {
    protected ActivityEditProgramBinding binding;
    protected EditText editText;

    public EditProgramRow(com.rafaelwitak.gymdatabro.databinding.ActivityEditProgramBinding binding) {
        this.binding = binding;
        this.editText = getEditText();
    }

    public Object getEditTextValue() {
        return editText.getText();
    }

    public String getEditTextValueAsString() {
        if (getEditTextValue().toString().isEmpty()) {
            return "";
        }
        return getEditTextValue().toString().trim();
    }

    protected abstract EditText getEditText();

    public void showErrorText(CharSequence error) {
        this.editText.setError(error);
        this.editText.requestFocus();
    }

    public abstract void setPreFilledText(Program program);
}
