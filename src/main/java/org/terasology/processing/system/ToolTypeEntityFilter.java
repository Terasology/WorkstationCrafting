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

import com.google.common.base.Predicate;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.workstationCrafting.component.CraftingStationToolComponent;

/**
 * An entity filter that returns true only when an item with a specific tool type is passed.
 */
public class ToolTypeEntityFilter implements Predicate<EntityRef> {
    private String toolType;

    public ToolTypeEntityFilter(String toolType) {
        this.toolType = toolType;
    }

    /**
     * Applies the entity filter to a given item.
     *
     * @param item The item to apply the filter to
     * @return     Returns true if the given item is of a specific tool type
     */
    @Override
    public boolean apply(EntityRef item) {
        CraftingStationToolComponent tool = item.getComponent(CraftingStationToolComponent.class);
        return tool != null && tool.type.contains(toolType);
    }
}
