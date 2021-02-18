/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.programHandling;

import android.widget.EditText;

import com.rafaelwitak.gymdatabro.database.Program;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditProgramBinding;
import com.rafaelwitak.gymdatabro.util.EditRow;
import com.rafaelwitak.gymdatabro.util.EditRowTextGetter;

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
