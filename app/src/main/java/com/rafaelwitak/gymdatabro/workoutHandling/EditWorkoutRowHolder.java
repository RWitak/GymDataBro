/*
 * Copyright (c) 2021, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.workoutHandling;

import android.widget.EditText;
import android.widget.TextView;
import com.rafaelwitak.gymdatabro.database.Workout;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditWorkoutBinding;
import java8.util.Optional;

public class EditWorkoutRowHolder {
    private final Workout workout;
    private final EditText nameEdit;
    private final EditText detailsEdit;
    private final EditText notesEdit;

    public EditWorkoutRowHolder(
            ActivityEditWorkoutBinding binding,
            Workout workout) {

        this.workout = workout;
        nameEdit = binding.editWorkoutNameEdit;
        detailsEdit = binding.editWorkoutDetailsEdit;
        notesEdit = binding.editWorkoutNotesEdit;
    }

    public Workout getWorkout() {
        setupWorkoutFromRowsInputs();
        return workout;
    }

    private void setupWorkoutFromRowsInputs() {
        workout.setName(nameEdit.getText().toString().trim());
        workout.setDetails(detailsEdit.getText().toString().trim());
        workout.setNotes(notesEdit.getText().toString().trim());
    }

    public void displayNameError(CharSequence error) {
        this.nameEdit.setError(error);
        this.nameEdit.requestFocus();
    }

    public void setupRowTexts() {
        String name = Optional.ofNullable(workout.getName()).orElse("");
        String details = workout.getDetails();
        String notes = workout.getNotes();

        nameEdit.setText(name, TextView.BufferType.EDITABLE);
        detailsEdit.setText(Optional.ofNullable(details).orElse(""),
                TextView.BufferType.EDITABLE);
        notesEdit.setText(Optional.ofNullable(notes).orElse(""),
                TextView.BufferType.EDITABLE);
    }
}
