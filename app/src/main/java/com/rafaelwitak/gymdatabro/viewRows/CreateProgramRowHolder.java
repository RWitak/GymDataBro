package com.rafaelwitak.gymdatabro.viewRows;

import com.rafaelwitak.gymdatabro.database.Program;
import com.rafaelwitak.gymdatabro.databinding.ActivityCreateProgramBinding;
import com.rafaelwitak.gymdatabro.viewRows.createProgramRows.InfoRow;
import com.rafaelwitak.gymdatabro.viewRows.createProgramRows.LinksRow;
import com.rafaelwitak.gymdatabro.viewRows.createProgramRows.NameRow;
import com.rafaelwitak.gymdatabro.viewRows.createProgramRows.NotesRow;
import com.rafaelwitak.gymdatabro.viewRows.createProgramRows.NumberWorkoutsRow;
import com.rafaelwitak.gymdatabro.viewRows.createProgramRows.SourceRow;

public class CreateProgramRowHolder {
    private final NameRow nameRow;
    private final SourceRow sourceRow;
    private final LinksRow linksRow;
    private final InfoRow infoRow;
    private final NotesRow notesRow;
    private final NumberWorkoutsRow numberWorkoutsRow;

    public CreateProgramRowHolder(ActivityCreateProgramBinding binding) {
        nameRow = new NameRow(binding);
        sourceRow = new SourceRow(binding);
        linksRow = new LinksRow(binding);
        infoRow = new InfoRow(binding);
        notesRow = new NotesRow(binding);
        numberWorkoutsRow = new NumberWorkoutsRow(binding);
    }

    public Program getProgram() {
        return setupProgramFromRowsInputs(new Program());
    }

    private Program setupProgramFromRowsInputs(Program program) {
        program.name = (String) this.nameRow.getEditTextValue();
        program.source = (String) this.sourceRow.getEditTextValue();
        program.links = (String) this.linksRow.getEditTextValue();
        program.info = (String) this.infoRow.getEditTextValue();
        program.notes = (String) this.notesRow.getEditTextValue();
        program.number_workouts = (Integer) this.numberWorkoutsRow.getEditTextValue();

        return program;
    }
}
