package com.rafaelwitak.gymdatabro;

import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;

import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.Set;
import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;
import com.rafaelwitak.gymdatabro.workoutStepRows.DurationRow;
import com.rafaelwitak.gymdatabro.workoutStepRows.RPERow;
import com.rafaelwitak.gymdatabro.workoutStepRows.RepsRow;
import com.rafaelwitak.gymdatabro.workoutStepRows.RestRow;
import com.rafaelwitak.gymdatabro.workoutStepRows.WeightRow;
import com.rafaelwitak.gymdatabro.workoutStepRows.WorkoutStepRow;

import java.util.ArrayList;
import java.util.Arrays;

public class WorkoutStepActivity extends AppCompatActivity {

    private GymBroDatabase database;
    private ActivityWorkoutStepBinding binding;
    private WorkoutStep currentWorkoutStep;
    private Set performedSet;
    private Toolbar toolbar;
    private SeekBar painSlider;

    private ArrayList <WorkoutStepRow> rows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_step);
        currentWorkoutStep = getCurrentWorkoutStep();
        database = MainActivity.database;
        performedSet = new Set();

        // automatically bind all Views with IDs
        binding = ActivityWorkoutStepBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // populate rows
        // TODO populate
        rows.addAll(Arrays.asList(
                new RepsRow(this, binding, currentWorkoutStep),
                new WeightRow(this, binding, currentWorkoutStep),
                new RPERow(this, binding, currentWorkoutStep),
                new DurationRow(this, binding, currentWorkoutStep),
                new RestRow(this, binding, currentWorkoutStep)
                )
        );

        // set up Toolbar
        toolbar = binding.toolbar.getRoot();
        setSupportActionBar(toolbar);

        // set up painSlider SeekBar
        painSlider = binding.stepPainSlider;
        painSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (painLevelInsideBounds(progress)) {
                    performedSet.painLevel = progress;
                }
                else {
                    throw new IndexOutOfBoundsException("Pain Level out of bounds");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        toolbar.setTitle("Current Workout Name");
        toolbar.setSubtitle("Current Exercise");



        // TODO rebuild database and update schema in IDE50

        setupWorkoutStepViewRows();

        binding.stepBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: implement
            }
        });
    }

    private boolean painLevelInsideBounds(Integer painLevel) {
        return (0 <= painLevel && painLevel <= getResources().getInteger(R.integer.pain_max));
    }

    // Set visibility and/or data for the WorkoutStep's View's Rows
    private void setupWorkoutStepViewRows() {

        for ( WorkoutStepRow row : rows ) {
            row.setup();
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