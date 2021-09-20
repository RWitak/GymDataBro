/*
 * Copyright (c) 2021, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.workoutHandling;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.rafaelwitak.gymdatabro.R;
import com.rafaelwitak.gymdatabro.activities.WorkoutDetailActivity;
import com.rafaelwitak.gymdatabro.activities.WorkoutDetailFragment;
import com.rafaelwitak.gymdatabro.activities.WorkoutListActivity;
import com.rafaelwitak.gymdatabro.util.UniqueWorkout;

import java.util.List;

public class WorkoutListRecyclerViewAdapter
        extends RecyclerView.Adapter<WorkoutListRecyclerViewAdapter.ViewHolder> {

    private final WorkoutListActivity mParentActivity;
    private final boolean mTwoPane;
    private final List<UniqueWorkout> uniqueWorkouts;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        // TODO: 08.06.2021 Replace DummyContent
        @Override
        public void onClick(@NonNull View view) {
            UniqueWorkout item = (UniqueWorkout) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putInt(WorkoutDetailFragment.ARG_ITEM_ID,
                        item.getInstanceId());
                WorkoutDetailFragment fragment = new WorkoutDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.workout_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, WorkoutDetailActivity.class);
                intent.putExtra(WorkoutDetailFragment.ARG_ITEM_ID,
                        item.getInstanceId());

                context.startActivity(intent);
            }
        }
    };

    public WorkoutListRecyclerViewAdapter(WorkoutListActivity parent,
                                          @NonNull List<UniqueWorkout> uniqueWorkouts,
                                          boolean twoPane) {
        this.uniqueWorkouts = uniqueWorkouts;
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
        holder.mIdView.setText(uniqueWorkouts.get(position).computeName());
//        YAGNI details...
//        holder.mContentView.setText(uniqueWorkouts.get(position).computeName());

        holder.itemView.setTag(uniqueWorkouts.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return uniqueWorkouts.size();
    }

    public @NonNull
    List<UniqueWorkout> getUniqueWorkouts() {
        return uniqueWorkouts;
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
