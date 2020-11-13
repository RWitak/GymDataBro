package com.rafaelwitak.gymdatabro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rafaelwitak.gymdatabro.database.Exercise;
import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditExerciseBinding;

import java.util.HashMap;

public class EditExerciseActivity extends AppCompatActivity {

    private GymBroDatabase database;
    private ActivityEditExerciseBinding binding;
    private Exercise exercise;
    private boolean isExistingExercise;
    private Intent intent;
    private HashMap<String, EditText> editTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.intent = getIntent();
        this.database = GymBroDatabase.getDatabase(this);
        this.isExistingExercise = getIsExistingExercise();
        this.exercise = getExercise();

        this.binding = ActivityEditExerciseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.editTexts = getEditTexts(this.binding);
        setViews();
    }

    private HashMap<String, EditText> getEditTexts(
            ActivityEditExerciseBinding binding) {
        HashMap<String, EditText> map = new HashMap<>();

        map.put("Name", binding.editExerciseNameEdit);
        map.put("PR", binding.editExercisePrEdit);
        map.put("Cues", binding.editExerciseCuesEdit);
        map.put("Links", binding.editExerciseLinksEdit);
        map.put("Equipment", binding.editExerciseEquipmentEdit);

        return map;
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
        setupEditTexts(this.editTexts, this.exercise);
        setupEditButton();
    }

    private void setupEditTexts(
            HashMap<String, EditText> editTexts,
            Exercise exercise) {

        editTexts.get("Name").setText(getSanitizedStringFromString(exercise.name));
        editTexts.get("PR").setText(getSanitizedStringFromNumber(exercise.pr));
        editTexts.get("Cues").setText(getSanitizedStringFromString(exercise.cues));
        editTexts.get("Links").setText(getSanitizedStringFromString(exercise.links));
        editTexts.get("Equipment").setText(getSanitizedStringFromString(exercise.equipment));
    }

    private String getSanitizedStringFromString(String string) {
        return (string == null) ? "" : string;
    }

    public String getSanitizedStringFromNumber(Number number) {
        if (number == null) {
            return "";
        }
        return String.valueOf(number);
    }

    private void setupToolbar(Toolbar toolbar) {
        toolbar.setTitle(isExistingExercise ? "Edit Exercise" : "Create Exercise");
    }

    private void setupEditButton() {
        Button button = binding.editExerciseButtonEdit;
        button.setOnClickListener(v -> saveChanges());
    }


    private void saveChanges() {
        Exercise editedExercise = updateExerciseFromEditTexts(this.exercise, this.editTexts);
        trySavingExerciseToDb(editedExercise);
    }

    private Exercise updateExerciseFromEditTexts(
            Exercise exercise,
            HashMap<String, EditText> editTexts) {

        exercise.name = getTextAsString(editTexts.get("Name"));
        exercise.pr = getTextAsFloat(editTexts.get("PR"));
        exercise.cues = getTextAsString(editTexts.get("Cues"));        exercise.links = getTextAsString(editTexts.get("Links"));
        exercise.equipment = getTextAsString(editTexts.get("Equipment"));

        return exercise;
    }

    public String getTextAsString(EditText editText) {
        return editText.getText().toString();
    }

    public Integer getTextAsInteger(EditText editText) {
        return Integer.getInteger(getTextAsString(editText), null);
    }

    public Float getTextAsFloat(EditText editText) {
        try {
            return Float.parseFloat(getTextAsString(editText));
        } catch (NumberFormatException e) {
            return null;
        }
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