package com.rafaelwitak.gymdatabro.viewRows;

import com.rafaelwitak.gymdatabro.database.Program;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditProgramBinding;
import com.rafaelwitak.gymdatabro.viewRows.editProgramRows.InfoRow;
import com.rafaelwitak.gymdatabro.viewRows.editProgramRows.LinksRow;
import com.rafaelwitak.gymdatabro.viewRows.editProgramRows.NameRow;
import com.rafaelwitak.gymdatabro.viewRows.editProgramRows.NotesRow;
import com.rafaelwitak.gymdatabro.viewRows.editProgramRows.SourceRow;

import java.util.Arrays;
import java.util.List;

import static com.rafaelwitak.gymdatabro.activities.MainActivity.database;

public class EditProgramRowHolder {
    private final Program program;

    private final NameRow nameRow;
    private final SourceRow sourceRow;
    private final LinksRow linksRow;
    private final InfoRow infoRow;
    private final NotesRow notesRow;

//    out of use:
//    private final NumberWorkoutsRow numberWorkoutsRow;

    public EditProgramRowHolder(ActivityEditProgramBinding binding, Program program) {
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
}
