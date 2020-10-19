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
            return null;
        }
        return getEditTextValue().toString();
    }

    protected abstract EditText getEditText();

    public abstract void setPreFilledText(Program program);
}
