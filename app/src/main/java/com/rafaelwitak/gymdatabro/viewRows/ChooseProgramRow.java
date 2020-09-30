package com.rafaelwitak.gymdatabro.viewRows;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.rafaelwitak.gymdatabro.R;
import com.rafaelwitak.gymdatabro.activities.WorkoutStepActivity;

public class ChooseProgramRow extends androidx.appcompat.widget.AppCompatTextView {

    public ChooseProgramRow(@NonNull Context context, String text) {
        super(context);

        this.setTextViewText(text);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WorkoutStepActivity.class);
                context.startActivity(intent);
            }
        });
    }

    public ChooseProgramRow(Context context) {
        super(context);
    }

    public ChooseProgramRow(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }


    private void setTextViewText(String text) {
        setText(text);
    }
}
