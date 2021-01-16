/*
 * Copyright (c) 2020, Rafael Witak.
 */

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
            // TODO: 16.01.2021 Find out why this needs fixing.
        }
        return getEditTextValue().toString().trim();
    }

    public void showErrorText(CharSequence error) {
        this.editText.setError(error);
        this.editText.requestFocus();
    }
}
