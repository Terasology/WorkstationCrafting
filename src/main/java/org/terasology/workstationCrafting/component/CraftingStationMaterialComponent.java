// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.workstationCrafting.component;

import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

public class CraftingStationMaterialComponent implements Component<CraftingStationMaterialComponent> {
    @Replicate
    public String stationType;

    @Override
    public void copy(CraftingStationMaterialComponent other) {
        this.stationType = other.stationType;
    }
}
