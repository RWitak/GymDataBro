package com.rafaelwitak.gymdatabro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import androidx.annotation.NonNull;
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
import java.util.Objects;

import static com.rafaelwitak.gymdatabro.EditTextHelper.getTextAsNullableFloat;
import static com.rafaelwitak.gymdatabro.EditTextHelper.getTextAsNullableInteger;
import static com.rafaelwitak.gymdatabro.EditTextHelper.getTextAsTrimmedStringOrNull;
import static com.rafaelwitak.gymdatabro.StringHelper.getNonNullString;

public class EditWorkoutStepActivity extends AppCompatActivity {

    private final HashMap<String, EditText> editTextMap = new HashMap<>();
    private GymBroDatabase database;
    private ActivityEditWorkoutStepBinding binding;
    private WorkoutStep workoutStep;
    private boolean isExistingWorkoutStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.database = GymBroDatabase.getDatabase(this);

        this.isExistingWorkoutStep = getIsExistingWorkoutStep(getIntent());
        this.workoutStep = getWorkoutStep(this.isExistingWorkoutStep);

        this.binding = ActivityEditWorkoutStepBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setEditTextMap();
        setViews();
    }

    private boolean getIsExistingWorkoutStep(@NonNull Intent intent) {
        return intent.hasExtra("workoutId") && intent.hasExtra("stepNumber");
    }

    private WorkoutStep getWorkoutStep(boolean isExistingWorkoutStep) {
        if (isExistingWorkoutStep) {
            return getWorkoutStepFromIntent(getIntent());
        }
        return new WorkoutStep();
    }

    private WorkoutStep getWorkoutStepFromIntent(@NonNull Intent intent) {
        int workoutId = intent.getIntExtra("workoutId", -1);
        int stepNumber = intent.getIntExtra("stepNumber", -1);
        return database.workoutStepDAO().getWorkoutStepSynchronously(workoutId, stepNumber);
    }


    private void setEditTextMap() {
        editTextMap.put("Name", binding.editWorkoutStepNameEdit);
        editTextMap.put("WorkoutID", binding.editWorkoutStepWorkoutIdEdit);
//        editTextMap.put("Number", binding.editWorkoutStepNumberEdit); // obsolete atm
        editTextMap.put("ExerciseID", binding.editWorkoutStepExerciseIdEdit);
        editTextMap.put("Reps", binding.editWorkoutStepRepsEdit);
        editTextMap.put("Weight", binding.editWorkoutStepWeightEdit);
        editTextMap.put("RPE", binding.editWorkoutStepRpeEdit);
        editTextMap.put("Duration", binding.editWorkoutStepDurationEdit);
        editTextMap.put("Rest", binding.editWorkoutStepRestEdit);
        editTextMap.put("Details", binding.editWorkoutStepDetailsEdit);
        editTextMap.put("Notes", binding.editWorkoutStepNotesEdit);
    }


    private void setViews() {
        setupToolbar(binding.editWorkoutStepToolbar.getRoot());
        setupNumberSetsPicker(binding.editWorkoutStepNumberSetsPicker);
        setupEditTexts();
        setupSaveButton();

        new SpinnerManager().setup();
    }

    private void setupToolbar(@NonNull Toolbar toolbar) {
        toolbar.setTitle(isExistingWorkoutStep ? "Edit WorkoutStep" : "Create WorkoutStep");
    }

    private void setupNumberSetsPicker(@NonNull NumberPicker numberPicker) {
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(100);
        numberPicker.setValue(getNumberPickerValueFromIntent());
    }

    private int getNumberPickerValueFromIntent() {
        return getIntent().getIntExtra("NumberSets", 1);
    }


    private void setupEditTexts() {
        setTextsSafely("Name", workoutStep.name);
        setTextsSafely("WorkoutID", workoutStep.workoutID);
//        setTextsSafely("Number", workoutStep.number);  // obsolete atm
        setTextsSafely("ExerciseID", workoutStep.exerciseID);
        setTextsSafely("Reps", workoutStep.reps);
        setTextsSafely("Weight", workoutStep.weight);
        setTextsSafely("RPE", workoutStep.rpe);
        setTextsSafely("Duration", workoutStep.durationSeconds);
        setTextsSafely("Rest", workoutStep.restSeconds);
        setTextsSafely("Details", workoutStep.details);
        setTextsSafely("Notes", workoutStep.notes);
    }

    public void setTextsSafely(String key, String text) {
        String sanitizedText = getNonNullString(text);
        getEditTextSafely(key).setText(sanitizedText);
    }

    public void setTextsSafely(String key, Number value) {
        String sanitizedText = getNonNullString(value);
        getEditTextSafely(key).setText(sanitizedText);
    }


    private void setupSaveButton() {
        Button button = binding.editWorkoutStepButtonSave;
        button.setOnClickListener(v -> tryToSaveAndFinish());
    }

    private void tryToSaveAndFinish() {
        updateWorkoutStepFromEditTexts();

        int sanityStatus = getSanityStatus();

        if (sanityStatus == WorkoutStepSanityChecker.Status.SAVABLE) {
            saveAndFinish();
        } else {
            handleSanityStatusErrors(sanityStatus);
        }
    }

    private void updateWorkoutStepFromEditTexts() {

        workoutStep.workoutID = getNonNullIntFromEditText("WorkoutID");
        workoutStep.exerciseID = getNonNullIntFromEditText("ExerciseID");

        workoutStep.name = getTextAsTrimmedStringOrNull(getEditTextSafely("Name"));
// workoutStep.number = getTextAsNullableInteger(getEditTextSafely("Number")); // obsolete atm
        workoutStep.reps = getTextAsNullableInteger(getEditTextSafely("Reps"));
        workoutStep.weight = getTextAsNullableFloat(getEditTextSafely("Weight"));
        workoutStep.rpe = getTextAsNullableFloat(getEditTextSafely("RPE"));
        workoutStep.durationSeconds = getTextAsNullableInteger(getEditTextSafely("Duration"));
        workoutStep.restSeconds = getTextAsNullableInteger(getEditTextSafely("Rest"));
        workoutStep.details = getTextAsTrimmedStringOrNull(getEditTextSafely("Details"));
        workoutStep.notes = getTextAsTrimmedStringOrNull(getEditTextSafely("Notes"));

        if (!isExistingWorkoutStep) {
            workoutStep.number = getNextWorkoutStepNumber();
        }
    }

    private void saveAndFinish() {
        WorkoutStepSaveHandler saveHandler = getSaveHandler();
        int numberSets = getNumberSets();

        if (numberSets > 1) {
            saveHandler.saveMultipleAndFinish(numberSets);
        }
        getSaveHandler().saveAndFinish();
    }

    private int getNumberSets() {
        return binding.editWorkoutStepNumberSetsPicker.getValue();
    }

    private int getSanityStatus() {
        return WorkoutStepSanityChecker.getStatus(
                    workoutStep,
                    database.workoutStepDAO(),
                    database.exerciseDAO(),
                    isExistingWorkoutStep);
    }

    @NonNull
    private WorkoutStepSaveHandler getSaveHandler() {
        return new WorkoutStepSaveHandler(
                this,
                this.database.workoutStepDAO(),
                this.workoutStep,
                this.isExistingWorkoutStep);
    }

    private int getNonNullIntFromEditText(String editTextKey) {
        return Integer.parseInt(getEditTextSafely(editTextKey).getText().toString());
    }

    @NonNull
    private EditText getEditTextSafely(String key) {
        return Objects.requireNonNull(editTextMap.get(key));
    }

    private int getNextWorkoutStepNumber() {
        return database.workoutStepDAO()
                .getAllStepsForWorkoutSynchronously(workoutStep.workoutID)
                .size();
    }

    private void handleSanityStatusErrors(int sanityStatus) {
        if (WorkoutStepSanityChecker.Status.isBadName(sanityStatus)) {
            getEditTextSafely("Name")
                    .setError("Please choose a meaningful name or leave empty.");
        }
        else if (WorkoutStepSanityChecker.Status.isBadExerciseId(sanityStatus)) {
            getEditTextSafely("ExerciseID")
                    .setError("Please choose an existing ExerciseID.");
        }
        else if (WorkoutStepSanityChecker.Status.isBadRpe(sanityStatus)) {
            getEditTextSafely("RPE")
                    .setError("RPE must be a positive number up to 10.");
        }
        else if (WorkoutStepSanityChecker.Status.isBadNumber(sanityStatus)) {
            throw new RuntimeException("SanityCheck Error (WorkoutStep): " +
                    "WorkoutStep number " + workoutStep.number + " is illegal.");
        }
        else {
            throw new RuntimeException("SanityCheck Error (WorkoutStep): Status code '"
                    + sanityStatus
                    + "' could not be handled.");
        }
    }


    private class SpinnerManager {

        public void setup() {

            Spinner workoutIdSpinner = binding.editWorkoutStepWorkoutIdSpinner;
            Spinner exerciseIdSpinner = binding.editWorkoutStepExerciseIdSpinner;

            setupSpinner(workoutIdSpinner, getWorkoutNamesArray(), getWorkoutListener());
            setupSpinner(exerciseIdSpinner, getExerciseNamesArray(), getExerciseListener());
        }

        private void setupSpinner(@NonNull Spinner spinner,
                                  ArrayList<CharSequence> arrayList,
                                  AdapterView.OnItemSelectedListener listener) {

            ArrayAdapter<CharSequence> spinnerAdapter =
                    new ArrayAdapter<>(getBaseContext(),
                            android.R.layout.simple_spinner_item,
                            arrayList);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(spinnerAdapter);
            spinner.setOnItemSelectedListener(listener);
        }

        @NonNull
        private AdapterView.OnItemSelectedListener getWorkoutListener() {
            return new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(@NonNull AdapterView<?> adapterView, View view, int i, long l) {
                    Workout workout = database.workoutDAO().getWorkoutByName(
                            (String) adapterView.getItemAtPosition(i));
                    binding.editWorkoutStepWorkoutIdEdit.setText(String.valueOf(workout.id));
                }

                @Override
                public void onNothingSelected(@NonNull AdapterView<?> adapterView) {
                    adapterView.setSelection(0);
                }
            };
        }

        @NonNull
        private ArrayList<CharSequence> getWorkoutNamesArray() {
            ArrayList<CharSequence> workoutNames = new ArrayList<>();

            for (Workout workout : database.workoutDAO().getAllWorkouts()) {
                workoutNames.add(workout.name);
            }
            return workoutNames;
        }


        @NonNull
        private AdapterView.OnItemSelectedListener getExerciseListener() {
            return new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(@NonNull AdapterView<?> adapterView, View view, int i, long l) {
                    Exercise exercise = database.exerciseDAO().getExercisesByName(
                            adapterView.getItemAtPosition(i).toString()).get(0);
                    binding.editWorkoutStepExerciseIdEdit.setText(String.valueOf(exercise.id));
                }

                @Override
                public void onNothingSelected(@NonNull AdapterView<?> adapterView) {
                    adapterView.setSelection(0);
                }
            };
        }

        @NonNull
        private ArrayList<CharSequence> getExerciseNamesArray() {
            ArrayList<CharSequence> exerciseNames = new ArrayList<>();

            for (Exercise exercise : database.exerciseDAO().getAllExercises()) {
                exerciseNames.add(exercise.name);
            }
            return exerciseNames;
        }
    }
}