package com.rafaelwitak.gymdatabro;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.room.Database;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;

import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.database.WorkoutStepDAO;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

import java.util.List;

public class WorkoutStepActivity extends AppCompatActivity {

    private ActivityWorkoutStepBinding binding;
    private WorkoutStep currentWorkoutStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_step);
        database = ;
        currentWorkoutStep = getCurrentWorkoutStep();

        // bind all Views with IDs automatically
        binding = ActivityWorkoutStepBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    private WorkoutStep getCurrentWorkoutStep() {
        // TODO
        int stepNumber = getIntent().getIntExtra("nextStepNumber", 0);
        int workoutID = getIntent().getIntExtra("workoutID", 0);
        LiveData<WorkoutStep> step
        return step;
    }
}