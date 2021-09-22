/*
 * Copyright (c) 2021, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class WorkoutInstanceUpdaterTest {
    WorkoutInstanceDAO mockDao;

    @Test
    public void unchangedListDoesNothing() {
        final List<WorkoutInstance> mockList = mockDao.getAllWorkoutInstancesForProgram(1);
        new WorkoutInstanceUpdater(mockDao, 1, mockList).update();
        assertThat(mockDao.getAllWorkoutInstancesForProgram(1)).isEqualTo(mockList);
    }

    @Test
    public void oneDuplicateGetsInserted() {
        final List<WorkoutInstance> mockList = mockDao.getAllWorkoutInstancesForProgram(1);
        final List<WorkoutInstance> newInstances = StreamSupport.stream(mockList)
                .map(WorkoutInstance::clone)
                .collect(Collectors.toList());
        newInstances.add(0, newInstances.get(0).clone());
        new WorkoutInstanceUpdater(mockDao, 1, newInstances).update();
        final List<WorkoutInstance> result = mockDao.getAllWorkoutInstancesForProgram(1);
        final int[] woNumbers = StreamSupport.stream(result).mapToInt(WorkoutInstance::getWorkoutNumber).sorted().toArray();
        assertThat(woNumbers).isEqualTo(new int[]{1, 2, 3, 4, 5, 6});
    }

    @Test
    public void multipleInsertedDuplicates() {
        final List<WorkoutInstance> mockList = mockDao.getAllWorkoutInstancesForProgram(1);
        final List<WorkoutInstance> newInstances = StreamSupport.stream(mockList)
                .map(WorkoutInstance::clone)
                .collect(Collectors.toList());
        newInstances.add(0, newInstances.get(0).clone());
        newInstances.add(newInstances.size()-1, newInstances.get(newInstances.size()-1).clone());
        newInstances.add(0, newInstances.get(0).clone());
        newInstances.add(newInstances.size()-1, newInstances.get(newInstances.size()-1).clone());

        new WorkoutInstanceUpdater(mockDao, 1, newInstances).update();
        final List<WorkoutInstance> result = mockDao.getAllWorkoutInstancesForProgram(1);
        final int[] woNumbers = StreamSupport.stream(result).mapToInt(WorkoutInstance::getWorkoutNumber).sorted().toArray();
        assertThat(woNumbers).isEqualTo(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
    }

    @Test
    public void deleteElements() {
        // FIXME: 22.09.2021
        final List<WorkoutInstance> mockList = mockDao.getAllWorkoutInstancesForProgram(1);
        final List<WorkoutInstance> newInstances = StreamSupport.stream(mockList)
                .map(WorkoutInstance::clone)
                .collect(Collectors.toList());
        newInstances.remove(newInstances.size()-1);
        newInstances.remove(0);
        newInstances.remove(newInstances.size()/2);

        new WorkoutInstanceUpdater(mockDao, 1, newInstances).update();
        final List<WorkoutInstance> result = mockDao.getAllWorkoutInstancesForProgram(1);
        final int[] woNumbers = StreamSupport.stream(result).mapToInt(WorkoutInstance::getWorkoutNumber).sorted().toArray();
        assertThat(woNumbers).isEqualTo(new int[]{1, 2});
    }

    @Before
    public void setupMockDao() {
        this.mockDao = new WorkoutInstanceDAO() {
            private final List<WorkoutInstance> mockInstances =
                    StreamSupport.stream(Arrays.asList(
                            new WorkoutInstance(1, "one", 1, 1, 1),
                            new WorkoutInstance(2, "two", 1, 1, 2),
                            new WorkoutInstance(3, "three", 1, 1, 3),
                            new WorkoutInstance(4, "four", 1, 1, 4),
                            new WorkoutInstance(5, "five", 1, 1, 5)))
                    .collect(Collectors.toList());

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
            public WorkoutInstance getWorkoutInstance(int programId, int workoutNumber) {
                final List<WorkoutInstance> results = StreamSupport.stream(mockInstances)
                        .filter(instance -> instance.getProgramId() == programId)
                        .filter(instance -> instance.getWorkoutNumber() == workoutNumber)
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
            public WorkoutInstance getNextWorkoutInstanceForProgram(Integer programId, Integer previousWorkoutNumber) {
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
            public List<WorkoutInstance> getAllWorkoutInstancesForProgram(Integer programId) {
                return mockInstances;
            }

            @Override
            public long insertWorkoutInstanceForId(WorkoutInstance workoutInstance) {
                final int newId = new Random().nextInt(Integer.MAX_VALUE - 1000) + 1000;
                workoutInstance.setId(newId);
                mockInstances.add(workoutInstance);
                assertWithMessage("Unique constraint violated: ")
                        .that(StreamSupport.stream(mockInstances)
                        .filter(instance -> instance.getId() == workoutInstance.getId())
                        .filter(instance -> instance.getWorkoutNumber() == workoutInstance.getWorkoutNumber())
                        .count()).isEqualTo(1);
                return newId;
            }

            @Override
            public void updateWorkoutInstance(WorkoutInstance workoutInstance) {
                final List<WorkoutInstance> sameId = StreamSupport.stream(mockInstances)
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
            public void deleteWorkoutInstance(WorkoutInstance workoutInstance) {
                mockInstances.remove(workoutInstance);
            }

            @NonNull
            @Override
            public String toString() {
                return "MockDAO: \n" +
                        StreamSupport.stream(mockInstances)
                                .map(instance -> "id=" + instance.getWorkoutId() +
                                        ", number=" + instance.getWorkoutNumber())
                                .collect(Collectors.joining("/n"));
            }
        };
    }
}