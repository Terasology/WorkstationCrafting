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
package org.terasology.heat.processPart;

import org.terasology.engine.Time;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.heat.component.HeatFuelComponent;
import org.terasology.heat.component.HeatProducerComponent;
import org.terasology.logic.inventory.InventoryManager;
import org.terasology.logic.inventory.InventoryUtils;
import org.terasology.registry.In;
import org.terasology.workstation.component.SpecificInputSlotComponent;
import org.terasology.workstation.component.WorkstationInventoryComponent;
import org.terasology.workstation.event.WorkstationStateChanged;
import org.terasology.workstation.process.WorkstationInventoryUtils;
import org.terasology.workstation.processPart.ProcessEntityFinishExecutionEvent;
import org.terasology.workstation.processPart.ProcessEntityGetDurationEvent;
import org.terasology.workstation.processPart.ProcessEntityIsInvalidToStartEvent;
import org.terasology.workstation.processPart.ProcessEntityStartExecutionEvent;
import org.terasology.workstation.processPart.inventory.ProcessEntityIsInvalidForInventoryItemEvent;

/**
 * Contains the common workings of fueling parts.
 */
@RegisterSystem
public class HeatFuelingProcessPartCommonSystem extends BaseComponentSystem {
    @In
    InventoryManager inventoryManager;
    @In
    Time time;

    ///// Processing

    /**
     * Make sure that the fuel to be processed is valid.
     *
     * @param event                The event that indicates that the entity to process is invalid to start fueling
     * @param processEntity        The fuel entity that is invalid
     * @param heatFuelingComponent The heat fueling component liked with this event
     */
    @ReceiveEvent
    public void validateToStartExecution(ProcessEntityIsInvalidToStartEvent event, EntityRef processEntity,
                                         HeatFuelingComponent heatFuelingComponent) {
        if (event.getWorkstation().hasComponent(WorkstationInventoryComponent.class)) {
            for (int slot : WorkstationInventoryUtils.getAssignedSlots(event.getWorkstation(), "FUEL")) {
                HeatFuelComponent fuel = InventoryUtils.getItemAt(event.getWorkstation(), slot).getComponent(HeatFuelComponent.class);
                if (fuel != null) {
                    processEntity.addComponent(new SpecificInputSlotComponent(slot));
                    return;
                }
            }
        }

        event.consume();
    }

    /**
     * Start fueling process.
     *
     * @param event                The event sent to indicate that the fueling process is to be started
     * @param processEntity        The fuel entity to process
     * @param heatFuelingComponent The heat fueling component linked to this event
     */
    @ReceiveEvent
    public void startExecution(ProcessEntityStartExecutionEvent event, EntityRef processEntity,
                               HeatFuelingComponent heatFuelingComponent) {
        SpecificInputSlotComponent input = processEntity.getComponent(SpecificInputSlotComponent.class);
        EntityRef item = InventoryUtils.getItemAt(event.getWorkstation(), input.slot);
        HeatFuelComponent fuel = item.getComponent(HeatFuelComponent.class);

        long startTime = time.getGameTimeInMs();

        if (inventoryManager.removeItem(event.getWorkstation(), event.getInstigator(), item, true, 1) != null) {
            HeatProducerComponent producer = event.getWorkstation().getComponent(HeatProducerComponent.class);
            HeatProducerComponent.FuelSourceConsume fuelSource = new HeatProducerComponent.FuelSourceConsume();
            fuelSource.startTime = startTime;
            fuelSource.burnLength = fuel.consumeTime;
            fuelSource.heatProvided = fuel.heatProvided;
            producer.fuelConsumed.add(fuelSource);
            event.getWorkstation().saveComponent(producer);
        }
    }

    /**
     * Get the duration for which a fuel is being consumed. This method does not return the value, but rather adds the
     * value to an event passed to the method.
     *
     * @param event                The event corresponding to a request sent to get the duration of fuel consumption
     *                             The value of the duration will be added to this event by the method
     * @param processEntity        The fuel entity being processed
     * @param heatFuelingComponent The heat fueling component linked to this event
     */
    @ReceiveEvent
    public void getDuration(ProcessEntityGetDurationEvent event, EntityRef processEntity,
                            HeatFuelingComponent heatFuelingComponent) {
        SpecificInputSlotComponent input = processEntity.getComponent(SpecificInputSlotComponent.class);
        HeatFuelComponent fuel = InventoryUtils.getItemAt(event.getWorkstation(), input.slot).getComponent(HeatFuelComponent.class);
        if (fuel != null) {
            event.add(fuel.consumeTime / 1000f);
        }
    }

    /**
     * Finish the fueling process.
     *
     * @param event                The event sent to indicate that the execution is to be ended
     * @param entityRef            The fuel entity which has been processed
     * @param heatFuelingComponent The heat fueling component linked to this event
     */
    @ReceiveEvent
    public void finishExecution(ProcessEntityFinishExecutionEvent event, EntityRef entityRef,
                                HeatFuelingComponent heatFuelingComponent) {
        event.getWorkstation().send(new WorkstationStateChanged());
    }

    ///// Inventory

    @ReceiveEvent
    public void isValidInventoryItem(ProcessEntityIsInvalidForInventoryItemEvent event, EntityRef processEntity,
                                     HeatFuelingComponent heatFuelingComponent) {
        if (WorkstationInventoryUtils.getAssignedSlots(event.getWorkstation(), "FUEL").contains(event.getSlotNo())
                && !event.getItem().hasComponent(HeatFuelComponent.class)) {
            event.consume();
        }
    }
}
