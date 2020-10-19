package com.rafaelwitak.gymdatabro.viewRows;

import com.rafaelwitak.gymdatabro.database.Program;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditProgramBinding;
import com.rafaelwitak.gymdatabro.viewRows.editProgramRows.InfoRow;
import com.rafaelwitak.gymdatabro.viewRows.editProgramRows.LinksRow;
import com.rafaelwitak.gymdatabro.viewRows.editProgramRows.NameRow;
import com.rafaelwitak.gymdatabro.viewRows.editProgramRows.NotesRow;
import com.rafaelwitak.gymdatabro.viewRows.editProgramRows.SourceRow;

import static com.rafaelwitak.gymdatabro.activities.MainActivity.database;

public class EditProgramRowHolder {
    private final NameRow nameRow;
    private final SourceRow sourceRow;
    private final LinksRow linksRow;
    private final InfoRow infoRow;
    private final NotesRow notesRow;

//    out of use:
//    private final NumberWorkoutsRow numberWorkoutsRow;

    public EditProgramRowHolder(ActivityEditProgramBinding binding) {
        nameRow = new NameRow(binding);
        sourceRow = new SourceRow(binding);
        linksRow = new LinksRow(binding);
        infoRow = new InfoRow(binding);
        notesRow = new NotesRow(binding);

//        out of use:
//        numberWorkoutsRow = new NumberWorkoutsRow(binding);
    }

    public Program getProgram() {
        return setupProgramFromRowsInputs(new Program());
    }

    private Program setupProgramFromRowsInputs(Program program) {
        program.name = this.nameRow.getEditTextValue().toString();
        program.source = this.sourceRow.getEditTextValue().toString();
        program.links = this.linksRow.getEditTextValue().toString();
        program.info = this.infoRow.getEditTextValue().toString();
        program.notes = this.notesRow.getEditTextValue().toString();
        program.number_workouts = database.workoutDAO()
                .getNumberOfWorkoutsByProgram(program.id);

//        out of use:
//        program.number_workouts = (Integer) this.numberWorkoutsRow.getEditTextValue();

        return program;
    }
}
