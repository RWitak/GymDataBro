/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.rafaelwitak.gymdatabro.R;
import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.MasterDao;
import com.rafaelwitak.gymdatabro.database.Program;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditProgramBinding;
import com.rafaelwitak.gymdatabro.programHandling.EditProgramRowHolder;
import com.rafaelwitak.gymdatabro.util.DeletionWarningDialogFragment;

import java.util.List;

public class EditProgramActivity
        extends AppCompatActivity
        implements DeletionWarningDialogFragment.WarningDialogListener {

    private MasterDao dao;
    private ActivityEditProgramBinding binding;
    private Program program;
    private boolean isNewProgram;
    private EditProgramRowHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.dao = GymBroDatabase.getDatabase(this).masterDao();

        int programID = getIntent().getIntExtra("programID", -1);
        isNewProgram = (programID == -1);

        this.program = getProgramByID(programID);

        binding = ActivityEditProgramBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.holder = getEditProgramRowHolder();
        setupViews();
    }


    private Program getProgramByID(int programID) {
        if (isNewProgram) {
            return new Program();
        }
        return dao.getProgramByID(programID);
    }

    private EditProgramRowHolder getEditProgramRowHolder() {
        return new EditProgramRowHolder(dao, binding, program);
    }

    private Program getProgramFromRowHolder() {
        return this.holder.getProgram();
    }


    private void setupViews() {
        setupToolbar();
        setupRows();
        setupEditButton();
        setupDeleteButton();
        // TODO: 19.03.2021 only show this and Add Workout for existing programs.
        // TODO: 19.03.2021 for new programs show Cancel Button instead
    }

    private void setupDeleteButton() {
        Button deleteButton = binding.editProgramButtonDeleteProgram;
        deleteButton.setOnClickListener(v -> {
            DeletionWarningDialogFragment warning = new DeletionWarningDialogFragment();
            warning.show(getSupportFragmentManager(), "ProgramDeletionWarningDialogFragment");
        });
    }

    private void setupRows() {
        if (!isNewProgram) {
            this.holder.setupRowTexts(this.program);
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.editProgramToolbar.getRoot();
        toolbar.setTitle(isNewProgram
                ? getString(R.string.create_program)
                : getString(R.string.edit_program));
        setSupportActionBar(toolbar);
    }

    private void setupEditButton() {
        Button editButton = binding.editProgramButtonEdit;
        editButton.setOnClickListener(view -> {
            Program program = getProgramFromRowHolder();
            if (isSavableProgram(program)) {
                saveAndReturn(program);
            }
            else {
                holder.displayNameError("Enter a unique and meaningful name!");
            }
        });
    }


    private boolean isSavableProgram(Program program) {
        return (isValidProgramName(program) && isUniqueProgramName(program));
    }

    private boolean isValidProgramName(Program currentProgram) {
        return currentProgram.getName().length() > 2;
    }

    private boolean isUniqueProgramName(Program currentProgram) {
        String name = currentProgram.getName();
        List<Program> programs = dao.getAllPrograms();

        for ( Program program : programs) {
            if (name.equalsIgnoreCase(program.getName())) {
                if (currentProgram.getId() != program.getId()) {
                    return false;
                }
            }
        }

        return true;
    }



    private void saveAndReturn(Program program) {
        saveProgramToDatabase(program);

        Intent intent = new Intent(this, ChooseProgramActivity.class);
        startActivity(intent);
    }

    private void saveProgramToDatabase(Program program) {
        dao.insertProgram(program);
    }

    @Override
    public void onPositiveClick(DialogFragment dialog) {
        dao.deleteProgram(program);
        finish();
    }

    // TODO: 19.03.2021 Refactor whole file, beginning here
    @Override
    public void onNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }
}