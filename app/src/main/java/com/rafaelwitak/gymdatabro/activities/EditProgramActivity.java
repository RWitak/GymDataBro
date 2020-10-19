package com.rafaelwitak.gymdatabro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.Program;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditProgramBinding;
import com.rafaelwitak.gymdatabro.viewRows.EditProgramRowHolder;

public class EditProgramActivity extends AppCompatActivity {

    private ActivityEditProgramBinding binding;
    private Program program;
    private boolean isNewProgram;
    private EditProgramRowHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        return MainActivity.database.programDAO().getProgramByID(programID);
    }

    private EditProgramRowHolder getEditProgramRowHolder() {
        return new EditProgramRowHolder(binding, program);
    }

    private void setupViews() {
        setupToolbar();
        setupRows();
        setupEditButton();
    }

    private void setupRows() {
        if (!isNewProgram) {
            this.holder.setupRowTexts(this.program);
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.editProgramToolbar.getRoot();
        setSupportActionBar(toolbar);
        toolbar.setTitle(isNewProgram ? "Create Program" : "Edit Program");
    }

    private void setupEditButton() {
        Button editButton = binding.editProgramButtonEdit;
        editButton.setOnClickListener(view -> saveAndReturn());
    }

    private void saveAndReturn() {
        saveProgramToDatabase();

        Intent intent = new Intent(this, ChooseProgramActivity.class);
        startActivity(intent);
    }

    private void saveProgramToDatabase() {
        GymBroDatabase database = MainActivity.database;
        database.programDAO().insertProgram(getProgramFromRowHolder());
    }

    private Program getProgramFromRowHolder() {
        return this.holder.getProgram();
    }
}