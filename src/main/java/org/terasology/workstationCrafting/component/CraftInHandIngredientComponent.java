// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.workstationCrafting.component;

import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

public class CraftInHandIngredientComponent implements Component<CraftInHandIngredientComponent> {
    @Replicate
    public String componentType;

    @Override
    public void copyFrom(CraftInHandIngredientComponent other) {
        this.componentType = other.componentType;
    }
}
