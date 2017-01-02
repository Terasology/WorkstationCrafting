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

import org.terasology.durability.components.DurabilityComponent;
import org.terasology.durability.events.DurabilityExhaustedEvent;
import org.terasology.durability.components.OverTimeDurabilityReduceComponent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.registry.In;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.BlockComponent;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.items.OnBlockItemPlaced;
import org.terasology.world.block.items.OnBlockToItem;

/**
 * This system defines the workings of torches in the game.
 */
@RegisterSystem
public class LitTorchSystem extends BaseComponentSystem {
    @In
    private BlockManager blockManager;

    /**
     * Defines what to do when an entity places a torch.
     *
     * @param event                             The event corresponding to the torch being placed
     * @param item                              The entity reference of the torch
     * @param overTimeDurabilityReduceComponent The component of the torch that defines how the durability of the torch decays over time
     * @param itemDurability                    The durability of the torch
     */
    @ReceiveEvent
    public void whenTorchPlaced(OnBlockItemPlaced event, EntityRef item,
                                OverTimeDurabilityReduceComponent overTimeDurabilityReduceComponent,
                                DurabilityComponent itemDurability) {
        EntityRef blockEntity = event.getPlacedBlock();
        DurabilityComponent durability = blockEntity.getComponent(DurabilityComponent.class);
        durability.durability = itemDurability.durability;
        blockEntity.saveComponent(durability);
    }

    /**
     * Defines what to do when a torch is removed.
     *
     * @param event                             The event corresponding to the torch being removed
     * @param block                             The block from which the torch was removed
     * @param overTimeDurabilityReduceComponent The component of the torch that defines how the durability of the torch decays over time
     * @param blockDurability                   The durability of the torch
     */
    @ReceiveEvent
    public void whenTorchRemoved(OnBlockToItem event, EntityRef block,
                                 OverTimeDurabilityReduceComponent overTimeDurabilityReduceComponent,
                                 DurabilityComponent blockDurability) {
        EntityRef itemEntity = event.getItem();
        DurabilityComponent durability = itemEntity.getComponent(DurabilityComponent.class);
        durability.durability = blockDurability.durability;
        itemEntity.saveComponent(durability);
    }

    /**
     * Defines what to do when the durability of the torch expires.
     *
     * @param event                             The event corresponding to the torch expiring
     * @param entity                            The entity reference of the torch
     * @param overTimeDurabilityReduceComponent The component of the torch that defines how the durability of the torch decays over time
     * @param block                             The block on which the torch was placed
     */
    @ReceiveEvent
    public void whenTorchAsBlockExpires(DurabilityExhaustedEvent event, EntityRef entity,
                                        OverTimeDurabilityReduceComponent overTimeDurabilityReduceComponent,
                                        BlockComponent block) {
        Vector3i position = block.getPosition();
        CoreRegistry.get(WorldProvider.class).setBlock(position, blockManager.getBlock(BlockManager.AIR_ID));
        entity.removeComponent(DurabilityComponent.class);
        entity.removeComponent(OverTimeDurabilityReduceComponent.class);
        event.consume();
    }
}
