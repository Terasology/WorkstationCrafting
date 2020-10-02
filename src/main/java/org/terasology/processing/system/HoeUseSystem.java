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
package org.terasology.processing.system;

import org.joml.RoundingMode;
import org.joml.Vector3i;
import org.terasology.durability.events.ReduceDurabilityEvent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.common.ActivateEvent;
import org.terasology.math.Side;
import org.terasology.processing.component.HoeComponent;
import org.terasology.processing.component.TillableComponent;
import org.terasology.registry.In;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;

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
     * @param event        The event corresponding to the hoe being used
     * @param item         An entity reference to the hoe
     * @param hoeComponent The hoe component on the hoe
     */
    @ReceiveEvent
    public void hoeUsed(ActivateEvent event, EntityRef item, HoeComponent hoeComponent) {
        EntityRef target = event.getTarget();
        // Clicked on top of soil
        if (Side.inDirection(event.getHitNormal()) == Side.TOP && target.hasComponent(TillableComponent.class)) {
            worldProvider.setBlock(new Vector3i(event.getTargetLocation(), RoundingMode.HALF_UP), tillEarthBlock);
            item.send(new ReduceDurabilityEvent(1));
        }
    }
}
