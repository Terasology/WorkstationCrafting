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
package org.terasology.workstationCrafting.system;

import org.joml.Vector3f;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.logic.inventory.InventoryManager;
import org.terasology.engine.logic.inventory.events.DropItemEvent;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.registry.In;
import org.terasology.workstationCrafting.event.UserCraftInHandRequest;
import org.terasology.workstationCrafting.system.recipe.hand.CraftInHandRecipe;

import java.util.List;

@RegisterSystem(RegisterMode.AUTHORITY)
public class CraftInHandAuthoritySystem extends BaseComponentSystem {
    @In
    private EntityManager entityManager;
    @In
    private CraftInHandRecipeRegistry recipeRegistry;

    @In
    private InventoryManager inventoryManager;

    @ReceiveEvent
    public void craftInHandRequestReceived(UserCraftInHandRequest event, EntityRef character) {
        if (!recipeRegistry.isCraftingInHandDisabled()) {
            String recipeId = event.getRecipeId();
            final List<String> parameters = event.getParameters();
            CraftInHandRecipe craftInHandRecipe = recipeRegistry.getRecipes().get(recipeId);
            if (craftInHandRecipe != null) {
                CraftInHandRecipe.CraftInHandResult result = craftInHandRecipe.getResultByParameters(parameters);
                if (result != null) {
                    EntityRef resultEntity = result.craft(character, event.getCount());
                    if (resultEntity.exists()) {
                        resultEntity.send(new DropItemEvent(character.getComponent(LocationComponent.class).getWorldPosition(new Vector3f())));
                    }
                }
            }
        }
    }
}
