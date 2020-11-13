package com.rafaelwitak.gymdatabro;

public class StringHelper {
    public static String getNonNullStringFromString(String string) {
        return (string == null) ? "" : string;
    }

    public static String getNonNullStringFromNumber(Number number) {
        return (number == null) ? "" : String.valueOf(number);
    }
}
