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
    private boolean programNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        programNew = true;

        binding = ActivityEditProgramBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupViews();

    }

    private void setupViews() {
        setupToolbar();
        setupEditButton();
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.editProgramToolbar.getRoot();
        setSupportActionBar(toolbar);
        toolbar.setTitle(programNew ? "Create Program" : "Edit Program");
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
        EditProgramRowHolder holder = new EditProgramRowHolder(binding);
        return holder.getProgram();
    }
}