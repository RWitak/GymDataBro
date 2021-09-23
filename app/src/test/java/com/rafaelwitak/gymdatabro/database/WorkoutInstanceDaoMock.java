/*
 * Copyright (c) 2021, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import java.util.List;
import java.util.Random;

import static com.google.common.truth.Truth.assertWithMessage;

public class WorkoutInstanceDaoMock extends WorkoutInstanceDAO {

    private final List<WorkoutInstance> mockInstances;

    public WorkoutInstanceDaoMock(List<WorkoutInstance> mockInstances) {
        this.mockInstances = mockInstances;
    }

    @Nullable
    @Override
    public String getNameByInstanceId(Integer id) {
        return null;
    }

    @Nullable
    @Override
    public Integer getLatestWorkoutInstanceIdForProgramId(int programId) {
        return null;
    }

    @Nullable
    @Override
    public WorkoutInstance getLatestWorkoutInstance() {
        return null;
    }

    @Nullable
    @Override
    public WorkoutInstance getWorkoutInstance(
            int programId,
            int workoutNumber) {

        final List<WorkoutInstance> results =
                StreamSupport.stream(mockInstances)
                        .filter(instance ->
                                instance.getProgramId() == programId)
                        .filter(instance ->
                                instance.getWorkoutNumber() == workoutNumber)
                        .collect(Collectors.toList());

        assertWithMessage("Unique constraint violated: ")
                .that(results.size()).isLessThan(2);

        if (results.size() == 1) {
            return results.get(0);
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public WorkoutInstance getNextWorkoutInstanceForProgram(
            Integer programId,
            Integer previousWorkoutNumber) {

        return null;
    }

    @Nullable
    @Override
    public WorkoutInstance getFirstWorkoutInstanceForProgram(Integer programId) {
        return null;
    }

    @Override
    public boolean isLastInstanceOfProgram(int instanceId, Integer programId) {
        return false;
    }

    @Override
    public List<WorkoutInstance> getAllWorkoutInstancesForProgram(
            Integer programId) {

        return mockInstances;
    }

    @Override
    public long insertWorkoutInstanceForId(WorkoutInstance workoutInstance) {
        final int newId =
                new Random().nextInt(Integer.MAX_VALUE - 1000) + 1000;
        workoutInstance.setId(newId);
        mockInstances.add(workoutInstance);

        assertWithMessage("Unique constraint violated: ")
                .that(StreamSupport.stream(mockInstances)
                        .filter(instance ->
                                instance.getId() == workoutInstance.getId())
                        .filter(instance ->
                                instance.getWorkoutNumber()
                                        == workoutInstance.getWorkoutNumber())
                        .count()).isEqualTo(1);

        return newId;
    }

    @Override
    public void updateWorkoutInstance(WorkoutInstance workoutInstance) {
        final List<WorkoutInstance> sameId = StreamSupport
                .stream(mockInstances)
                .filter(instance -> instance.getId() == workoutInstance.getId())
                .collect(Collectors.toList());

        assertWithMessage("Unique constraint violated by " +
                StreamSupport.stream(sameId)
                        .map(instance -> "id:" + instance.getId()
                                + "/num:" + instance.getWorkoutNumber())
                        .collect(Collectors.joining(" and ")))
                .that(sameId.size()).isEqualTo(1);

        final WorkoutInstance oldInstance = sameId.get(0);
        final int indexOf = mockInstances.indexOf(oldInstance);
        mockInstances.remove(oldInstance);
        mockInstances.add(indexOf, workoutInstance);
    }

    @Override
    public void deleteWorkoutInstance(int instanceId) {
        mockInstances.removeIf(instance -> instance.getId() == instanceId);
    }

    @NonNull
    @Override
    public String toString() {
        return "MockDAO: \n" + StreamSupport
                .stream(mockInstances)
                .map(instance -> "id=" + instance.getWorkoutId()
                         + ", number=" + instance.getWorkoutNumber())
                .collect(Collectors.joining("/n"));
    }
}

