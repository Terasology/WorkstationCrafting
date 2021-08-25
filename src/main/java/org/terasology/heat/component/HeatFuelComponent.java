// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.heat.component;

import org.terasology.engine.network.Replicate;
import org.terasology.engine.world.block.items.AddToBlockBasedItem;
import org.terasology.gestalt.entitysystem.component.Component;

/**
 * Indicates that an entity can be used as fuel to generate heat.
 */
@AddToBlockBasedItem
public class HeatFuelComponent implements Component<HeatFuelComponent> {
    /** The amount of heat provided by the fuel every update. */
    public float heatProvided;

    /** The amount of time taken to completely consume the fuel. */
    @Replicate
    public long consumeTime;

    @Override
    public void copyFrom(HeatFuelComponent other) {
        this.heatProvided = other.heatProvided;
        this.consumeTime = other.consumeTime;
    }
}
