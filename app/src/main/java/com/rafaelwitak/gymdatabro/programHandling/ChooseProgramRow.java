/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.programHandling;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rafaelwitak.gymdatabro.R;

public class ChooseProgramRow extends androidx.appcompat.widget.AppCompatTextView {

    private static final int defStyleAttr = R.attr.chooseProgramRowStyle;

    public ChooseProgramRow(@NonNull Context context) {
        this(context, null, defStyleAttr);
    }

    public ChooseProgramRow(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, defStyleAttr);
    }

    public ChooseProgramRow(@NonNull Context context,
                            @Nullable AttributeSet attrs,
                            int defStyleAttr) {

        super(context, attrs, defStyleAttr);
    }


    public void setTextViewText(String text) {
        setText(text);
    }
}
