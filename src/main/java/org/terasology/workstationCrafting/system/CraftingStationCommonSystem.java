// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.workstationCrafting.system;

import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.In;
import org.terasology.workstation.system.WorkstationRegistry;
import org.terasology.workstationCrafting.component.CraftingStationUpgradeRecipeComponent;

@RegisterSystem
public class CraftingStationCommonSystem extends BaseComponentSystem {
    @In
    private WorkstationRegistry recipeRegistry;

    @Override
    public void initialise() {
        recipeRegistry.registerProcessFactory(CraftingStationUpgradeRecipeComponent.PROCESS_TYPE,
                new CraftingWorkstationUpgradeProcessFactory());
    }
}
