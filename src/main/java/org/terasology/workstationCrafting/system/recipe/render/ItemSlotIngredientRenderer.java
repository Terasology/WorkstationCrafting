// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.workstationCrafting.system.recipe.render;

import com.google.common.base.Function;
import org.joml.Vector2i;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.logic.common.DisplayNameComponent;
import org.terasology.module.inventory.systems.InventoryUtils;
import org.terasology.engine.logic.inventory.ItemComponent;
import org.terasology.module.inventory.ui.GetItemTooltip;
import org.terasology.module.inventory.ui.ItemIcon;
import org.terasology.engine.utilities.Assets;
import org.terasology.engine.world.block.items.BlockItemComponent;
import org.terasology.joml.geom.Rectanglei;
import org.terasology.nui.Canvas;

public class ItemSlotIngredientRenderer implements CraftIngredientRenderer {
    private ItemIcon itemIcon;
    private Function<Integer, Integer> multiplierFunction;

    public ItemSlotIngredientRenderer() {
        itemIcon = new ItemIcon();
    }

    public void update(EntityRef entity, int slot, Function<Integer, Integer> newMultiplierFunction) {
        multiplierFunction = newMultiplierFunction;

        EntityRef item = InventoryUtils.getItemAt(entity, slot);
        ItemComponent itemComp = item.getComponent(ItemComponent.class);
        BlockItemComponent blockItemComp = item.getComponent(BlockItemComponent.class);
        if (itemComp != null && itemComp.icon != null) {
            itemIcon.setIcon(itemComp.icon);
        } else if (blockItemComp != null) {
            itemIcon.setMesh(blockItemComp.blockFamily.getArchetypeBlock().getMeshGenerator().getStandaloneMesh());
            itemIcon.setMeshTexture(Assets.getTexture("engine:terrain").get());
        }
        GetItemTooltip tooltipEvent;
        DisplayNameComponent displayName = item.getComponent(DisplayNameComponent.class);
        if (displayName != null) {
            tooltipEvent = new GetItemTooltip(displayName.name);
        } else {
            tooltipEvent = new GetItemTooltip();
        }
        item.send(tooltipEvent);

        itemIcon.setTooltipLines(tooltipEvent.getTooltipLines());
    }

    @Override
    public Vector2i getPreferredSize(Canvas canvas, int multiplier) {
        return new Vector2i(56, 56);
    }

    @Override
    public void render(Canvas canvas, Rectanglei region, int multiplier) {
        itemIcon.setQuantity(multiplierFunction.apply(multiplier));
        canvas.drawWidget(itemIcon, region);
    }
}
