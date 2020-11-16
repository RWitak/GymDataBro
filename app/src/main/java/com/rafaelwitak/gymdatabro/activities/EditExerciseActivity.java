package com.rafaelwitak.gymdatabro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rafaelwitak.gymdatabro.database.Exercise;
import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditExerciseBinding;
import com.rafaelwitak.gymdatabro.exerciseHandling.ExerciseSanityChecker;
import com.rafaelwitak.gymdatabro.exerciseHandling.ExerciseSaveHandler;

import java.util.HashMap;

import static com.rafaelwitak.gymdatabro.EditTextHelper.getTextAsNullableFloat;
import static com.rafaelwitak.gymdatabro.EditTextHelper.getTextAsTrimmedStringOrNull;
import static com.rafaelwitak.gymdatabro.StringHelper.getNonNullStringFromNumber;
import static com.rafaelwitak.gymdatabro.StringHelper.getNonNullStringFromString;

public class EditExerciseActivity extends AppCompatActivity {

    private GymBroDatabase database;
    private ActivityEditExerciseBinding binding;
    private Exercise exercise;
    private boolean isExistingExercise;
    private HashMap<String, EditText> editTexts;
    private ExerciseSaveHandler saveHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.database = GymBroDatabase.getDatabase(this);

        Intent intent = getIntent();
        this.isExistingExercise = getIsExistingExercise(intent);
        this.exercise = getExercise(this.isExistingExercise);

        this.binding = ActivityEditExerciseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.editTexts = getEditTexts(this.binding);
        setViews();

        this.saveHandler =
                new ExerciseSaveHandler(this,
                        this.database.exerciseDAO(),
                        this.exercise,
                        this.isExistingExercise);
    }

    private boolean getIsExistingExercise(Intent intent) {
        return intent.hasExtra("exerciseId");
    }

    private Exercise getExercise(boolean isExistingExercise) {
        if (isExistingExercise) {
            return getExerciseFromIntent(getIntent());
        }
        return new Exercise();
    }

    private Exercise getExerciseFromIntent(Intent intent) {
        int exerciseId = intent.getIntExtra("ExerciseId", -1);
        return database.exerciseDAO().getExerciseByID(exerciseId);
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


    private void setViews() {
        setupToolbar(binding.editExerciseToolbar.getRoot());
        setupEditTexts(this.editTexts, this.exercise);
        setupEditButton();
    }

    private void setupToolbar(Toolbar toolbar) {
        toolbar.setTitle(isExistingExercise ? "Edit Exercise" : "Create Exercise");
    }

    private void setupEditTexts(
            HashMap<String, EditText> editTexts,
            Exercise exercise) {

        editTexts.get("Name").setText(getNonNullStringFromString(exercise.name));
        editTexts.get("PR").setText(getNonNullStringFromNumber(exercise.pr));
        editTexts.get("Cues").setText(getNonNullStringFromString(exercise.cues));
        editTexts.get("Links").setText(getNonNullStringFromString(exercise.links));
        editTexts.get("Equipment").setText(getNonNullStringFromString(exercise.equipment));
    }

    private void setupEditButton() {
        Button button = binding.editExerciseButtonEdit;
        button.setOnClickListener(v -> tryToSaveAndExit());
    }


    private void tryToSaveAndExit() {
        Exercise updatedExercise = updateExerciseFromEditTexts(this.exercise, this.editTexts);

        int sanityStatus = ExerciseSanityChecker.getStatus(updatedExercise, this.database.exerciseDAO());

        if (sanityStatus == ExerciseSanityChecker.Status.SAVABLE) {
            this.saveHandler.saveAndFinish();
        }
        else if (ExerciseSanityChecker.Status.isBadName(sanityStatus)) {
            this.editTexts.get("Name").setError("Please choose a unique and meaningful name.");
        }
        else {
            Log.e("GDB",
                    "SanityCheck Error (Exercise): Status code '"
                            + sanityStatus
                            + "' could not be handled.");
        }
    }

    private Exercise updateExerciseFromEditTexts(
            Exercise exercise,
            HashMap<String, EditText> editTexts) {

        exercise.name = getTextAsTrimmedStringOrNull(editTexts.get("Name"));
        exercise.pr = getTextAsNullableFloat(editTexts.get("PR"));
        exercise.cues = getTextAsTrimmedStringOrNull(editTexts.get("Cues"));
        exercise.links = getTextAsTrimmedStringOrNull(editTexts.get("Links"));
        exercise.equipment = getTextAsTrimmedStringOrNull(editTexts.get("Equipment"));

        return exercise;
    }
}