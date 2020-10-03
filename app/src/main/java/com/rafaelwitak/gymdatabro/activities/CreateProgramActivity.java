package com.rafaelwitak.gymdatabro.activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.Program;
import com.rafaelwitak.gymdatabro.databinding.ActivityCreateProgramBinding;
import com.rafaelwitak.gymdatabro.viewRows.CreateProgramRowHolder;

public class CreateProgramActivity extends AppCompatActivity {

    private ActivityCreateProgramBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateProgramBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupViews();

    }

    private void setupViews() {
        setupCreateButton();
    }

    private void setupCreateButton() {
        Button createButton = binding.createProgramButtonCreate;
        createButton.setOnClickListener(view -> saveProgramToDatabase());
    }

    private void saveProgramToDatabase() {
        GymBroDatabase database = MainActivity.database;
        database.programDAO().insertProgram(getProgram());
    }

    private Program getProgram() {
        CreateProgramRowHolder holder = new CreateProgramRowHolder(binding);
        return holder.getProgram();
    }
}