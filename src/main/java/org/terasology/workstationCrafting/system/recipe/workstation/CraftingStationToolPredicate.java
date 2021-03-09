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
package org.terasology.workstationCrafting.system.recipe.workstation;

import com.google.common.base.Predicate;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.workstationCrafting.component.CraftingStationToolComponent;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class CraftingStationToolPredicate implements Predicate<EntityRef> {
    private String toolType;

    public CraftingStationToolPredicate(String toolType) {
        this.toolType = toolType;
    }

    @Override
    public boolean apply(EntityRef input) {
        CraftingStationToolComponent component = input.getComponent(CraftingStationToolComponent.class);
        return component != null && component.type.contains(toolType);
    }
}
