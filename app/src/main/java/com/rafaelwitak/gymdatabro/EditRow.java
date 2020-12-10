package com.rafaelwitak.gymdatabro;

import android.text.Editable;
import android.widget.EditText;

public abstract class EditRow {
    protected EditText editText;

    public Editable getEditTextValue() {
        return editText.getText();
    }

    public String getEditTextValueAsString() {
        if (getEditTextValue().toString().isEmpty()) {
            return ""; // FIXME
        }
        return getEditTextValue().toString().trim();
    }

    public void showErrorText(CharSequence error) {
        this.editText.setError(error);
        this.editText.requestFocus();
    }

}
