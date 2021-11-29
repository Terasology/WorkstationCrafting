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
package org.terasology.workstationCrafting.system.recipe.render.result;

import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.registry.CoreRegistry;
import org.terasology.module.inventory.ui.ItemIcon;
import org.terasology.engine.utilities.Assets;
import org.terasology.engine.world.block.Block;
import org.terasology.engine.world.block.items.BlockItemFactory;
import org.terasology.workstationCrafting.system.recipe.render.RecipeResultFactory;

import java.util.List;

public class BlockRecipeResultFactory implements RecipeResultFactory {
    private Block block;
    private int count;

    protected BlockRecipeResultFactory(int count) {
        this(null, count);
    }

    public BlockRecipeResultFactory(Block block, int count) {
        this.block = block;
        this.count = count;
    }

    @Override
    public int getMaxMultiplier(List<String> parameters) {
        if (getBlock(parameters).isStackable()) {
            return 99 / count;
        } else {
            return 1;
        }
    }

    protected Block getBlock(List<String> parameters) {
        return block;
    }

    @Override
    public EntityRef createResult(List<String> parameters, int multiplier) {
        return new BlockItemFactory(CoreRegistry.get(EntityManager.class)).newInstance(getBlock(parameters).getBlockFamily(), count * multiplier);
    }

    @Override
    public int getCount(List<String> parameters) {
        return count;
    }

    @Override
    public void setupDisplay(List<String> parameters, ItemIcon itemIcon) {
        Block blockToDisplay = getBlock(parameters);
        itemIcon.setMesh(blockToDisplay.getMeshGenerator().getStandaloneMesh());
        itemIcon.setMeshTexture(Assets.getTexture("engine:terrain").get());
        itemIcon.setTooltip(blockToDisplay.getDisplayName());
    }
}
