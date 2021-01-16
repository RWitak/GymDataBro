/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.programHandling;

import android.content.Context;

import com.rafaelwitak.gymdatabro.EditRowHolder;
import com.rafaelwitak.gymdatabro.database.Program;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditProgramBinding;
import com.rafaelwitak.gymdatabro.programHandling.editProgramRows.InfoRow;
import com.rafaelwitak.gymdatabro.programHandling.editProgramRows.LinksRow;
import com.rafaelwitak.gymdatabro.programHandling.editProgramRows.NameRow;
import com.rafaelwitak.gymdatabro.programHandling.editProgramRows.NotesRow;
import com.rafaelwitak.gymdatabro.programHandling.editProgramRows.SourceRow;

import java.util.Arrays;
import java.util.List;

public class EditProgramRowHolder extends EditRowHolder {
    private final Program program;

    private final NameRow nameRow;
    private final SourceRow sourceRow;
    private final LinksRow linksRow;
    private final InfoRow infoRow;
    private final NotesRow notesRow;

    public EditProgramRowHolder(
            Context context,
            ActivityEditProgramBinding binding,
            Program program) {
        super(context);
        this.program = program;

        this.nameRow = new NameRow(binding);
        this.sourceRow = new SourceRow(binding);
        this.linksRow = new LinksRow(binding);
        this.infoRow = new InfoRow(binding);
        this.notesRow = new NotesRow(binding);
    }

    public List<EditProgramRow> getRows() {
        return Arrays.asList(
                this.nameRow,
                this.sourceRow,
                this.linksRow,
                this.infoRow,
                this.notesRow
        );
    }

    public Program getProgram() {
        setupProgramFromRowsInputs();
        setNumberOfWorkouts();
        return program;
    }

    private void setupProgramFromRowsInputs() {
        program.setName(this.nameRow.getEditTextValueAsString());
        program.setSource(this.sourceRow.getEditTextValueAsString());
        program.setLinks(this.linksRow.getEditTextValueAsString());
        program.setInfo(this.infoRow.getEditTextValueAsString());
        program.setNotes(this.notesRow.getEditTextValueAsString());
    }

    private void setNumberOfWorkouts() {
        program.setNumber_workouts(database.workoutDAO()
                .getNumberOfWorkoutsByProgram(program.getId()));
    }

    public void setupRowTexts(Program program) {
        for ( EditProgramRow row : getRows() ) {
            row.setPreFilledText(program);
        }
    }

    public void displayNameError(CharSequence error) {
        this.nameRow.showErrorText(error);
    }
}
