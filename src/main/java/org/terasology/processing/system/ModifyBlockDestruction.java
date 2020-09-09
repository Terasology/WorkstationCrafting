// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.processing.system;

import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.world.block.BlockUri;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
@RegisterSystem(RegisterMode.AUTHORITY)
public class ModifyBlockDestruction extends BaseComponentSystem {
    private final Set<BlockUri> exceptions = new HashSet<>();

    @Override
    public void initialise() {
        exceptions.add(new BlockUri("CoreAssets:Grass"));
        exceptions.add(new BlockUri("CoreAssets:Dirt"));
        exceptions.add(new BlockUri("CoreAssets:Sand"));
        exceptions.add(new BlockUri("WorkstationCrafting:ClayStone"));
    }

    // TODO: Temporarily removed until this entire class is moved onto NeoTTA.
    /*
    @ReceiveEvent
    public void preventPlayerFromDestroyingBasicBlocksByHand(BeforeDamagedEvent event, EntityRef blockEntity) {
        BlockComponent blockComponent = blockEntity.getComponent(BlockComponent.class);
        if (blockComponent != null && event.getInstigator().hasComponent(CharacterComponent.class)) {
            Block block = blockComponent.getBlock();

            if (exceptions.contains(block.getURI())) {
                return;
            }

            Iterable<String> categoriesIterator = block.getBlockFamily().getCategories();
            if (!canBeDestroyedByBlockDamage(categoriesIterator, event.getDamageType())) {
                event.consume();
            }
        }
    }

    private boolean canBeDestroyedByBlockDamage(Iterable<String> categoriesIterator, Prefab damageType) {
        if (categoriesIterator.iterator().hasNext()) {
            // If this block has a category, then it HAS to be destroyed by a tool with that category
            BlockDamageModifierComponent blockDamage = damageType.getComponent(BlockDamageModifierComponent.class);
            if (blockDamage == null) {
                return false;
            }
            for (String category : categoriesIterator) {
                if (blockDamage.materialDamageMultiplier.containsKey(category)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    */
}
