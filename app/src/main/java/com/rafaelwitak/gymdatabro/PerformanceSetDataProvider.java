package com.rafaelwitak.gymdatabro;

public interface PerformanceSetDataProvider {
    public abstract int getExerciseID();
    public abstract Integer getReps();
    public abstract Float getWeight();
    public abstract Integer getSecondsPerformed();
    public abstract Integer getSecondsRested();
    public abstract Float getRpe();
    public abstract Integer getPainLevel();
    public abstract String getNotes();
}
