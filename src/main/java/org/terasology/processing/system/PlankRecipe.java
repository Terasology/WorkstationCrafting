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
import org.terasology.workstationCrafting.system.recipe.behaviour.ConsumeItemCraftBehaviour;
import org.terasology.workstationCrafting.system.recipe.behaviour.InventorySlotResolver;
import org.terasology.workstationCrafting.system.recipe.behaviour.InventorySlotTypeResolver;
import org.terasology.workstationCrafting.system.recipe.behaviour.ReduceDurabilityCraftBehaviour;
import org.terasology.workstationCrafting.system.recipe.render.result.ItemRecipeResultFactory;
import org.terasology.workstationCrafting.system.recipe.workstation.AbstractWorkstationRecipe;
import org.terasology.workstationCrafting.system.recipe.workstation.CraftingStationIngredientPredicate;
import org.terasology.workstationCrafting.system.recipe.workstation.CraftingStationToolPredicate;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.logic.common.DisplayNameComponent;
import org.terasology.rendering.nui.layers.ingame.inventory.ItemIcon;
import org.terasology.utilities.Assets;
import org.terasology.processing.component.TreeTypeComponent;

import java.util.List;

/**
 * This class represents the recipe used to craft planks.
 */
public class PlankRecipe extends AbstractWorkstationRecipe {
    public PlankRecipe(int plankCount) {
        Predicate<EntityRef> woodPredicate = new CraftingStationIngredientPredicate("WorkstationCrafting:wood");
        Predicate<EntityRef> axePredicate = new CraftingStationToolPredicate("axe");

        addIngredientBehaviour(new ConsumeWoodIngredientBehaviour(woodPredicate, 1, new InventorySlotTypeResolver("INPUT")));
        addToolBehaviour(new ReduceDurabilityCraftBehaviour(axePredicate, 1, new InventorySlotTypeResolver("TOOL")));

        setResultFactory(new PlankRecipeResultFactory(Assets.getPrefab("WorkstationCrafting:WoodPlank").get(), plankCount));
    }

    private final class PlankRecipeResultFactory extends ItemRecipeResultFactory {
        private PlankRecipeResultFactory(Prefab prefab, int count) {
            super(prefab, count);
        }

        @Override
        public void setupDisplay(List<String> parameters, ItemIcon itemIcon) {
            super.setupDisplay(parameters, itemIcon);

            final String[] split = parameters.get(0).split("\\|");
            if (split.length > 1) {
                itemIcon.setTooltip(split[1] + " Plank");
            }
        }

        @Override
        public EntityRef createResult(List<String> parameters, int multiplier) {
            final EntityRef result = super.createResult(parameters, multiplier);
            final String woodParameter = parameters.get(0);
            final String[] split = woodParameter.split("\\|");
            if (split.length > 0) {
                String treeType = split[1];

                DisplayNameComponent displayName = result.getComponent(DisplayNameComponent.class);
                displayName.name = treeType + " Plank";
                result.saveComponent(displayName);

                TreeTypeComponent treeTypeComponent = new TreeTypeComponent();
                treeTypeComponent.treeType = treeType;
                result.addComponent(treeTypeComponent);
            }

            return result;
        }
    }

    private final class ConsumeWoodIngredientBehaviour extends ConsumeItemCraftBehaviour {
        private ConsumeWoodIngredientBehaviour(Predicate<EntityRef> matcher, int count, InventorySlotResolver resolver) {
            super(matcher, count, resolver);
        }

        @Override
        protected String getParameter(List<Integer> slots, EntityRef item) {
            final TreeTypeComponent treeType = item.getComponent(TreeTypeComponent.class);
            if (treeType != null) {
                return super.getParameter(slots, item) + "|" + treeType.treeType;
            } else {
                return super.getParameter(slots, item);
            }
        }

        @Override
        protected List<Integer> getSlots(String parameter) {
            final String[] split = parameter.split("\\|");
            return super.getSlots(split[0]);
        }
    }
}
