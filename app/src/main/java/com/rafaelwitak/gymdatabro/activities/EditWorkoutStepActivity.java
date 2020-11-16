package com.rafaelwitak.gymdatabro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rafaelwitak.gymdatabro.database.Exercise;
import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.Workout;
import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditWorkoutStepBinding;
import com.rafaelwitak.gymdatabro.workoutStepHandling.workoutStepHandling.WorkoutStepSanityChecker;
import com.rafaelwitak.gymdatabro.workoutStepHandling.workoutStepHandling.WorkoutStepSaveHandler;

import java.util.ArrayList;
import java.util.HashMap;

import static com.rafaelwitak.gymdatabro.EditTextHelper.getTextAsNullableFloat;
import static com.rafaelwitak.gymdatabro.EditTextHelper.getTextAsNullableInteger;
import static com.rafaelwitak.gymdatabro.EditTextHelper.getTextAsTrimmedStringOrNull;
import static com.rafaelwitak.gymdatabro.StringHelper.getNonNullStringFromNumber;
import static com.rafaelwitak.gymdatabro.StringHelper.getNonNullStringFromString;

public class EditWorkoutStepActivity extends AppCompatActivity {

    private GymBroDatabase database;
    private ActivityEditWorkoutStepBinding binding;
    private WorkoutStep workoutStep;
    private boolean isExistingWorkoutStep;
    private HashMap<String, EditText> editTexts;
    private WorkoutStepSaveHandler saveHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.database = GymBroDatabase.getDatabase(this);

        Intent intent = getIntent();
        this.isExistingWorkoutStep = getIsExistingWorkoutStep(intent);
        this.workoutStep = getWorkoutStep(this.isExistingWorkoutStep);

        this.binding = ActivityEditWorkoutStepBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.editTexts = getEditTexts(this.binding);
        setViews();

