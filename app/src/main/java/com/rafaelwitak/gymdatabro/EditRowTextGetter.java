package com.rafaelwitak.gymdatabro;

import android.widget.EditText;

import androidx.viewbinding.ViewBinding;

public interface EditRowTextGetter<T extends ViewBinding> {
    EditText getEditText(T binding);
}