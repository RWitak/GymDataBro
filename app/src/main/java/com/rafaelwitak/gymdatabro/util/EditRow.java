/*
 * Copyright (c) 2021, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.util;

import android.text.Editable;
import android.widget.EditText;

import androidx.annotation.NonNull;

public abstract class EditRow {
    protected EditText editText;

    private Editable getEditTextValue() {
        return editText.getText();
    }

    @NonNull
    public String getEditTextValueAsString() {
        return getEditTextValue().toString().trim();
    }

    public void showErrorText(CharSequence error) {
        this.editText.setError(error);
        this.editText.requestFocus();
    }
}
