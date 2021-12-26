/*
 * Copyright (c) 2021, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.database;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class WorkoutInstanceUpdaterTest {
    WorkoutInstanceDAO mockDao;

    @Before
    public void setupMockDao() {
        this.mockDao = new WorkoutInstanceDaoMock(
                StreamSupport.stream(Arrays.asList(
                                new WorkoutInstance(1, "one", 1, 1, 1, true),
                                new WorkoutInstance(2, "two", 1, 1, 2, true),
                                new WorkoutInstance(3, "three", 1, 1, 3, true),
                                new WorkoutInstance(4, "four", 1, 1, 4, true),
                                new WorkoutInstance(5, "five", 1, 1, 5, true)))
                        .collect(Collectors.toList()));
    }

    @Test
    public void unchangedListDoesNothing() {
        final List<WorkoutInstance> mockList =
                mockDao.getAllWorkoutInstancesForProgram(1);

        new WorkoutInstanceUpdater(mockDao, 1, mockList)
                .update();

        assertThat(mockDao.getAllWorkoutInstancesForProgram(1))
                .isEqualTo(mockList);
    }

    @Test
    public void oneDuplicateGetsInserted() {
        final List<WorkoutInstance> mockList =
                mockDao.getAllWorkoutInstancesForProgram(1);
        final List<WorkoutInstance> newInstances = StreamSupport
                .stream(mockList)
                .map(WorkoutInstance::clone)
                .collect(Collectors.toList());

        newInstances.add(0, newInstances.get(0).clone());

        new WorkoutInstanceUpdater(mockDao, 1, newInstances).update();

        final List<WorkoutInstance> result =
                mockDao.getAllWorkoutInstancesForProgram(1);
        final int[] woNumbers = StreamSupport
                .stream(result)
                .mapToInt(WorkoutInstance::getWorkoutNumber)
                .sorted()
                .toArray();

        assertThat(woNumbers).isEqualTo(new int[]{1, 2, 3, 4, 5, 6});
    }

    @Test
    public void multipleInsertedDuplicates() {
        final List<WorkoutInstance> mockList =
                mockDao.getAllWorkoutInstancesForProgram(1);
        final List<WorkoutInstance> newInstances = StreamSupport
                .stream(mockList)
                .map(WorkoutInstance::clone)
                .collect(Collectors.toList());

        newInstances.add(0, newInstances.get(0).clone());
        newInstances.add(newInstances.size()-1,
                newInstances.get(newInstances.size()-1).clone());
        newInstances.add(0, newInstances.get(0).clone());
        newInstances.add(newInstances.size()-1,
                newInstances.get(newInstances.size()-1).clone());

        new WorkoutInstanceUpdater(mockDao, 1, newInstances)
                .update();
        final List<WorkoutInstance> result =
                mockDao.getAllWorkoutInstancesForProgram(1);
        final int[] woNumbers = StreamSupport
                .stream(result)
                .mapToInt(WorkoutInstance::getWorkoutNumber)
                .sorted()
                .toArray();

        assertThat(woNumbers).isEqualTo(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
    }

    @Test
    public void deleteElements() {
        final List<WorkoutInstance> mockList =
                mockDao.getAllWorkoutInstancesForProgram(1);

        final List<WorkoutInstance> newInstances = StreamSupport
                .stream(mockList)
                .map(WorkoutInstance::clone)
                .collect(Collectors.toList());

        newInstances.remove(newInstances.size()-1);
        newInstances.remove(0);
        newInstances.remove(newInstances.size()/2);

        new WorkoutInstanceUpdater(mockDao, 1, newInstances)
                .update();

        final List<WorkoutInstance> result =
                mockDao.getAllWorkoutInstancesForProgram(1);

        final int[] woNumbers = StreamSupport.stream(result)
                .mapToInt(WorkoutInstance::getWorkoutNumber)
                .sorted()
                .toArray();

        assertThat(woNumbers).isEqualTo(new int[]{1, 2});
    }
}