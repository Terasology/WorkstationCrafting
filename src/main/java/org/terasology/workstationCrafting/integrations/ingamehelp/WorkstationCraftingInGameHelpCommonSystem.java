// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.workstationCrafting.integrations.ingamehelp;

import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.engine.logic.players.LocalPlayer;
import org.terasology.engine.registry.CoreRegistry;
import org.terasology.inGameHelpAPI.event.OnAddNewCategoryEvent;

@RegisterSystem
public class WorkstationCraftingInGameHelpCommonSystem extends BaseComponentSystem implements UpdateSubscriberSystem {
    boolean hasSent = false;

    @Override
    public void preBegin() {
        super.preBegin();
    }

    @Override
    public void update(float delta) {
        if (!hasSent) {
            // Create the category pertaining to this module and send it to other modules through OnAddNewCategoryEvent.
            CoreRegistry.get(LocalPlayer.class).getClientEntity().send(new OnAddNewCategoryEvent(new WorkstationCraftingCategory()));
            hasSent = true;
        }
    }
}
