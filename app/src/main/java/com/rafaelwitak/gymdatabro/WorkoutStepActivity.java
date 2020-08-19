package com.rafaelwitak.gymdatabro;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
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


        binding.stepRepsPrescribed.setText(currentWorkoutStep.reps);
        binding.stepWeightPrescribed.setText(String.valueOf(currentWorkoutStep.weight));
        binding.stepRpePrescribed.setText(String.valueOf(currentWorkoutStep.rpe));
        binding.stepDurationPrescribed.setText(currentWorkoutStep.durationSeconds);
        binding.stepRestPrescribed.setText(String.valueOf(currentWorkoutStep.restSeconds));
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