/*
 * Copyright (c) 2021, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.rafaelwitak.gymdatabro.R;

import static androidx.core.content.ContextCompat.getColor;

public class DeletionWarningDialogFragment extends DialogFragment {
    WarningDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (WarningDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement WarningDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage(R.string.delete_warning_message)
                .setTitle(R.string.delete_warning_title)
                .setPositiveButton(R.string.delete, (dialog, which)
                        -> listener.onPositiveClick(DeletionWarningDialogFragment.this))
                .setNegativeButton(R.string.cancel, (dialog, which)
                        -> listener.onNegativeClick(DeletionWarningDialogFragment.this));

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface
                -> dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(getColor(requireContext(), R.color.colorBad)));

        return dialog;
    }

    public interface WarningDialogListener {
        void onPositiveClick(DialogFragment dialog);
        void onNegativeClick(DialogFragment dialog);
    }
}


























































