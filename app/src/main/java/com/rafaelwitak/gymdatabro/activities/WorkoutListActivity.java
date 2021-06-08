/*
 * Copyright (c) 2021, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rafaelwitak.gymdatabro.R;
import com.rafaelwitak.gymdatabro.activities.dummy.DummyContent;
import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.MasterDao;
import com.rafaelwitak.gymdatabro.database.Workout;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutListBinding;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * An activity representing a list of Workouts. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link WorkoutDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
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
    private List<Workout> workouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityWorkoutListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.dao = GymBroDatabase.getDatabase(this).masterDao();

        final int ID_ALL_PROGRAMS = -1;
        int programId = getIntent().getIntExtra("programId", ID_ALL_PROGRAMS);

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(view -> {
        }); // TODO: 17.04.2021 Add listener.

        if (binding.workoutListLayout.workoutDetailContainer != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        workouts = dao.getWorkoutsForProgramId(programId);

        this.recyclerView = binding.workoutListLayout.workoutList;
        setupRecyclerView(recyclerView);

        Button button = binding.workoutListButtonSave;
        button.setOnClickListener(v -> saveAndExit(workouts));

        final ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
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
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final SimpleItemRecyclerViewAdapter adapter =
                        (SimpleItemRecyclerViewAdapter) Objects.requireNonNull(recyclerView.getAdapter());

                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        adapter.getWorkouts().add(position, adapter.getWorkouts().get(position));
                        adapter.notifyDataSetChanged();
                        break;
                    case ItemTouchHelper.RIGHT:
                        adapter.getWorkouts().remove(position);
                        adapter.notifyItemRemoved(position);
                        break;
                }
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        assert workouts != null;
        recyclerView.setAdapter(
                new SimpleItemRecyclerViewAdapter(this, workouts, mTwoPane));
    }

    private <T extends Collection<Workout>> void saveAndExit(@NonNull T workouts) {
        // TODO: 05.06.2021 test
        // TODO: 08.06.2021 :
        //  * Treat changed Program as new Program entry!
        //  * reenter all Workouts into DB with new primary key (PK is used for ordering...)
        //  * link WO-Steps to new ids
        //  * delete old Workouts
        //  * for duplicates, copy all data (except PK! but including linked WO-Steps!) to new Workout
        //  * delete WO-Steps for deleted Workouts

        try {
            dao.updateWorkouts(workouts);
            Log.d("GDB", "Workouts updated!");
        } catch (Exception e) {
            throw new RuntimeException("Saving failed! ", e);
        }
        finishAfterTransition();
    }


    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final WorkoutListActivity mParentActivity;
        private final boolean mTwoPane;
        private final List<Workout> workouts;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            // TODO: 08.06.2021 Replace DummyContent
            @Override
            public void onClick(@NonNull View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(WorkoutDetailFragment.ARG_ITEM_ID, item.id);
                    WorkoutDetailFragment fragment = new WorkoutDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.workout_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, WorkoutDetailActivity.class);
                    intent.putExtra(WorkoutDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(WorkoutListActivity parent,
                                      @NonNull List<Workout> workouts,
                                      boolean twoPane) {
            this.workouts = workouts;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.workout_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            holder.mIdView.setText(workouts.get(position).getName());
            holder.mContentView.setText(workouts.get(position).getDetails());

            holder.itemView.setTag(workouts.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return workouts.size();
        }

        public @NonNull
        List<Workout> getWorkouts() {
            return workouts;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = view.findViewById(R.id.id_text);
                mContentView = view.findViewById(R.id.content);
            }
        }
    }
}