package com.rafaelwitak.gymdatabro.viewRows;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rafaelwitak.gymdatabro.database.Program;
import com.rafaelwitak.gymdatabro.databinding.ActivityCreateProgramBinding;
//TODO work on logic
//TODO implement children
public abstract class CreateProgramRow {
    protected ActivityCreateProgramBinding binding;
    protected EditText editText;
    protected EditText.OnKeyListener onKeyListener;
    protected Context context;

    public CreateProgramRow(ActivityCreateProgramBinding binding, Context context) {
        editText = getEditTextFromBinding();
    }

    protected abstract EditText.OnKeyListener getOnKeyListener();

    protected abstract EditText getEditTextFromBinding();

    public abstract Program updateProgram(Program program);
}
