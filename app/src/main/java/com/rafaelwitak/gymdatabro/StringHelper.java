/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro;

public class StringHelper {
    public static String getNonNullString(String string) {
        return (string == null) ? "" : string;
    }

    public static String getNonNullString(Number number) {
        return (number == null) ? "" : String.valueOf(number);
    }
}
