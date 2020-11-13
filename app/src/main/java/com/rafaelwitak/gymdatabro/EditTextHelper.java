package com.rafaelwitak.gymdatabro;

import android.widget.EditText;

import androidx.annotation.NonNull;

public class EditTextHelper {

    public static String getTextAsTrimmedStringOrNull(@NonNull EditText editText) {
        String text = getTrimmedStringFromEditText(editText);
        return text.isEmpty() ? null : text;
    }

    public static String getTrimmedStringFromEditText(@NonNull EditText editText) {
        return editText.getText().toString().trim();
    }

    public static Integer getTextAsNullableInteger(@NonNull EditText editText) {
        return Integer.getInteger(getTrimmedStringFromEditText(editText), null);
    }

    public static Float getTextAsNullableFloat(@NonNull EditText editText) {
        try {
            return Float.parseFloat(getTrimmedStringFromEditText(editText));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
