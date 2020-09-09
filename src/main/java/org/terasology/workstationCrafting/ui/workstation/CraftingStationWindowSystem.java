// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.workstationCrafting.ui.workstation;

import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.nui.NUIManager;
import org.terasology.inventory.logic.events.BeforeItemPutInInventory;
import org.terasology.inventory.logic.events.BeforeItemRemovedFromInventory;
import org.terasology.inventory.logic.events.InventorySlotStackSizeChangedEvent;
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
        CraftingStationWindow screen = (CraftingStationWindow) nuiManager.getScreen("WorkstationCrafting" +
                ":CraftInStation");

        if (screen != null) {
            screen.updateAvailableRecipes();
        }
    }

    @ReceiveEvent
    public void itemPutIntoInventorySlot(InventorySlotStackSizeChangedEvent event, EntityRef entity,
                                         WorkstationInventoryComponent inventory) {
        CraftingStationWindow screen = (CraftingStationWindow) nuiManager.getScreen("WorkstationCrafting" +
                ":CraftInStation");

        if (screen != null) {
            screen.updateAvailableRecipes();
        }
    }

    @ReceiveEvent
    public void itemRemovedFromInventorySlot(BeforeItemRemovedFromInventory event, EntityRef entity,
                                             WorkstationInventoryComponent inventory) {
        CraftingStationWindow screen = (CraftingStationWindow) nuiManager.getScreen("WorkstationCrafting" +
                ":CraftInStation");

        if (screen != null) {
            screen.updateAvailableRecipes();
        }
    }

    @ReceiveEvent
    public void onCraftingStationUpgraded(CraftingStationUpgraded event, EntityRef entity,
                                          WorkstationInventoryComponent inventory) {
        CraftingStationWindow screen = (CraftingStationWindow) nuiManager.getScreen("WorkstationCrafting" +
                ":CraftInStation");

        if (screen != null) {
            screen.updateAvailableRecipes();
        }
    }
}
