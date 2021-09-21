/*
 * Copyright (c) 2021, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.MasterDao;
import com.rafaelwitak.gymdatabro.database.WorkoutInstance;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutListBinding;
import com.rafaelwitak.gymdatabro.util.UniqueWorkout;
import com.rafaelwitak.gymdatabro.workoutHandling.WorkoutListRecyclerViewAdapter;
import java8.util.Optional;
import java8.util.stream.StreamSupport;

import java.util.List;
import java.util.Objects;

/**
 * An activity representing a list of Workouts. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link WorkoutDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 *
 * ATTENTION: Despite the name, this mostly deals with WorkoutInstances,
 * not Workouts themselves!
 */
public class WorkoutListActivity extends AppCompatActivity {
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private MasterDao dao;
    private ActivityWorkoutListBinding binding;
    private RecyclerView recyclerView;
    private List<UniqueWorkout> uniqueWorkouts;
    private int programId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityWorkoutListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.dao = GymBroDatabase.getDatabase(this).masterDao();

        final int ID_ALL_PROGRAMS = -1;
        programId = getIntent().getIntExtra("programId", ID_ALL_PROGRAMS);

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(view -> {
            IntentMaker intentMaker = new IntentMaker(getBaseContext());
            Intent editWorkoutIntent =
                    intentMaker.getEditWorkoutIntent(
                        Optional.empty(),
                        programId);
            startActivity(editWorkoutIntent);
        });

        if (binding.workoutListLayout.workoutDetailContainer != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        this.uniqueWorkouts = dao.getUniqueWorkoutsOfProgram(programId);

        this.recyclerView = binding.workoutListLayout.workoutList;
        setupRecyclerView(recyclerView);

        Button button = binding.workoutListButtonSave;
        button.setOnClickListener(v -> saveAndExit(uniqueWorkouts));

        final ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
                                 int direction) {
                final int position = viewHolder.getAbsoluteAdapterPosition();
                final WorkoutListRecyclerViewAdapter adapter =
                        (WorkoutListRecyclerViewAdapter) Objects.requireNonNull(
                                recyclerView.getAdapter());

                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        adapter.getUniqueWorkouts()
                                .add(position,
                                        adapter.getUniqueWorkouts()
                                                .get(position));
                        adapter.notifyDataSetChanged();
                        break;
                    case ItemTouchHelper.RIGHT:
                        adapter.getUniqueWorkouts().remove(position);
                        adapter.notifyItemRemoved(position);
                        break;
                }
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        assert uniqueWorkouts != null;
        recyclerView.setAdapter(
                new WorkoutListRecyclerViewAdapter(
                        this, uniqueWorkouts, mTwoPane));
    }

    private <T extends List<UniqueWorkout>> void saveAndExit(
            @NonNull T uniqueWorkouts) {
        final List<WorkoutInstance> workoutInstances =
                StreamSupport.stream(uniqueWorkouts)
                        .map(UniqueWorkout::toInstance)
                        .toList();
        try {
            dao.updateWorkoutInstancesOfProgram(programId,
                    workoutInstances,
                    new AlertDialog.Builder(this)
                            .setCancelable(true).create());
            Log.i("GDB", "WorkoutInstances updated!");
        } catch (Exception e) {
            throw new RuntimeException("Updating workout failed! ", e);
        }
        finishAfterTransition();
    }


}