        this.saveHandler =
                new WorkoutStepSaveHandler(this,
                        this.database.workoutStepDAO(),
                        this.workoutStep,
                        this.isExistingWorkoutStep);
    }

    private boolean getIsExistingWorkoutStep(Intent intent) {
        return intent.hasExtra("workoutId") && intent.hasExtra("stepNumber");
    }

    private WorkoutStep getWorkoutStep(boolean isExistingWorkoutStep) {
        if (isExistingWorkoutStep) {
            return getWorkoutStepFromIntent(getIntent());
        }
        return new WorkoutStep();
    }

    private WorkoutStep getWorkoutStepFromIntent(Intent intent) {
        int workoutId = intent.getIntExtra("workoutId", -1);
        int stepNumber = intent.getIntExtra("stepNumber", -1);
        return database.workoutStepDAO().getWorkoutStepSynchronously(workoutId, stepNumber);
    }

    private HashMap<String, EditText> getEditTexts(
            ActivityEditWorkoutStepBinding binding) {
        HashMap<String, EditText> map = new HashMap<>();

        map.put("Name", binding.editWorkoutStepNameEdit);
        map.put("WorkoutID", binding.editWorkoutStepWorkoutIdEdit);
        map.put("Number", binding.editWorkoutStepNumberEdit);
        map.put("ExerciseID", binding.editWorkoutStepExerciseIdEdit);
        map.put("Reps", binding.editWorkoutStepRepsEdit);
        map.put("Weight", binding.editWorkoutStepWeightEdit);
        map.put("RPE", binding.editWorkoutStepRpeEdit);
        map.put("Duration", binding.editWorkoutStepDurationEdit);
        map.put("Rest", binding.editWorkoutStepRestEdit);
        map.put("Details", binding.editWorkoutStepDetailsEdit);
        map.put("Notes", binding.editWorkoutStepNotesEdit);

        return map;
    }


    private void setViews() {
        setupToolbar(binding.editWorkoutStepToolbar.getRoot());
        setupEditTexts(this.editTexts, this.workoutStep);
        setupEditButton();

        new SpinnerManager().setup(); // FIXME: I'm ugly.
    }

    private void setupToolbar(Toolbar toolbar) {
        toolbar.setTitle(isExistingWorkoutStep ? "Edit WorkoutStep" : "Create WorkoutStep");
    }

    private void setupEditTexts(
            HashMap<String, EditText> editTexts,
            WorkoutStep workoutStep) {

        editTexts.get("Name").setText(getNonNullStringFromString(workoutStep.name));
        editTexts.get("WorkoutID").setText(getNonNullStringFromNumber(workoutStep.workoutID));
        editTexts.get("Number").setText(getNonNullStringFromNumber(workoutStep.number));
        editTexts.get("ExerciseID").setText(getNonNullStringFromNumber(workoutStep.exerciseID));
        editTexts.get("Reps").setText(getNonNullStringFromNumber(workoutStep.reps));
        editTexts.get("Weight").setText(getNonNullStringFromNumber(workoutStep.weight));
        editTexts.get("RPE").setText(getNonNullStringFromNumber(workoutStep.rpe));
        editTexts.get("Duration").setText(getNonNullStringFromNumber(workoutStep.durationSeconds));
        editTexts.get("Rest").setText(getNonNullStringFromNumber(workoutStep.restSeconds));
        editTexts.get("Details").setText(getNonNullStringFromString(workoutStep.details));
        editTexts.get("Notes").setText(getNonNullStringFromString(workoutStep.notes));
    }
    private void setupEditButton() {
        Button button = binding.editWorkoutStepButtonEdit;
        button.setOnClickListener(v -> tryToSaveAndExit());
    }


    private void tryToSaveAndExit() {
        WorkoutStep updatedWorkoutStep =
                updateWorkoutStepFromEditTexts(this.workoutStep, this.editTexts);

        int sanityStatus = WorkoutStepSanityChecker.getStatus(
                updatedWorkoutStep,
                this.database.workoutStepDAO(),
                this.database.exerciseDAO(),
                this.isExistingWorkoutStep);

        if (sanityStatus == WorkoutStepSanityChecker.Status.SAVABLE) {
            this.saveHandler.saveAndFinish();
        }

        else if (WorkoutStepSanityChecker.Status.isBadName(sanityStatus)) {
            this.editTexts.get("Name").setError("Please choose a meaningful name or leave empty.");
        }
        else if (WorkoutStepSanityChecker.Status.isBadExerciseId(sanityStatus)) {
            this.editTexts.get("ExerciseID").setError("Please choose an existing ExerciseID.");
        }
        else if (WorkoutStepSanityChecker.Status.isBadNumber(sanityStatus)) {
            this.editTexts.get("Number").setError("Please enter valid number.");
        }
        else if (WorkoutStepSanityChecker.Status.isBadRpe(sanityStatus)) {
            this.editTexts.get("RPE").setError("RPE must be a positive number up to 10.");
        }

        Log.e("GDB",
                "SanityCheck Error (WorkoutStep): Status code '"
                + sanityStatus
                + "' could not be handled.");
    }

    private WorkoutStep updateWorkoutStepFromEditTexts(
            WorkoutStep workoutStep,
            HashMap<String, EditText> editTexts) {

        workoutStep.name = getTextAsTrimmedStringOrNull(editTexts.get("Name"));
        workoutStep.workoutID = getTextAsNullableInteger(editTexts.get("PR"));
        workoutStep.number = getTextAsNullableInteger(editTexts.get("Cues"));
        workoutStep.exerciseID = getTextAsNullableInteger(editTexts.get("Links"));
        workoutStep.reps = getTextAsNullableInteger(editTexts.get("Equipment"));
        workoutStep.weight = getTextAsNullableFloat(editTexts.get("Equipment"));
        workoutStep.rpe = getTextAsNullableFloat(editTexts.get("Equipment"));
        workoutStep.durationSeconds = getTextAsNullableInteger(editTexts.get("Equipment"));
        workoutStep.restSeconds = getTextAsNullableInteger(editTexts.get("Equipment"));
        workoutStep.details = getTextAsTrimmedStringOrNull(editTexts.get("Equipment"));
        workoutStep.notes = getTextAsTrimmedStringOrNull(editTexts.get("Equipment"));

        return workoutStep;
    }


    private class SpinnerManager {

        public void setup() {
            setupAllSpinners(getSpinners(), database);
        }

        private void setupAllSpinners(HashMap<String, Spinner> spinners, GymBroDatabase database) {

            Spinner exerciseIdSpinner = spinners.get("ExerciseIdSpinner");
            Spinner workoutIdSpinner = spinners.get("WorkoutIdSpinner");

            setupExerciseSpinner(database, exerciseIdSpinner, getExerciseNamesArray(database));
            setupWorkoutIdSpinner(database, workoutIdSpinner, getWorkoutNamesArray(database));
        }

        private void setupWorkoutIdSpinner(GymBroDatabase database,
                                           Spinner spinner,
                                           ArrayList<String> workoutNames) {

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                    getBaseContext(),
                    android.R.layout.simple_spinner_item,
                    new ArrayList<>());
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(spinnerAdapter);
            spinner.setOnItemSelectedListener(getWorkoutListener(database));

            // As suggested by https://stackoverflow.com/a/61875814/12121416
            spinnerAdapter.addAll(workoutNames);
            spinnerAdapter.notifyDataSetChanged();
        }

        private AdapterView.OnItemSelectedListener getWorkoutListener(GymBroDatabase database) {
            return new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Workout workout = database.workoutDAO().getWorkoutByName(
                            (String) adapterView.getItemAtPosition(i));
                    binding.editWorkoutStepWorkoutIdEdit.setText(workout.name);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    adapterView.setSelection(0);
                }
            };
        }

        private ArrayList<String> getWorkoutNamesArray(GymBroDatabase database) {
            ArrayList<String> workoutNames = new ArrayList<>();

            for (Workout workout : database.workoutDAO().getAllWorkouts()) {
                workoutNames.add(workout.name);
            }
            return workoutNames;
        }


        private void setupExerciseSpinner(GymBroDatabase database,
                                          Spinner exerciseIdSpinner,
                                          ArrayList<CharSequence> exerciseNames) {

            ArrayAdapter<CharSequence> adapter =
                    new ArrayAdapter<>(getBaseContext(),
                            android.R.layout.simple_spinner_item,
                            exerciseNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // TODO: Not the way suggested by https://stackoverflow.com/a/61875814/12121416, still correct?

            exerciseIdSpinner.setAdapter(adapter);
            exerciseIdSpinner.setOnItemSelectedListener(getExerciseListener(database));
        }

        private AdapterView.OnItemSelectedListener getExerciseListener(GymBroDatabase database) {
            return new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Exercise exercise = database.exerciseDAO().getExercisesByName(
                            adapterView.getItemAtPosition(i).toString()).get(0);
                    binding.editWorkoutStepExerciseIdEdit.setText(exercise.id);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    adapterView.setSelection(0);
                }
            };
        }

        private ArrayList<CharSequence> getExerciseNamesArray(GymBroDatabase database) {
            ArrayList<CharSequence> names = new ArrayList<>();
            for (Exercise exercise : database.exerciseDAO().getAllExercises()) {
                names.add(exercise.name);
            }
            return names;
        }

        private HashMap<String, Spinner> getSpinners() {
            HashMap<String, Spinner> spinnerHashMap = new HashMap<>();

            spinnerHashMap.put(
                    "WorkoutIdSpinner",
                    (Spinner) binding.editWorkoutStepWorkoutIdSpinner);
            spinnerHashMap.put(
                    "ExerciseIdSpinner",
                    (Spinner) binding.editWorkoutStepExerciseIdSpinner);

            return spinnerHashMap;
        }
    }
}