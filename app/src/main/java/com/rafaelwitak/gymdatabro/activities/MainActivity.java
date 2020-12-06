package com.rafaelwitak.gymdatabro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rafaelwitak.gymdatabro.R;
import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.WorkoutInstance;

public class MainActivity extends AppCompatActivity {
    public static GymBroDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = GymBroDatabase.getDatabase(this);

        setSupportActionBar(findViewById(R.id.toolbar));
    }

    public void resumeWorkout(View view) {
        Intent intent = getResumeWorkoutIntentOrRedirect();
        startActivity(intent);
    }

    private Intent getResumeWorkoutIntentOrRedirect() {
        WorkoutInstance workoutInstance = getWorkoutInstance();
        if (workoutInstance == null) {
            return new Intent(this, ChooseProgramActivity.class);
        }

        // FIXME: From here on, I'm ugly and convoluted and it would probably be best to base all
        //  that on the latest PerformanceSet directly.
        Intent intent = new Intent(this, WorkoutStepActivity.class);
        intent.putExtra("nextStepNumber", getNextWorkoutStepNumber());
        intent.putExtra("workoutID", workoutInstance.workoutId);
        intent.putExtra("workoutInstanceId", workoutInstance.id);

        return intent;
    }

    private Integer getNextWorkoutStepNumber() {
        Integer latestNumber = getLatestWorkoutStepNumber();
        return database.workoutStepDAO().getNextNumberUp(latestNumber);
    }

    @NonNull
    private Integer getLatestWorkoutStepNumber() {
        return database.workoutStepDAO().getWorkoutStepById(
                database.performanceSetDAO().getLatestPerformanceSet().workoutStepId)
                .number;
    }

    @Nullable
    public WorkoutInstance getWorkoutInstance() {
        WorkoutInstance instance = database.workoutInstanceDAO().getLatestWorkoutInstance();
        if (instance == null) {
            return null;
        }
        if (hasReachedLastStepOfWorkout()) {
            return database.workoutInstanceDAO().getNextWorkoutInstance();
        }
        return null;
    }

    public boolean hasReachedLastStepOfWorkout() {
        return database.workoutStepDAO().isLastStepOfWorkout(
                database.performanceSetDAO().getLatestPerformanceSet().workoutStepId);
    }

    public void chooseProgram(View view) {
        Intent intent = new Intent(this, ChooseProgramActivity.class);
        startActivity(intent);
    }

    public void addExercise(View view) {
        Intent intent = new Intent(this, EditExerciseActivity.class);
        startActivity(intent);
    }

    public void addWorkoutStep(View view) {
        Intent intent = new Intent(this, EditWorkoutStepActivity.class);
        startActivity(intent);
    }
}
