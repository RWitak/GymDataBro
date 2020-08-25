package com.rafaelwitak.gymdatabro;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;

import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

public class WorkoutStepActivity extends AppCompatActivity {

    private GymBroDatabase database;
    private ActivityWorkoutStepBinding binding;
    private WorkoutStep currentWorkoutStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_step);
        currentWorkoutStep = getCurrentWorkoutStep();
        database = MainActivity.database;

        // bind all Views with IDs automatically
        binding = ActivityWorkoutStepBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Toolbar toolbar = binding.toolbar.getRoot();
        setSupportActionBar(toolbar);

        toolbar.setTitle("Current Workout Name");
        toolbar.setSubtitle("Current Exercise");

        //TODO rebuild database and update schema in IDE50

        setupWorkoutStepViewRows();
    }

    private void setupWorkoutStepViewRows() {

        if (currentWorkoutStep.reps == null) {
            binding.stepRepsRow.setVisibility(View.GONE);
        }
        else {
            binding.stepRepsPrescribed.setText(currentWorkoutStep.reps);
            binding.stepRepsPerformed.setText(currentWorkoutStep.reps);
        }

        if (currentWorkoutStep.weight == null) {
            binding.stepWeightRow.setVisibility(View.GONE);
        }
        else {
            binding.stepWeightPrescribed.setText(String.valueOf(currentWorkoutStep.weight));
            binding.stepWeightPerformed.setText(String.valueOf(currentWorkoutStep.weight));
        }

        if (currentWorkoutStep.rpe == null) {
            binding.stepRpeRow.setVisibility(View.GONE);
        }
        else {
            binding.stepRpePrescribed.setText(String.valueOf(currentWorkoutStep.rpe));
            binding.stepRpePerformed.setText(String.valueOf(currentWorkoutStep.rpe));
        }

        if (currentWorkoutStep.durationSeconds == null) {
            binding.stepDurationRow.setVisibility(View.GONE);
        }
        else {
            binding.stepDurationPrescribed.setText(currentWorkoutStep.durationSeconds);
            binding.stepDurationPerformed.setText(currentWorkoutStep.durationSeconds);
        }

        if (currentWorkoutStep.restSeconds == null) {
            binding.stepRestRow.setVisibility(View.GONE);
        }
        else {
            binding.stepRestPrescribed.setText(String.valueOf(currentWorkoutStep.restSeconds));
            binding.stepRestPerformed.setText(String.valueOf(currentWorkoutStep.restSeconds));
        }
    }


    private WorkoutStep getCurrentWorkoutStep() {
        int workoutID = getIntent().getIntExtra("workoutID", 0);
        int stepNumber = getIntent().getIntExtra("nextStepNumber", 0);
        LiveData<WorkoutStep> step = database
                .workoutStepDAO()
                .getWorkoutStep(workoutID, stepNumber);
        return step.getValue();
    }
}