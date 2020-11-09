package com.rafaelwitak.gymdatabro.exerciseHandling;

import android.content.Context;

import com.rafaelwitak.gymdatabro.EditRowHolder;
import com.rafaelwitak.gymdatabro.database.Exercise;
import com.rafaelwitak.gymdatabro.databinding.ActivityEditExerciseBinding;
import com.rafaelwitak.gymdatabro.exerciseHandling.editExerciseRows.CuesRow;
import com.rafaelwitak.gymdatabro.exerciseHandling.editExerciseRows.EquipmentRow;
import com.rafaelwitak.gymdatabro.exerciseHandling.editExerciseRows.LinksRow;
import com.rafaelwitak.gymdatabro.exerciseHandling.editExerciseRows.NamesRow;

import java.util.Arrays;
import java.util.List;

public class EditExerciseRowHolder extends EditRowHolder {
    private final Exercise exercise;

    private final NamesRow namesRow;
    private final CuesRow cuesRow;
    private final LinksRow linksRow;
    private final EquipmentRow equipmentRow;

    public EditExerciseRowHolder(
            Context context,
            ActivityEditExerciseBinding binding,
            Exercise exercise) {

        super(context);
        this.exercise = exercise;

        this.namesRow = new NamesRow(binding);
        this.cuesRow = new CuesRow(binding);
        this.linksRow = new LinksRow(binding);
        this.equipmentRow = new EquipmentRow(binding);
    }

    public List<EditExerciseRow> getRows() {
        return Arrays.asList(
                this.namesRow,
                this.cuesRow,
                this.linksRow,
                this.equipmentRow
        );
    }

    public Exercise getUpdatedExercise() {
        return setupExerciseFromRowsInputs(this.exercise);
    }

    private Exercise setupExerciseFromRowsInputs(Exercise exercise) {
        exercise.name = namesRow.getEditTextValueAsString();
        exercise.cues = cuesRow.getEditTextValueAsString();
        exercise.links = linksRow.getEditTextValueAsString();
        exercise.equipment = equipmentRow.getEditTextValueAsString();

        return exercise;
    }

    public void setupRowTexts(Exercise exercise) {
        namesRow.setPreFilledText(exercise.name);
        cuesRow.setPreFilledText(exercise.cues);
        linksRow.setPreFilledText(exercise.links);
        equipmentRow.setPreFilledText(exercise.equipment);
    }
}
