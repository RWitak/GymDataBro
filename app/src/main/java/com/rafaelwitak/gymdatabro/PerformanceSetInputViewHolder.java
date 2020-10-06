package com.rafaelwitak.gymdatabro;

import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;
import com.rafaelwitak.gymdatabro.viewRows.ExerciseNameRow;

public class PerformanceSetInputViewHolder {
    private ActivityWorkoutStepBinding binding;

    public PerformanceSetInputViewHolder(ActivityWorkoutStepBinding binding) {
        this.binding = binding;
    }

    public PerformanceSetDataHolder dataHolder getExerciseNameHolder() {
        return ExerciseNameRow;
    }
}
