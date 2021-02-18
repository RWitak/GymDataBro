/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rafaelwitak.gymdatabro.database.Exercise;
import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditExerciseBinding;
import com.rafaelwitak.gymdatabro.exerciseHandling.ExerciseSanityChecker;
import com.rafaelwitak.gymdatabro.exerciseHandling.ExerciseSaveHandler;
import com.rafaelwitak.gymdatabro.util.StringHelper;

import java.util.HashMap;
import java.util.Objects;

import static com.rafaelwitak.gymdatabro.util.EditTextHelper.getTextAsNullableFloat;
import static com.rafaelwitak.gymdatabro.util.EditTextHelper.getTextAsTrimmedStringOrNull;
import static com.rafaelwitak.gymdatabro.util.StringHelper.getNonNullString;

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

    private boolean getIsExistingExercise(@NonNull Intent intent) {
        return intent.hasExtra("exerciseId");
    }

    private Exercise getExercise(boolean isExistingExercise) {
        if (isExistingExercise) {
            return getExerciseFromIntent(getIntent());
        }
        return new Exercise();
    }

    private Exercise getExerciseFromIntent(@NonNull Intent intent) {
        int exerciseId = intent.getIntExtra("ExerciseId", -1);
        return database.exerciseDAO().getExerciseByID(exerciseId);
    }

    @NonNull
    private HashMap<String, EditText> getEditTexts(
            @NonNull ActivityEditExerciseBinding binding) {
        HashMap<String, EditText> map = new HashMap<>();

        map.put("Name", binding.editExerciseNameEdit);
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

    private void setupToolbar(@NonNull Toolbar toolbar) {
        toolbar.setTitle(isExistingExercise ? "Edit Exercise" : "Create Exercise");
    }

    private void setupEditTexts(
            @NonNull HashMap<String, EditText> editTexts,
            @NonNull Exercise exercise) {

        Objects.requireNonNull(editTexts.get("Name"))
                .setText(getNonNullString(exercise.getName()));
        Objects.requireNonNull(editTexts.get("PR"))
                .setText(StringHelper.getNonNullString(exercise.getPr()));
        Objects.requireNonNull(editTexts.get("Cues"))
                .setText(getNonNullString(exercise.getCues()));
        Objects.requireNonNull(editTexts.get("Links"))
                .setText(getNonNullString(exercise.getLinks()));
        Objects.requireNonNull(editTexts.get("Equipment"))
                .setText(getNonNullString(exercise.getEquipment()));
    }

    private void setupEditButton() {
        Button button = binding.editExerciseButtonEdit;
        button.setOnClickListener(v -> tryToSaveAndExit());
    }


    private void tryToSaveAndExit() {
        Exercise updatedExercise =
                updateExerciseFromEditTexts(this.exercise, this.editTexts);

        int sanityStatus =
                ExerciseSanityChecker
                        .getStatus(
                                updatedExercise,
                                this.database.exerciseDAO());

        if (sanityStatus == ExerciseSanityChecker.Status.SAVABLE) {
            this.saveHandler.saveAndFinish();
        }
        else if (ExerciseSanityChecker.Status.isBadName(sanityStatus)) {
            Objects.requireNonNull(this.editTexts.get("Name"))
                    .setError("Please choose a unique and meaningful name.");
        }
        else {
            Log.e("GDB",
                    "SanityCheck Error (Exercise): Status code '"
                            + sanityStatus
                            + "' could not be handled.");
        }
    }

    @NonNull
    private Exercise updateExerciseFromEditTexts(
            @NonNull Exercise exercise,
            @NonNull HashMap<String, EditText> editTexts) {

        exercise.setName(getTextAsTrimmedStringOrNull(
                Objects.requireNonNull(editTexts.get("Name"))));
        exercise.setPr(getTextAsNullableFloat(
                Objects.requireNonNull(editTexts.get("PR"))));
        exercise.setCues(getTextAsTrimmedStringOrNull(
                Objects.requireNonNull(editTexts.get("Cues"))));
        exercise.setLinks(getTextAsTrimmedStringOrNull(
                Objects.requireNonNull(editTexts.get("Links"))));
        exercise.setEquipment(getTextAsTrimmedStringOrNull(
                Objects.requireNonNull(editTexts.get("Equipment"))));

        return exercise;
    }
}