/*
 * Copyright 2016 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.workstationCrafting.system;

import com.google.common.collect.Lists;
import org.terasology.entitySystem.entity.EntityBuilder;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.protobuf.EntityData;
import org.terasology.registry.In;
import org.terasology.workstation.process.DescribeProcess;
import org.terasology.workstation.process.ProcessPartDescription;
import org.terasology.workstation.processPart.metadata.ProcessEntityGetInputDescriptionEvent;
import org.terasology.workstation.processPart.metadata.ProcessEntityGetOutputDescriptionEvent;
import org.terasology.workstationCrafting.component.CraftingProcessComponent;
import org.terasology.workstationCrafting.event.CraftingWorkstationProcessRequest;
import org.terasology.workstationCrafting.system.recipe.workstation.CraftingStationRecipe;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.logic.inventory.InventoryManager;
import org.terasology.registry.CoreRegistry;
import org.terasology.workstation.event.WorkstationProcessRequest;
import org.terasology.workstation.process.InvalidProcessException;
import org.terasology.workstation.process.WorkstationInventoryUtils;
import org.terasology.workstation.process.WorkstationProcess;
import org.terasology.workstation.process.fluid.ValidateFluidInventoryItem;
import org.terasology.workstation.system.ValidateInventoryItem;

import java.util.Collection;
import java.util.List;

public class CraftingWorkstationProcess implements WorkstationProcess, ValidateInventoryItem, ValidateFluidInventoryItem, DescribeProcess {
    private String processType;
    private String craftingRecipeId;
    private CraftingStationRecipe recipe;
    private Prefab prefab;

    private EntityManager entityManager;

    public CraftingWorkstationProcess(String processType, String craftingRecipeId, CraftingStationRecipe recipe) {
        this.processType = processType;
        this.craftingRecipeId = craftingRecipeId;
        this.recipe = recipe;
    }

    public CraftingWorkstationProcess(String processType, String craftingRecipeId, CraftingStationRecipe recipe,
                                      Prefab prefab, EntityManager entityManager) {
        this.processType = processType;
        this.craftingRecipeId = craftingRecipeId;
        this.recipe = recipe;
        this.prefab = prefab;
        this.entityManager = entityManager;
    }

    @Override
    public boolean isValid(EntityRef workstation, int slotNo, EntityRef instigator, EntityRef item) {
        if (WorkstationInventoryUtils.getAssignedSlots(workstation, "INPUT").contains(slotNo)) {
            return recipe.hasAsComponent(item);
        }
        if (WorkstationInventoryUtils.getAssignedSlots(workstation, "TOOL").contains(slotNo)) {
            return recipe.hasAsTool(item);
        }
        if (WorkstationInventoryUtils.getAssignedSlots(workstation, "OUTPUT").contains(slotNo)) {
            return instigator == workstation;
        }
        return true;
    }

    @Override
    public boolean isValidFluid(EntityRef workstation, int slotNo, EntityRef instigator, String fluidType) {
        if (WorkstationInventoryUtils.getAssignedSlots(workstation, "FLUID_INPUT").contains(slotNo)) {
            return recipe.hasFluidAsComponent(fluidType);
        } else {
            return true;
        }
    }

    @Override
    public String getProcessType() {
        return processType;
    }

    @Override
    public String getId() {
        return craftingRecipeId;
    }

    public CraftingStationRecipe getCraftingWorkstationRecipe() {
        return recipe;
    }

    @Override
    public long startProcessingManual(EntityRef instigator, EntityRef workstation, WorkstationProcessRequest request, EntityRef processEntity) throws InvalidProcessException {
        if (!(request instanceof CraftingWorkstationProcessRequest)) {
            throw new InvalidProcessException();
        }

        final CraftingWorkstationProcessRequest craftingRequest = (CraftingWorkstationProcessRequest) request;
        final List<String> parameters = craftingRequest.getParameters();
        final CraftingStationRecipe.CraftingStationResult result = recipe.getResultByParameters(workstation, parameters);
        if (result == null) {
            throw new InvalidProcessException();
        }

        final int count = craftingRequest.getCount();
        final boolean success = result.startCrafting(workstation, count);
        if (!success) {
            throw new InvalidProcessException();
        }

        CraftingProcessComponent craftingProcess = new CraftingProcessComponent();
        craftingProcess.parameters = parameters;
        craftingProcess.count = count;
        processEntity.addComponent(craftingProcess);

        return result.getProcessDuration();
    }

    @Override
    public long startProcessingAutomatic(EntityRef workstation, EntityRef processEntity) throws InvalidProcessException {
        throw new InvalidProcessException();
    }

    @Override
    public void finishProcessing(EntityRef instigator, EntityRef workstation, EntityRef processEntity) {
        CraftingProcessComponent craftingProcess = processEntity.getComponent(CraftingProcessComponent.class);

        final CraftingStationRecipe.CraftingStationResult result = recipe.getResultByParameters(workstation, craftingProcess.parameters);
        EntityRef resultItem = result.finishCrafting(workstation, craftingProcess.count);
        if (CoreRegistry.get(InventoryManager.class).giveItem(workstation, workstation, resultItem, WorkstationInventoryUtils.getAssignedSlots(workstation, "OUTPUT"))) {
            return;
        }
        resultItem.destroy();
    }

    @Override
    public EntityRef createProcessEntity() {
        return createProcessEntity(true);
    }

    private EntityRef createProcessEntity(boolean persistant) {
        if (entityManager == null || prefab == null) {
            return EntityRef.NULL;
        }

        EntityBuilder builder = entityManager.newBuilder(prefab);
        builder.setPersistent(persistant);
        return builder.build();
    }

    @Override
    public Collection<ProcessPartDescription> getOutputDescriptions() {
        ProcessEntityGetOutputDescriptionEvent event = new ProcessEntityGetOutputDescriptionEvent();
        EntityRef tempProcessEntity = createProcessEntity(false);

        if (!tempProcessEntity.equals(EntityRef.NULL)) {
            tempProcessEntity.send(event);
            tempProcessEntity.destroy();
            return Lists.newLinkedList(event.getOutputDescriptions());
        }

        return Lists.newLinkedList();
    }

    @Override
    public Collection<ProcessPartDescription> getInputDescriptions() {
        ProcessEntityGetInputDescriptionEvent event = new ProcessEntityGetInputDescriptionEvent();
        EntityRef tempProcessEntity = createProcessEntity(false);

        if (!tempProcessEntity.equals(EntityRef.NULL)) {
            tempProcessEntity.send(event);
            tempProcessEntity.destroy();
            return Lists.newLinkedList(event.getInputDescriptions());
        }

        return Lists.newLinkedList();
    }
}
