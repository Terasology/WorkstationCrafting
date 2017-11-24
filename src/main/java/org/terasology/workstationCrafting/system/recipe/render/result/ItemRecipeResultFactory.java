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
import org.terasology.rendering.nui.widgets.TooltipLine;
import org.terasology.workstationCrafting.system.recipe.render.RecipeResultFactory;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.logic.common.DisplayNameComponent;
import org.terasology.logic.inventory.ItemComponent;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.nui.layers.ingame.inventory.ItemIcon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemRecipeResultFactory implements RecipeResultFactory {
    protected Prefab prefab;
    private int count;

    public ItemRecipeResultFactory(Prefab prefab, int count) {
        this.prefab = prefab;
        this.count = count;
    }

    @Override
    public int getMaxMultiplier(List<String> parameters) {
        final ItemComponent item = prefab.getComponent(ItemComponent.class);
        if (item.stackId == null || item.stackId.isEmpty()) {
            return 1;
        } else {
            return item.maxStackSize / count;
        }
    }

    @Override
    public EntityRef createResult(List<String> parameters, int multiplier) {
        final EntityRef entity = CoreRegistry.get(EntityManager.class).create(prefab);
        final ItemComponent item = entity.getComponent(ItemComponent.class);
        item.stackCount = (byte) (count * multiplier);
        entity.saveComponent(item);
        return entity;
    }

    @Override
    public int getCount(List<String> parameters) {
        return count;
    }

    /**
     * Setup the display (icon and description) of the recipe's resultant item.
     *
     * @param parameters    A list of parameters AKA inputs required for producing this recipe.
     * @param itemIcon      The item's icon.
     */
    @Override
    public void setupDisplay(List<String> parameters, ItemIcon itemIcon) {
        // Get the ItemComponent, and use it to set the item's icon.
        ItemComponent item = prefab.getComponent(ItemComponent.class);
        itemIcon.setIcon(item.icon);

        // Set the tooltip lines for this item. This include its name, category, and (short) description. That is, as
        // long as it has a name.
        DisplayNameComponent displayName = prefab.getComponent(DisplayNameComponent.class);
        if (displayName != null) {
            ArrayList<TooltipLine> tooltipLines = new ArrayList<>(Arrays.asList(new TooltipLine(displayName.name)));

            // If this prefab is registered into the InGameHelp system, get its category and add it into the tooltip.
            if (prefab.hasComponent(ItemHelpComponent.class))
            {
                ItemHelpComponent itemHelp = prefab.getComponent(ItemHelpComponent.class);
                tooltipLines.add(new TooltipLine(itemHelp.getCategory()));
            }
            // If this prefab has a description, add it into the tooltip.
            if (!displayName.description.equals("")) {
                tooltipLines.add(new TooltipLine(displayName.description));
            }

            // Set the item prefab's full tooltip or description.
            itemIcon.setTooltipLines(tooltipLines);
        }
    }
}