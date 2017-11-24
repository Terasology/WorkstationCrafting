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

import org.terasology.inGameHelpAPI.components.ItemHelpComponent;
import org.terasology.logic.common.DisplayNameComponent;
import org.terasology.rendering.nui.widgets.TooltipLine;
import org.terasology.workstationCrafting.system.recipe.render.RecipeResultFactory;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.nui.layers.ingame.inventory.ItemIcon;
import org.terasology.utilities.Assets;
import org.terasology.world.block.Block;
import org.terasology.world.block.items.BlockItemFactory;

import java.util.ArrayList;
import java.util.Arrays;
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

    /**
     * Setup the display (mesh and description) of the recipe's resultant block.
     *
     * @param parameters    A list of parameters AKA inputs required for producing this recipe.
     * @param itemIcon      The block's icon.
     */
    @Override
    public void setupDisplay(List<String> parameters, ItemIcon itemIcon) {
        Block blockToDisplay = getBlock(parameters);
        itemIcon.setMesh(blockToDisplay.getMesh());
        itemIcon.setMeshTexture(Assets.getTexture("engine:terrain").get());

        // Get the block's entity, and use it to set the tooltip lines for this item. This include its name, category,
        // and (short) description.
        EntityRef blockEntity = blockToDisplay.getEntity();
        DisplayNameComponent displayName = blockEntity.getComponent(DisplayNameComponent.class);
        if (displayName != null) {
            ArrayList<TooltipLine> tooltipLines = new ArrayList<>(Arrays.asList(new TooltipLine(displayName.name)));

            // If this block's entity is registered into the InGameHelp system, get its category and add it into the
            // tooltip.
            if (blockEntity.hasComponent(ItemHelpComponent.class)) {
                ItemHelpComponent itemHelp = blockEntity.getComponent(ItemHelpComponent.class);
                tooltipLines.add(new TooltipLine(itemHelp.getCategory()));
            }
            // If this block's entity has a description, add it into the tooltip.
            if (!displayName.description.equals("")) {
                tooltipLines.add(new TooltipLine(displayName.description));
            }

            // Set the block entity's full tooltip or description.
            itemIcon.setTooltipLines(tooltipLines);
        }
    }
}
