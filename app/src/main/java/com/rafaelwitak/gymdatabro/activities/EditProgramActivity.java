/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.annotation.NonNull;
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

        final int NEW_PROGRAM_ID = -1;
        int programID = getIntent().getIntExtra("programID", NEW_PROGRAM_ID);
        this.isNewProgram = (programID == NEW_PROGRAM_ID);
        this.program = getProgramByID(programID);

        this.binding = ActivityEditProgramBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.holder = new EditProgramRowHolder(dao, binding, program);
        setupViews();
    }


    private Program getProgramByID(int programID) {
        if (isNewProgram) {
            return new Program();
        }
        return dao.getProgramByID(programID);
    }

    private void setupViews() {
        setupToolbar();
        setupRows();
        setupButtons();
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.editProgramToolbar.getRoot();
        toolbar.setTitle(isNewProgram
                ? getString(R.string.create_program)
                : getString(R.string.edit_program));
        setSupportActionBar(toolbar);
    }

    private void setupRows() {
        if (!isNewProgram) {
            this.holder.setupRowTexts(this.program);
        }
    }

    private void setupButtons() {
        setupSaveButton();
        setupAddWorkoutButton();
        setupDeleteButton();
    }

    private void setupSaveButton() {
        Button editButton = binding.editProgramButtonSave;

        if (isNewProgram) {
            editButton.setText(R.string.create_exit);
        } else {
            editButton.setText(R.string.save_exit);
        }

        editButton.setOnClickListener(view -> {
            Program program = getProgramFromRowHolder();
            if (isSavableProgram(program)) {
                saveAndReturn(program);
            } else {
                holder.displayNameError(getString(R.string.unique_name_error));
            }
        });
    }

    private Program getProgramFromRowHolder() {
        return this.holder.getProgram();
    }

    private boolean isSavableProgram(@NonNull Program currentProgram) {
        String name = currentProgram.getName();
        if (name.length() < 3) {
            return false;
        }

        List<Program> programs = dao.getAllPrograms();
        for (Program program : programs) {
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
        finish();
    }

    private void saveProgramToDatabase(Program program) {
        dao.insertProgram(program);
    }

    private void setupAddWorkoutButton() {
        Button addWorkoutButton = binding.editProgramButtonAddWorkout;
        if (isNewProgram) {
            addWorkoutButton.setText(R.string.create_add_workout);
        } else {
            addWorkoutButton.setText(R.string.add_workout);
        }
        addWorkoutButton.setOnClickListener(view -> {
                Intent intent = new IntentMaker(this).getWorkoutListIntent(program);
                startActivity(intent);
        });
    }

    private void setupDeleteButton() {
        Button deleteButton = binding.editProgramButtonDeleteProgram;

        if (isNewProgram) {
            deleteButton.setText(R.string.cancel);
            deleteButton.setOnClickListener(v -> finish());
        } else {
            deleteButton.setOnClickListener(v ->
                    new DeletionWarningDialogFragment().show(
                            getSupportFragmentManager(),
                            "ProgramDeletionWarningDialogFragment"));
        }
    }

    @Override
    public void onPositiveClick(@NonNull DialogFragment dialog) {
        dao.deleteProgram(program);
        finish();
    }

    // TODO: 19.03.2021 Refactor whole file, beginning here
    @Override
    public void onNegativeClick(@NonNull DialogFragment dialog) {
        dialog.dismiss();
    }
}