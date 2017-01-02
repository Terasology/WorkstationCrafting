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
package org.terasology.processing.ui;

import org.terasology.engine.Time;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.heat.component.HeatProcessedComponent;
import org.terasology.heat.component.HeatProducerComponent;
import org.terasology.heat.ui.ThermometerWidget;
import org.terasology.logic.inventory.InventoryUtils;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.nui.BaseInteractionScreen;
import org.terasology.rendering.nui.databinding.Binding;
import org.terasology.rendering.nui.layers.ingame.inventory.InventoryGrid;
import org.terasology.rendering.nui.widgets.UILoadBar;
import org.terasology.workstation.component.WorkstationInventoryComponent;
import org.terasology.workstation.component.WorkstationProcessingComponent;
import org.terasology.workstation.process.WorkstationInventoryUtils;

import java.util.List;

/**
 * The window used to interact with furnaces.
 */
public class FurnaceWindow extends BaseInteractionScreen {
    private InventoryGrid input;
    private InventoryGrid fuel;
    private InventoryGrid output;
    private ThermometerWidget heat;
    private VerticalTextureProgressWidget burn;
    private UILoadBar craftingProgress;

    /**
     * Initialize variables required by the window.
     */
    @Override
    public void initialise() {
        input = find("input", InventoryGrid.class);
        fuel = find("fuel", InventoryGrid.class);
        output = find("output", InventoryGrid.class);
        craftingProgress = find("craftingProgress", UILoadBar.class);

        InventoryGrid player = find("player", InventoryGrid.class);
        player.setTargetEntity(CoreRegistry.get(LocalPlayer.class).getCharacterEntity());
        player.setCellOffset(10);
        player.setMaxCellCount(30);

        heat = find("heat", ThermometerWidget.class);

        burn = find("burn", VerticalTextureProgressWidget.class);
        burn.setMinY(76);
        burn.setMaxY(4);
    }

    /**
     * Initialize variables required by the window with a given workstation as the interaction target.
     *
     * @param workstation The workstation to be used as an interaction target
     */
    @Override
    protected void initializeWithInteractionTarget(final EntityRef workstation) {
        WorkstationScreenUtils.setupInventoryGrid(workstation, input, "INPUT");
        WorkstationScreenUtils.setupInventoryGrid(workstation, fuel, "FUEL");
        WorkstationScreenUtils.setupInventoryGrid(workstation, output, "OUTPUT");

        WorkstationScreenUtils.setupTemperatureWidget(workstation, heat, 20f);
        heat.bindMarkedTemperature(
                new Binding<Float>() {
                    @Override
                    public Float get() {
                        WorkstationInventoryComponent workstationInventory = workstation.getComponent(WorkstationInventoryComponent.class);
                        if (workstationInventory != null) {
                            for (int slot : WorkstationInventoryUtils.getAssignedSlots(workstation, "INPUT")) {
                                HeatProcessedComponent heatProcessed = InventoryUtils.getItemAt(workstation, slot).getComponent(HeatProcessedComponent.class);
                                if (heatProcessed != null) {
                                    return heatProcessed.heatRequired;
                                }
                            }
                        }
                        return null;
                    }

                    @Override
                    public void set(Float value) {
                    }
                });

        burn.bindValue(
                new Binding<Float>() {
                    @Override
                    public Float get() {
                        HeatProducerComponent heatProducer = workstation.getComponent(HeatProducerComponent.class);
                        List<HeatProducerComponent.FuelSourceConsume> consumedFuel = heatProducer.fuelConsumed;
                        if (consumedFuel.size() == 0) {
                            return 0f;
                        }
                        long gameTime = CoreRegistry.get(Time.class).getGameTimeInMs();

                        HeatProducerComponent.FuelSourceConsume lastConsumed = consumedFuel.get(consumedFuel.size() - 1);
                        if (gameTime > lastConsumed.startTime + lastConsumed.burnLength) {
                            return 0f;
                        }
                        return 1f - (1f * (gameTime - lastConsumed.startTime) / lastConsumed.burnLength);
                    }

                    @Override
                    public void set(Float value) {
                    }
                });

        craftingProgress.bindVisible(
                new Binding<Boolean>() {
                    @Override
                    public Boolean get() {
                        WorkstationProcessingComponent processing = workstation.getComponent(WorkstationProcessingComponent.class);
                        if (processing == null) {
                            return false;
                        }
                        WorkstationProcessingComponent.ProcessDef heatingProcess = processing.processes.get("Machines:Heater");
                        return heatingProcess != null;
                    }

                    @Override
                    public void set(Boolean value) {
                    }
                }
        );
        craftingProgress.bindValue(
                new Binding<Float>() {
                    @Override
                    public Float get() {
                        WorkstationProcessingComponent processing = workstation.getComponent(WorkstationProcessingComponent.class);
                        if (processing == null) {
                            return 1f;
                        }
                        WorkstationProcessingComponent.ProcessDef heatingProcess = processing.processes.get("Machines:Heater");
                        if (heatingProcess == null) {
                            return 1f;
                        }

                        long gameTime = CoreRegistry.get(Time.class).getGameTimeInMs();

                        return 1f * (gameTime - heatingProcess.processingStartTime) / (heatingProcess.processingFinishTime - heatingProcess.processingStartTime);
                    }

                    @Override
                    public void set(Float value) {
                    }
                }
        );
    }

    /**
     * Check if the window is modal. Returns false.
     *
     * @return Whether the window is modal. In this case, the method always returns false.
     */
    @Override
    public boolean isModal() {
        return false;
    }
}
