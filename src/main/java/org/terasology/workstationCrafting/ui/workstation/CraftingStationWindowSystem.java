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
package org.terasology.workstationCrafting.ui.workstation;

import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.inventory.events.BeforeItemPutInInventory;
import org.terasology.engine.logic.inventory.events.BeforeItemRemovedFromInventory;
import org.terasology.engine.logic.inventory.events.InventorySlotStackSizeChangedEvent;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.nui.NUIManager;
import org.terasology.workstation.component.WorkstationInventoryComponent;
import org.terasology.workstationCrafting.event.CraftingStationUpgraded;

@RegisterSystem
public class CraftingStationWindowSystem extends BaseComponentSystem {
    @In
    EntityManager entityManager;

    @In
    private NUIManager nuiManager;

    @Override
    public void initialise() {
    }

    @ReceiveEvent
    public void itemPutIntoInventorySlot(BeforeItemPutInInventory event, EntityRef entity,
                                         WorkstationInventoryComponent inventory) {
        CraftingStationWindow screen = (CraftingStationWindow) nuiManager.getScreen("WorkstationCrafting:CraftInStation");

        if (screen != null) {
            screen.updateAvailableRecipes();
        }
    }

    @ReceiveEvent
    public void itemPutIntoInventorySlot(InventorySlotStackSizeChangedEvent event, EntityRef entity,
                                         WorkstationInventoryComponent inventory) {
        CraftingStationWindow screen = (CraftingStationWindow) nuiManager.getScreen("WorkstationCrafting:CraftInStation");

        if (screen != null) {
            screen.updateAvailableRecipes();
        }
    }

    @ReceiveEvent
    public void itemRemovedFromInventorySlot(BeforeItemRemovedFromInventory event, EntityRef entity,
                                             WorkstationInventoryComponent inventory) {
        CraftingStationWindow screen = (CraftingStationWindow) nuiManager.getScreen("WorkstationCrafting:CraftInStation");

        if (screen != null) {
            screen.updateAvailableRecipes();
        }
    }

    @ReceiveEvent
    public void onCraftingStationUpgraded(CraftingStationUpgraded event, EntityRef entity,
                                         WorkstationInventoryComponent inventory) {
        CraftingStationWindow screen = (CraftingStationWindow) nuiManager.getScreen("WorkstationCrafting:CraftInStation");

        if (screen != null) {
            screen.updateAvailableRecipes();
        }
    }
}
