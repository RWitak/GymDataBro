package com.rafaelwitak.gymdatabro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.Program;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditProgramBinding;
import com.rafaelwitak.gymdatabro.programHandling.EditProgramRowHolder;

import java.util.List;

public class EditProgramActivity extends AppCompatActivity {

    private GymBroDatabase database;
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
        this.database = GymBroDatabase.getDatabase(this);

        binding = ActivityEditProgramBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.holder = getEditProgramRowHolder();
        setupViews();

    }


    private Program getProgramByID(int programID) {
        if (isNewProgram) {
            return new Program();
        }
        return database.programDAO().getProgramByID(programID);
    }

    private EditProgramRowHolder getEditProgramRowHolder() {
        return new EditProgramRowHolder(this, binding, program);
    }

    private Program getProgramFromRowHolder() {
        return this.holder.getProgram();
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
        return currentProgram.name.length() > 2;
    }

    private boolean isUniqueProgramName(Program currentProgram) {
        String name = currentProgram.name;
        List<Program> programs = database.programDAO().getAllPrograms();

        for ( Program program : programs) {
            if (name.equalsIgnoreCase(program.name)) {
                if (currentProgram.id != program.id) {
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
        database.programDAO().insertProgram(program);
    }
}