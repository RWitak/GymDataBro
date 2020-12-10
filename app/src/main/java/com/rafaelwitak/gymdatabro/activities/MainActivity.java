package com.rafaelwitak.gymdatabro.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rafaelwitak.gymdatabro.R;
import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.WorkoutInstance;
import com.rafaelwitak.gymdatabro.database.WorkoutStep;

public class MainActivity extends AppCompatActivity {
    public static GymBroDatabase database;
    private IntentMaker intentMaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.intentMaker = new IntentMaker(this);
        setContentView(R.layout.activity_main);

        database = GymBroDatabase.getDatabase(this);

        setSupportActionBar(findViewById(R.id.toolbar));
    }

    public void resumeWorkoutOrRedirect(View view) {
        Intent intent = intentMaker.getIntentToResumeWorkoutOrChooseProgram();
        startActivity(intent);
    }

    public void chooseProgram(View view) {
        Intent intent = intentMaker.getChooseProgramIntent();
        startActivity(intent);
    }

    public void addExercise(View view) {
        Intent intent = intentMaker.getEditExerciseIntent();
        startActivity(intent);
    }

    public void addWorkoutStep(View view) {
        Intent intent = intentMaker.getEditWorkoutStepIntent();
        startActivity(intent);
    }


    private static class IntentMaker {

        private final Context context;
        IntentMaker(Context context) {
            this.context = context;
        }

        @NonNull
        public Intent getChooseProgramIntent() {
            return new Intent(context, ChooseProgramActivity.class);
        }

        @NonNull
        public Intent getEditExerciseIntent() {
            return new Intent(context, EditExerciseActivity.class);
        }

        @NonNull
        public Intent getEditWorkoutStepIntent() {
            return new Intent(context, EditWorkoutStepActivity.class);
        }

        @NonNull
        private Intent getIntentToResumeWorkoutOrChooseProgram() {
            WorkoutInstance workoutInstance = getWorkoutInstance();
            if (workoutInstance == null) {
                Log.d("GDB", "workoutInstance == null");
                return getChooseProgramIntent();
            }
            Integer nextWorkoutStepNumber =
                    getNextWorkoutStepNumber(workoutInstance.programId, workoutInstance);

            Intent intent = new Intent(context, WorkoutStepActivity.class);
            intent.putExtra("workoutInstanceId", workoutInstance.id);
            intent.putExtra("workoutID", workoutInstance.workoutId);
            intent.putExtra("nextStepNumber", nextWorkoutStepNumber);

            return intent;
        }

        @Nullable
        private Integer getNextWorkoutStepNumber(Integer programId, WorkoutInstance instance) {
            WorkoutStep nextStep = database.masterDao()
                    .getNextWorkoutStepForProgramId(programId, instance);
            if (nextStep == null) {
                Log.d("GDB", "nextStep == null");
                return null;
            }
            return nextStep.number;
        }

        @NonNull
        private Integer getLatestWorkoutStepNumber() {
            return database.workoutStepDAO().getWorkoutStepById(
                    database.performanceSetDAO().getLatestPerformanceSet().workoutStepId)
                    .number;
        }

        @Nullable
        public WorkoutInstance getWorkoutInstance() {
            WorkoutInstance latestInstance = database.masterDao().getLatestWorkoutInstance();
            if (latestInstance != null && database.masterDao().isLastStepOfWorkout(
                    latestInstance.workoutId,
                    getLatestWorkoutStepNumber())) {
                if (database.masterDao()
                        .isLastInstanceOfProgram(latestInstance.id, latestInstance.programId)) {
                    return null;
                }
                return database.masterDao()
                        .getNextWorkoutInstance(
                                latestInstance.programId,
                                latestInstance.workoutNumber);
            }
            return null;
        }
    }
}
