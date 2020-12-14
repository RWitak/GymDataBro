package com.rafaelwitak.gymdatabro;

import androidx.annotation.NonNull;

public abstract class ConvertToFourths {
    @NonNull
    public static Number convertToFourthsPrecision(@NonNull Number number) {
        return ((float) Math.round(number.floatValue() * 4)) / 4f;
    }
}