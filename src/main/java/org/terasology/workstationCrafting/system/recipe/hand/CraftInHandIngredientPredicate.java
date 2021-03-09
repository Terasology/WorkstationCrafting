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
package org.terasology.workstationCrafting.system.recipe.hand;

import com.google.common.base.Predicate;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.workstationCrafting.component.CraftInHandIngredientComponent;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class CraftInHandIngredientPredicate implements Predicate<EntityRef> {
    private String itemType;

    public CraftInHandIngredientPredicate(String itemType) {
        this.itemType = itemType;
    }

    @Override
    public boolean apply(EntityRef input) {
        CraftInHandIngredientComponent craftComponent = input.getComponent(CraftInHandIngredientComponent.class);
        return craftComponent != null && craftComponent.componentType.equals(itemType);
    }
}
