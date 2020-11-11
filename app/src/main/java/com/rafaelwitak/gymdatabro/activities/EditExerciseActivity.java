package com.rafaelwitak.gymdatabro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rafaelwitak.gymdatabro.database.Exercise;
import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditExerciseBinding;
import com.rafaelwitak.gymdatabro.exerciseHandling.EditExerciseRowHolder;

public class EditExerciseActivity extends AppCompatActivity {

    private GymBroDatabase database;
    private ActivityEditExerciseBinding binding;
    private EditExerciseRowHolder rowHolder;
    private Exercise exercise;
    private boolean isExistingExercise;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.intent = getIntent();
        this.database = GymBroDatabase.getDatabase(this);
        this.exercise = getExercise();
        this.isExistingExercise = getIsExistingExercise();

        this.binding = ActivityEditExerciseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.rowHolder = new EditExerciseRowHolder(this, this.binding, this.exercise);
        setViews();
    }

    private Exercise getExercise() {
        if (isExistingExercise) {
            return getExerciseFromIntent(getIntent());
        }
        return new Exercise();
    }

    private boolean getIsExistingExercise() {
        return intent.hasExtra("exerciseId");
    }


    private void setViews() {
        setupToolbar(binding.editExerciseToolbar.getRoot());
        rowHolder.setupRowTexts(this.exercise);
        setupEditButton();
    }

    private void setupToolbar(Toolbar toolbar) {
        toolbar.setTitle(isExistingExercise ? "Edit Exercise" : "Create Exercise");
    }

    private void setupEditButton() {
        Button button = binding.editExerciseButtonEdit;
        button.setOnClickListener(v -> saveChanges());
    }


    private void saveChanges() {
        Exercise editedExercise = rowHolder.getEditedExercise();

        trySavingExerciseToDb(editedExercise);
    }

    private void trySavingExerciseToDb(Exercise editedExercise) {
        try {
            saveExerciseToDatabase(editedExercise);
        } catch (Exception e) {
            Toast.makeText(
                    this,
                    "Can't write to database",
                    Toast.LENGTH_LONG)
                    .show();
            Log.e("GDB", "Can't write to database!\n" + e.toString());
        }
    }

    private void saveExerciseToDatabase(Exercise editedExercise) {
        if (isExistingExercise) {
            database.exerciseDAO().updateExercise(editedExercise);
        } else {
            database.exerciseDAO().insertNewExercise(editedExercise);
        }
    }

    private Exercise getExerciseFromIntent(Intent intent) {
        int exerciseId = intent.getIntExtra("ExerciseId", -1);
        return database.exerciseDAO().getExerciseByID(exerciseId);
    }
}