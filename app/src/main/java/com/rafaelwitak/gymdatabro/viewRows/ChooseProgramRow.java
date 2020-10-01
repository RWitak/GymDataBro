package com.rafaelwitak.gymdatabro.viewRows;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rafaelwitak.gymdatabro.R;
import com.rafaelwitak.gymdatabro.activities.WorkoutStepActivity;

public class ChooseProgramRow extends androidx.appcompat.widget.AppCompatTextView {

    private static int defStyleAttr = R.attr.chooseProgramRowStyle;

    public ChooseProgramRow(@NonNull Context context) {
        this(context, null, defStyleAttr);
    }

    public ChooseProgramRow(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, defStyleAttr);
    }

    public ChooseProgramRow(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupOnClickStartActivity(context);
    }


    private void setupOnClickStartActivity(@NonNull Context context) {
        this.setOnClickListener(view -> {
            Intent intent = new Intent(context, WorkoutStepActivity.class);
            context.startActivity(intent);
        });
    }

    public void setTextViewText(String text) {
        setText(text);
    }
}
