package com.rafaelwitak.gymdatabro.activities;

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
    private int programID;
    private boolean isNewProgram;
    private EditProgramRowHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        programID = getIntent().getIntExtra("programID", -1);
        isNewProgram = (programID == -1);

        binding = ActivityEditProgramBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.holder = new EditProgramRowHolder(binding);
        setupViews();

    }

    private void setupViews() {
        setupToolbar();
        setupRows();
        setupEditButton();
    }

    private void setupRows() {
        if (!isNewProgram) {
            this.holder.setupRowTexts(MainActivity.database.programDAO().getProgramByID(programID));
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.editProgramToolbar.getRoot();
        setSupportActionBar(toolbar);
        toolbar.setTitle(isNewProgram ? "Create Program" : "Edit Program");
    }

    private void setupEditButton() {
        Button editButton = binding.editProgramButtonEdit;
        editButton.setOnClickListener(view -> saveProgramToDatabase());
    }

    private void saveProgramToDatabase() {
        GymBroDatabase database = MainActivity.database;
        database.programDAO().insertProgram(getProgram());
    }

    private Program getProgram() {
        return this.holder.getProgram();
    }
}