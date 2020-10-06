package com.rafaelwitak.gymdatabro;

import com.rafaelwitak.gymdatabro.database.PerformanceSet;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

public class PerformanceSetDataProvider {

    private final ActivityWorkoutStepBinding binding;
    private final PerformanceSetInputViewHolder inputViewHolder;

    public PerformanceSetDataProvider(ActivityWorkoutStepBinding binding, PerformanceSetInputViewHolder inputViewHolder){
        this.binding = binding;
        this.inputViewHolder = inputViewHolder;
    }

    public int getExerciseID() {
        this.binding.stepExerciseNameTitle
    };

    public Integer getReps();
    public Float getWeight();
    public Integer getSecondsPerformed();
    public Integer getSecondsRested();
    public Float getRpe();
    public Integer getPainLevel();
    public String getNotes();
}
