package com.rafaelwitak.gymdatabro.viewRows.workoutStepRows;

import android.content.Context;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.rafaelwitak.gymdatabro.PerformanceSetUserInputProvider;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

public class PainSlider implements PerformanceSetUserInputProvider {

    private final AppCompatSeekBar seekBar;

    public PainSlider(ActivityWorkoutStepBinding binding) {
        this.seekBar = binding.stepPainSlider;
    }

    public void setOnSeekBarChangeListener(AppCompatSeekBar.OnSeekBarChangeListener changeListener) {
        this.seekBar.setOnSeekBarChangeListener(changeListener);
    }

    @Override
    public Object getUserInput() {
        return seekBar.getProgress();
    }
}
