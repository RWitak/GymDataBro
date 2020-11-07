package com.rafaelwitak.gymdatabro;

import android.widget.EditText;

public abstract class EditRow {
    protected EditText editText;

    public EditRow() {
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

}
