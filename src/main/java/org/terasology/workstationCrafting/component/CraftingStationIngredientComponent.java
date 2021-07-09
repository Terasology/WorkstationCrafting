// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.workstationCrafting.component;

import org.terasology.engine.network.Replicate;
import org.terasology.engine.world.block.items.AddToBlockBasedItem;
import org.terasology.gestalt.entitysystem.component.Component;

@AddToBlockBasedItem
public class CraftingStationIngredientComponent implements Component<CraftingStationIngredientComponent> {
    @Replicate
    public String type;

    @Override
    public void copy(CraftingStationIngredientComponent other) {
        this.type = other.type;
    }
}
