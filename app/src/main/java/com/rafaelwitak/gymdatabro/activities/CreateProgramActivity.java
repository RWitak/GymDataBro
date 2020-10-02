package com.rafaelwitak.gymdatabro.activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.Program;
import com.rafaelwitak.gymdatabro.databinding.ActivityCreateProgramBinding;

public class CreateProgramActivity extends AppCompatActivity {

    private ActivityCreateProgramBinding binding;
    private Program program;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateProgramBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupViews();

        program = new Program();

    }

    private void setupViews() {
        setupEditViews();
        setupCreateButton();
    }

    private void setupCreateButton() {
        Button createButton = binding.createProgramButtonCreate;
        createButton.setOnClickListener(view -> saveProgramToDatabase());
    }

    private void saveProgramToDatabase() {
        GymBroDatabase database = MainActivity.database;
        try {
            database.programDAO().insertProgram(program);
        }
        catch (Exception e) {
            throw e;
            //TODO make good catch
        }
    }

    private void setupEditViews() {
        //TODO, but maybe unnecessary.
    }
}