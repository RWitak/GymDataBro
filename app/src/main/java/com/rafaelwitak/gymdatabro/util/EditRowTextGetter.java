/*
 * Copyright (c) 2021, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.util;

import android.widget.EditText;

import androidx.viewbinding.ViewBinding;

public interface EditRowTextGetter<T extends ViewBinding> {
    EditText getEditText(T binding);
}
