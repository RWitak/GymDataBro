package com.rafaelwitak.gymdatabro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rafaelwitak.gymdatabro.performanceSetHandling.PerformanceSetDataProviderHolder;
import com.rafaelwitak.gymdatabro.performanceSetHandling.PerformanceSetMaker;
import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.PerformanceSet;
import com.rafaelwitak.gymdatabro.database.Workout;
import com.rafaelwitak.gymdatabro.database.WorkoutStep;

import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

import com.rafaelwitak.gymdatabro.workoutStepHandling.WorkoutStepRow;
import com.rafaelwitak.gymdatabro.workoutStepHandling.WorkoutStepRowHolder;

import java.util.List;
import java.util.Objects;

public class WorkoutStepActivity extends AppCompatActivity {

    private GymBroDatabase database;
    private Workout currentWorkout;
    private WorkoutStep currentWorkoutStep;
    private ActivityWorkoutStepBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = MainActivity.database;
        currentWorkoutStep = getCurrentWorkoutStep();
        currentWorkout = getCurrentWorkout();

        // automatically bind all Views with IDs
        binding = ActivityWorkoutStepBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpViews();
    }


    private void setUpViews() {
        setUpToolbar();
        setUpWorkoutStepViewRows();
        setUpButton();
    }

    private void setUpToolbar() {
        Toolbar toolbar = binding.toolbar.getRoot();
        setSupportActionBar(toolbar);
        this.setTitle(getCurrentWorkoutName()); // otherwise toolbar always just displays app name

        String programName = getCurrentProgramName();
        if (programName != null) {
            toolbar.setSubtitle(programName);
        }
    }

    // Set visibility and/or data for the WorkoutStep's View's Rows
    private void setUpWorkoutStepViewRows() {
        WorkoutStepRowHolder rowHolder = new WorkoutStepRowHolder(binding, currentWorkoutStep);
        for ( WorkoutStepRow row : rowHolder.getRows() ) {
            row.setup();
        }
    }

    private void setUpButton() {
        binding.stepBtnNext.setOnClickListener(getViewOnClickListener());
    }


    private View.OnClickListener getViewOnClickListener() {
        //noinspection Convert2Lambda
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePerformanceSet(getPerformedSet());

                if (isLastWorkoutStep(currentWorkoutStep)){
                    finish();
                }
                else {
                    startNextWorkoutStep();
                }
            }
        };
    }

    private PerformanceSet getPerformedSet() {
        return PerformanceSetMaker.getPerformanceSet(getPerformanceSetDataProviderHolder());
    }

    private PerformanceSetDataProviderHolder getPerformanceSetDataProviderHolder() {
        return new PerformanceSetDataProviderHolder(this.binding, this.currentWorkoutStep);
    }

    public boolean isLastWorkoutStep(WorkoutStep currentWorkoutStep) {
        final List<WorkoutStep> workoutSteps =
                database
                        .workoutStepDAO()
                        .getAllStepsForWorkoutAsLiveData(currentWorkout.id)
                        .getValue();

        final int numberOfStepsInWorkout = Objects.requireNonNull(workoutSteps).size();

        return (currentWorkoutStep.number + 1 == numberOfStepsInWorkout);
    }


    private void savePerformanceSet(PerformanceSet performanceSet) {
        database.performanceSetDAO().insertSet(performanceSet);
    }

    private void startNextWorkoutStep() {
        Intent intent = getIntentWithExtras();

        startActivity(intent);
    }

    private Intent getIntentWithExtras() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("workoutID", currentWorkoutStep.workoutID);
        intent.putExtra("nextStepNumber", currentWorkoutStep.number + 1);

        return intent;
    }


    private WorkoutStep getCurrentWorkoutStep() {
        int workoutID = getIntent().getIntExtra("workoutID", 1);
        int stepNumber = getIntent().getIntExtra("nextStepNumber", 0);

        Log.d("GymDataBro", "Fetching Workout " +
                    + workoutID
                    + ", Step "
                    + stepNumber);

        WorkoutStep step = database
                .workoutStepDAO()
                .getWorkoutStepSynchronously(workoutID, stepNumber);

        if (step == null) {
            Log.w("GymDataBro",
                    "getCurrentWorkoutStep returned null, empty WorkoutStep created.");
            return new WorkoutStep();
        }

        Log.i("GymDataBro", "WorkoutStep fetched successfully.");

        return step;
    }

    private Workout getCurrentWorkout() {
        Workout currentWorkout = database.workoutDAO().getWorkoutByID(currentWorkoutStep.workoutID);

        if (currentWorkout == null) {
            return new Workout();
        }

        return currentWorkout;
    }


    private String getCurrentWorkoutName() {
        if (currentWorkout.name.isEmpty()) {
            return "Unnamed Workout";
        }
        return currentWorkout.name;
    }

    private String getCurrentProgramName() {
        Integer id = currentWorkout.programID;
        if (id != null) {
            return database.programDAO().getProgramByID(id).name;
        }
        return null;
    }
}