/*
 * Copyright (c) 2021, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.programHandling;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rafaelwitak.gymdatabro.R;
import com.rafaelwitak.gymdatabro.activities.EditProgramActivity;
import com.rafaelwitak.gymdatabro.activities.IntentMaker;
import com.rafaelwitak.gymdatabro.database.Program;

import java.util.List;

public final class ChooseProgramAdapter
        extends RecyclerView.Adapter<ChooseProgramAdapter.ViewHolder> {
    private final List<Program> programs;

    public ChooseProgramAdapter(List<Program> dataSet) {
        programs = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_choose_program, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Program currentProgram = programs.get(position);
        holder.itemView.setTag(currentProgram);
        holder.getRow().setTextViewText(currentProgram.getName());
    }

    @Override
    public int getItemCount() {
        return programs.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ChooseProgramRow row;

        public ChooseProgramRow getRow() {
            return row;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            row = (ChooseProgramRow) itemView.findViewById(R.id.row_choose_program_text);

            row.setOnClickListener(v -> getOnClickListener(itemView, v));

            row.setOnLongClickListener(v -> getOnLongClickListener(itemView, v));
        }

        private void getOnClickListener(@NonNull View itemView, View v) {
            Context context = v.getContext();
            Program program = (Program) itemView.getTag();

            context.startActivity(
                    new IntentMaker(context)
                            .getIntentToResumeProgram(program.getId()));
        }

        private boolean getOnLongClickListener(@NonNull View itemView, View v) {
            Context context = v.getContext();
            Program program = (Program) itemView.getTag();

            Intent intent = new Intent(context, EditProgramActivity.class);

            intent.putExtra("programID", program.getId());
            context.startActivity(intent);
            return true;
        }
    }
}
