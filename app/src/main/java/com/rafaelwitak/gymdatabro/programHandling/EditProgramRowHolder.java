package com.rafaelwitak.gymdatabro.programHandling;

import android.content.Context;

import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.Program;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditProgramBinding;
import com.rafaelwitak.gymdatabro.programHandling.editProgramRows.InfoRow;
import com.rafaelwitak.gymdatabro.programHandling.editProgramRows.LinksRow;
import com.rafaelwitak.gymdatabro.programHandling.editProgramRows.NameRow;
import com.rafaelwitak.gymdatabro.programHandling.editProgramRows.NotesRow;
import com.rafaelwitak.gymdatabro.programHandling.editProgramRows.SourceRow;

import java.util.Arrays;
import java.util.List;

public class EditProgramRowHolder {
    private final GymBroDatabase database;
    private final Program program;

    private final NameRow nameRow;
    private final SourceRow sourceRow;
    private final LinksRow linksRow;
    private final InfoRow infoRow;
    private final NotesRow notesRow;

//    out of use:
//    private final NumberWorkoutsRow numberWorkoutsRow;

    public EditProgramRowHolder(Context context, ActivityEditProgramBinding binding, Program program) {
        this.database = GymBroDatabase.getDatabase(context);
        this.program = program;

        this.nameRow = new NameRow(binding);
        this.sourceRow = new SourceRow(binding);
        this.linksRow = new LinksRow(binding);
        this.infoRow = new InfoRow(binding);
        this.notesRow = new NotesRow(binding);

//        out of use:
//        numberWorkoutsRow = new NumberWorkoutsRow(binding);
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
        return setupProgramFromRowsInputs();
    }

    private Program setupProgramFromRowsInputs() {
        program.name = this.nameRow.getEditTextValueAsString();
        program.source = this.sourceRow.getEditTextValueAsString();
        program.links = this.linksRow.getEditTextValueAsString();
        program.info = this.infoRow.getEditTextValueAsString();
        program.notes = this.notesRow.getEditTextValueAsString();
        program.number_workouts = database.workoutDAO()
                .getNumberOfWorkoutsByProgram(program.id);

//        out of use:
//        program.number_workouts = (Integer) this.numberWorkoutsRow.getEditTextValue();

        return program;
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
