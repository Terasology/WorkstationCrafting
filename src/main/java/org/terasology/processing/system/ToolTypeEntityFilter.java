// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.processing.system;

import com.google.common.base.Predicate;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.workstationCrafting.component.CraftingStationToolComponent;

/**
 * An entity filter that returns true only when an item with a specific tool type is passed.
 */
public class ToolTypeEntityFilter implements Predicate<EntityRef> {
    private final String toolType;

    public ToolTypeEntityFilter(String toolType) {
        this.toolType = toolType;
    }

    /**
     * Applies the entity filter to a given item.
     *
     * @param item The item to apply the filter to
     * @return Returns true if the given item is of a specific tool type
     */
    @Override
    public boolean apply(EntityRef item) {
        CraftingStationToolComponent tool = item.getComponent(CraftingStationToolComponent.class);
        return tool != null && tool.type.contains(toolType);
    }
}
