// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.processing.system;

import org.terasology.durability.components.DurabilityComponent;
import org.terasology.durability.components.OverTimeDurabilityReduceComponent;
import org.terasology.durability.events.DurabilityExhaustedEvent;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.CoreRegistry;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.WorldProvider;
import org.terasology.engine.world.block.BlockComponent;
import org.terasology.engine.world.block.BlockManager;
import org.terasology.engine.world.block.items.OnBlockItemPlaced;
import org.terasology.engine.world.block.items.OnBlockToItem;
import org.terasology.math.geom.Vector3i;

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
     * @param event The event corresponding to the torch being placed
     * @param item The entity reference of the torch
     * @param overTimeDurabilityReduceComponent The component of the torch that defines how the durability of
     *         the torch decays over time
     * @param itemDurability The durability of the torch
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
     * @param event The event corresponding to the torch being removed
     * @param block The block from which the torch was removed
     * @param overTimeDurabilityReduceComponent The component of the torch that defines how the durability of
     *         the torch decays over time
     * @param blockDurability The durability of the torch
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
     * @param event The event corresponding to the torch expiring
     * @param entity The entity reference of the torch
     * @param overTimeDurabilityReduceComponent The component of the torch that defines how the durability of
     *         the torch decays over time
     * @param block The block on which the torch was placed
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
