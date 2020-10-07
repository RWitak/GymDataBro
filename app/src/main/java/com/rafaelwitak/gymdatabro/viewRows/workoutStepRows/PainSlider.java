package com.rafaelwitak.gymdatabro.viewRows.workoutStepRows;

import androidx.appcompat.widget.AppCompatSeekBar;

import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

public class PainSlider {

    private final AppCompatSeekBar seekBar;

    public PainSlider(ActivityWorkoutStepBinding binding) {
        this.seekBar = binding.stepPainSlider;
    }

    public void setOnSeekBarChangeListener(AppCompatSeekBar.OnSeekBarChangeListener changeListener) {
        this.seekBar.setOnSeekBarChangeListener(changeListener);
    }
}
