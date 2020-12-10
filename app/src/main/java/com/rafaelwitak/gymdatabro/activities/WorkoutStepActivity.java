package com.rafaelwitak.gymdatabro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.PerformanceSet;
import com.rafaelwitak.gymdatabro.database.Workout;
import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;
import com.rafaelwitak.gymdatabro.performanceSetHandling.PerformanceSetDataProviderHolder;
import com.rafaelwitak.gymdatabro.performanceSetHandling.PerformanceSetMaker;
import com.rafaelwitak.gymdatabro.workoutStepHandling.WorkoutStepRowHolder;

public class WorkoutStepActivity extends AppCompatActivity {

    private GymBroDatabase database;
    private Workout currentWorkout;
    private WorkoutStep currentWorkoutStep;
    private ActivityWorkoutStepBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.database = GymBroDatabase.getDatabase(this);
        this.currentWorkoutStep = getCurrentWorkoutStep();
        if (currentWorkoutStep == null) {
            Toast.makeText(this,
                    "Please create a WorkoutStep first!",
                    Toast.LENGTH_SHORT).show();
            finish();
            return; // DO NOT DELETE: Method will try to continue without a proper WorkoutStep!
        }
        this.currentWorkout = getCurrentWorkout();

        // automatically bind all Views with IDs
        this.binding = ActivityWorkoutStepBinding.inflate(getLayoutInflater());
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
        this.setTitle(getToolbarTitle()); // otherwise toolbar always just displays app name

        String programName = getCurrentProgramName();
        if (programName != null) {
            toolbar.setSubtitle(programName);
        }
    }

    private String getToolbarTitle() {
        String workoutInstanceName = getWorkoutInstanceName(getWorkoutInstanceId());
        if (workoutInstanceName == null || workoutInstanceName.isEmpty()) {
            if (currentWorkout.name.isEmpty()) {
                return "Unnamed Workout";
            }
            return currentWorkout.name;
        }
        return workoutInstanceName;
    }

    private String getWorkoutInstanceName(Integer workoutInstanceId) {
        return database.workoutInstanceDAO().getNameByInstanceId(workoutInstanceId);
    }

    // Set visibility and/or data for the WorkoutStep's View's Rows
    private void setUpWorkoutStepViewRows() {
        WorkoutStepRowHolder rowHolder = new WorkoutStepRowHolder(
                binding,
                currentWorkoutStep,
                this);
        rowHolder.setupAllRows();
    }

    private void setUpButton() {
        binding.stepBtnNext.setOnClickListener(getViewOnClickListener());
    }


    @NonNull
    private View.OnClickListener getViewOnClickListener() {
        return v -> {
            savePerformanceSet(getPerformedSet());

            if (isLastWorkoutStep(currentWorkoutStep)){
                congratulateAndFinish();
            }
            else {
                startNextWorkoutStep();
            }
        };
    }

    public void congratulateAndFinish() {
        Toast.makeText(this, "Congrats, you are done for today! \n" +
                "Have a good rest!", Toast.LENGTH_LONG).show();
        finish();
    }

    @NonNull
    private PerformanceSet getPerformedSet() {
        return PerformanceSetMaker.getPerformanceSet(getPerformanceSetDataProviderHolder());
    }

    @NonNull
    private PerformanceSetDataProviderHolder getPerformanceSetDataProviderHolder() {
        return new PerformanceSetDataProviderHolder(
                binding,
                currentWorkoutStep,
                getWorkoutInstanceId());
    }

    private boolean isLastWorkoutStep(@NonNull WorkoutStep currentWorkoutStep) {
        return database.masterDao()
                .isLastStepOfWorkout(
                        currentWorkoutStep.workoutID,
                        currentWorkoutStep.number);
    }


    private void savePerformanceSet(PerformanceSet performanceSet) {
        long savedSetRowId = database.performanceSetDAO().insertSet(performanceSet);

        Log.d("GymDataBro", "PerformanceSet successfully saved to database by "
                + "WorkoutStepActivity.savePerformanceSet():"
                + "\n"
                + database.performanceSetDAO().getSetByRowId(savedSetRowId).toString());
    }

    private void startNextWorkoutStep() {
        Intent intent = getNewIntentWithExtras();

        startActivity(intent);
        finish();
    }

    @NonNull
    private Intent getNewIntentWithExtras() {
        Intent intent = new Intent(this, WorkoutStepActivity.class);
        Integer nextWorkoutStepNumber = getNextWorkoutStepNumber();
        intent.putExtra("workoutID", currentWorkoutStep.workoutID);
        intent.putExtra("nextStepNumber", nextWorkoutStepNumber);
        intent.putExtra("workoutInstanceId", getWorkoutInstanceId());

        return intent;
    }

    @Nullable
    private Integer getNextWorkoutStepNumber() {
        WorkoutStep nextStep = database.masterDao()
                    .getNextStepInWorkout(currentWorkoutStep.id, currentWorkout.id);
        if (nextStep == null) {
            return null;
        }
        return nextStep.number;
    }


    private WorkoutStep getCurrentWorkoutStep() {
        int workoutID = getWorkoutIdFromIntent();
        int stepNumber = getStepNumberFromIntent();

        Log.d("GymDataBro", "Fetching Workout " +
                    + workoutID
                    + ", Step "
                    + stepNumber);

        return database
                .workoutStepDAO()
                .getWorkoutStep(workoutID, stepNumber);
    }

    private int getWorkoutIdFromIntent() {
        return getIntent().getIntExtra("workoutID", 1);
    }

    private int getStepNumberFromIntent() {
        return getIntent().getIntExtra("nextStepNumber", 0);
    }

    @NonNull
    private Workout getCurrentWorkout() {
        Workout currentWorkout = database.workoutDAO().getWorkoutByID(currentWorkoutStep.workoutID);

        if (currentWorkout == null) {
            return new Workout();
        }

        return currentWorkout;
    }

    @Nullable
    private Integer getWorkoutInstanceId() {
        int currentWorkoutNumber = getWorkoutInstanceIdFromIntent();
        return currentWorkoutNumber == -1
                ? null
                : currentWorkoutNumber;
    }

    private int getWorkoutInstanceIdFromIntent() {
        return getIntent().getIntExtra("workoutInstanceId", -1);
    }


    @Nullable
    private String getCurrentProgramName() {
        Integer id = currentWorkout.programID;
        if (id != null) {
            return database.programDAO().getProgramByID(id).name;
        }
        return null;
    }
}