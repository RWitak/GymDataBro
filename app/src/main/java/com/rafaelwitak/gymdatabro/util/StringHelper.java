/*
 * Copyright (c) 2021, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.util;

import androidx.annotation.NonNull;

public class StringHelper {

    @NonNull
    public static String getNonNullString(String string) {
        return (string == null) ? "" : string;
    }

    @NonNull
    public static String getNonNullString(Number number) {
        return (number == null) ? "" : String.valueOf(number);
    }
}
