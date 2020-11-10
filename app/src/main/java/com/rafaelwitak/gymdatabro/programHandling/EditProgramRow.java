package com.rafaelwitak.gymdatabro.programHandling;

import android.widget.EditText;

import com.rafaelwitak.gymdatabro.EditRow;
import com.rafaelwitak.gymdatabro.EditRowTextGetter;
import com.rafaelwitak.gymdatabro.database.Program;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditProgramBinding;

public abstract class EditProgramRow
        extends EditRow
        implements EditRowTextGetter<ActivityEditProgramBinding> {

    protected ActivityEditProgramBinding binding;

    public EditProgramRow(com.rafaelwitak.gymdatabro.databinding.ActivityEditProgramBinding binding) {
        super();
        this.binding = binding;
        this.editText = getEditText(binding);
    }

    @Override
    public abstract EditText getEditText(ActivityEditProgramBinding binding);

    public abstract void setPreFilledText(Program program);
}
