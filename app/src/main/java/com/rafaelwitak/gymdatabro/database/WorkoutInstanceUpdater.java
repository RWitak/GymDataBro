/*
 * Copyright (c) 2021, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.database;

import androidx.annotation.NonNull;
import java8.util.function.Consumer;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class WorkoutInstanceUpdater {
    private final WorkoutInstanceDAO dao;
    private final List<WorkoutInstance> previousInstances;
    private final List<WorkoutInstance> newInstances;
    private final List<Integer> oldIds;

    public WorkoutInstanceUpdater(@NonNull WorkoutInstanceDAO workoutInstanceDAO,
                                  int programId,
                                  List<WorkoutInstance> newInstances) {
        this.dao = workoutInstanceDAO;

        this.previousInstances = dao.getAllWorkoutInstancesForProgram(programId);
        this.newInstances = newInstances;
        this.oldIds = StreamSupport.stream(previousInstances)
                .map(WorkoutInstance::getId)
                .toList();
    }

    private boolean isObsolete(@NonNull WorkoutInstance instance) {
        return !oldIds.contains(instance.getId());
    }

    private boolean isPreexistingInstance(WorkoutInstance instance) {
        return StreamSupport.stream(previousInstances)
                .filter(old -> old.getWorkoutId() == instance.getWorkoutId())
                .filter(old -> old.getWorkoutNumber() == instance.getWorkoutNumber())
                .anyMatch(old -> old.getId() == instance.getId());
    }

    private WorkoutInstance getIdKeeper(List<WorkoutInstance> instances) {
        return StreamSupport.stream(instances)
                .filter(this::isPreexistingInstance)
                .findAny()
                .orElse(instances.get(0));
    }

    private boolean isDatasetUnchanged() {
        return newInstances.equals(previousInstances);
    }

    /**
     * Updates
     */
    public void update() {
        if (isDatasetUnchanged()) {
            return;
        }

        deleteUnusedInstancesInDatabase();
        updateWorkoutOrder();
        updateDatabase();
    }

    private void updateDatabase() {
        Map<WorkoutInstance, Consumer<WorkoutInstance>> procedureMap =
                assignDbProceduresToInstances();

        StreamSupport.stream(procedureMap.entrySet())
                .forEach(entry -> entry.getValue().accept(entry.getKey()));
    }

    @NonNull
    private Map<WorkoutInstance, Consumer<WorkoutInstance>> assignDbProceduresToInstances() {
        final Set<Map.Entry<Integer, List<WorkoutInstance>>> instancesById =
                instancesGroupedById().entrySet();

        Map<WorkoutInstance, Consumer<WorkoutInstance>> procedureMap =
                new HashMap<>();

        for (Map.Entry<Integer, List<WorkoutInstance>> entry : instancesById) {
            final int id = entry.getKey();
            final List<WorkoutInstance> sameIdList = entry.getValue();
            final WorkoutInstance keepsId = getIdKeeper(sameIdList);

            procedureMap.put(keepsId,
                    oldIds.contains(id)
                            ? dao::updateWorkoutInstance
                            : dao::insertWorkoutInstanceForId);

            sameIdList.remove(keepsId);
            StreamSupport.stream(sameIdList)
                    .forEach(instance -> procedureMap.put(instance,
                            dao::insertDuplicate));
        }
        return procedureMap;
    }

    private Map<Integer, List<WorkoutInstance>> instancesGroupedById() {
        return StreamSupport.stream(newInstances)
                .collect(Collectors.groupingBy(WorkoutInstance::getId));
    }

    private void updateWorkoutOrder() {
//          List::indexOf does NOT work here, as duplicates might exist!
        for (int i = 0; i < newInstances.size(); i++) {
            newInstances.get(i).setWorkoutNumber(i + 1);
        }
    }

    private void deleteUnusedInstancesInDatabase() {
        StreamSupport.stream(previousInstances)
                .filter(this::isObsolete)
                .forEach(dao::deleteWorkoutInstance);
        // FIXME: 02.07.2021 Leaves orphans if deletion doesn't cascade.
    }
}
