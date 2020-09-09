// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.processing.system;

import org.terasology.durability.events.ReduceDurabilityEvent;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.common.ActivateEvent;
import org.terasology.engine.math.Side;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.WorldProvider;
import org.terasology.engine.world.block.Block;
import org.terasology.engine.world.block.BlockManager;
import org.terasology.math.geom.Vector3i;
import org.terasology.processing.component.HoeComponent;
import org.terasology.processing.component.TillableComponent;

/**
 * This system defines how hoes work in the game.
 */
@RegisterSystem(RegisterMode.AUTHORITY)
public class HoeUseSystem extends BaseComponentSystem {
    @In
    private WorldProvider worldProvider;
    @In
    private BlockManager blockManager;

    private Block tillEarthBlock;

    /**
     * Set up variables used by the system, namely the block corresponding to tilled earth.
     */
    @Override
    public void preBegin() {
        tillEarthBlock = blockManager.getBlock("WorkstationCrafting:TilledEarth");
    }

    /**
     * Defines what happens when a hoe is used.
     *
     * @param event The event corresponding to the hoe being used
     * @param item An entity reference to the hoe
     * @param hoeComponent The hoe component on the hoe
     */
    @ReceiveEvent
    public void hoeUsed(ActivateEvent event, EntityRef item, HoeComponent hoeComponent) {
        EntityRef target = event.getTarget();
        // Clicked on top of soil
        if (Side.inDirection(event.getHitNormal()) == Side.TOP && target.hasComponent(TillableComponent.class)) {
            worldProvider.setBlock(new Vector3i(event.getTargetLocation(), 0.5f), tillEarthBlock);
            item.send(new ReduceDurabilityEvent(1));
        }
    }
}
