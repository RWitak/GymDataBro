/*
 * Copyright (c) 2021, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.activities;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import com.rafaelwitak.gymdatabro.R;
import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.MasterDao;
import com.rafaelwitak.gymdatabro.database.Workout;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditWorkoutBinding;
import com.rafaelwitak.gymdatabro.util.DeletionWarningDialogFragment;
import com.rafaelwitak.gymdatabro.workoutHandling.EditWorkoutRowHolder;

import java.util.List;

public class EditWorkoutActivity extends AppCompatActivity
        implements DeletionWarningDialogFragment.WarningDialogListener {
    public static final String PROGRAM_ID = "ProgramID";
    public static final String WORKOUT_ID = "WorkoutID";
    private Workout workout;
    private boolean isNewWorkout;
    private com.rafaelwitak.gymdatabro.databinding.ActivityEditWorkoutBinding binding;
    private MasterDao dao;
    private EditWorkoutRowHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dao = GymBroDatabase.getDatabase(getBaseContext()).masterDao();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workout);
        final int workoutId = getIntent().getIntExtra(WORKOUT_ID, -1);
        final int programId = getIntent().getIntExtra(PROGRAM_ID, -1);
        assert programId != -1;

        isNewWorkout = (workoutId == -1);
        if (isNewWorkout) {
            workout = new Workout();
            workout.setProgramID(programId);
        } else {
            workout = dao.getWorkoutByID(workoutId);
            assert workout != null;
        }

        binding = ActivityEditWorkoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        holder = new EditWorkoutRowHolder(binding, workout);
        setupViews();
    }

    private void setupViews() {
        setupToolbar();
        setupRows();
        setupButtons();
    }

    private void setupButtons() {
        setupSaveButton();
        setupDeleteButton();
    }

    private void setupDeleteButton() {
        final Button deleteButton = binding.editWorkoutButtonDeleteWorkout;

        if (isNewWorkout) {
            deleteButton.setText(R.string.cancel);
            deleteButton.setOnClickListener(v -> finish());
        } else {
            deleteButton.setOnClickListener(v ->
                    new DeletionWarningDialogFragment().show(
                            getSupportFragmentManager(),
                            "WorkoutDeletionWarningDialogFragment"));
        }
    }

    private void setupSaveButton() {
        final Button saveButton = binding.editWorkoutButtonSave;

        if (isNewWorkout) {
            saveButton.setText(R.string.create_exit);
        } else {
            saveButton.setText(R.string.save_exit);
        }

        saveButton.setOnClickListener(view -> {
            final Workout currentWorkout = holder.getWorkout();
            if (isSavableWorkout(currentWorkout)) {
                saveAndReturn(currentWorkout);
            } else {
                holder.displayNameError(getString(R.string.unique_name_error));
            }
        });

    }

    private boolean isSavableWorkout(Workout currentWorkout) {
        String currentName = currentWorkout.getName();
        if (currentName.length() < 3) {
            return false;
        }

        List<Workout> workouts = dao.getAllWorkouts();
        for (Workout workout : workouts) {
            if (currentName.equalsIgnoreCase(workout.getName())) {
                if (currentWorkout.getId() != workout.getId()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void saveAndReturn(Workout workout) {
        dao.insertWorkoutForId(workout);
        finish();
    }

    private void setupRows() {
        if (!isNewWorkout) {
            this.holder.setupRowTexts();
        }
    }

    private void setupToolbar() {
        final Toolbar toolbar = binding.editWorkoutToolbar.getRoot();

        toolbar.setTitle(isNewWorkout
                ? getString(R.string.create_workout)
                : getString(R.string.edit_workout));
        setSupportActionBar(toolbar);
    }

    @Override
    public void onPositiveClick(DialogFragment dialog) {
        dao.deleteWorkout(workout);
        finish();
    }

    @Override
    public void onNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }
}