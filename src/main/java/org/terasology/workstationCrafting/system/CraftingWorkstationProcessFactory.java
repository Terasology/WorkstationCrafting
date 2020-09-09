// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.workstationCrafting.system;

import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.entitySystem.prefab.PrefabManager;
import org.terasology.engine.registry.CoreRegistry;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.block.BlockManager;
import org.terasology.workstation.component.ProcessDefinitionComponent;
import org.terasology.workstation.process.WorkstationProcess;
import org.terasology.workstation.system.WorkstationProcessFactory;
import org.terasology.workstationCrafting.component.CraftingStationRecipeComponent;
import org.terasology.workstationCrafting.system.recipe.render.result.BlockRecipeResultFactory;
import org.terasology.workstationCrafting.system.recipe.render.result.ItemRecipeResultFactory;
import org.terasology.workstationCrafting.system.recipe.workstation.DefaultWorkstationRecipe;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class CraftingWorkstationProcessFactory implements WorkstationProcessFactory {
    @In
    private EntityManager entityManager;

    @Override
    public WorkstationProcess createProcess(Prefab prefab) {
        ProcessDefinitionComponent process = prefab.getComponent(ProcessDefinitionComponent.class);

        CraftingStationRecipeComponent recipe = prefab.getComponent(CraftingStationRecipeComponent.class);

        DefaultWorkstationRecipe workstationRecipe = new DefaultWorkstationRecipe();
        if (recipe.recipeComponents != null) {
            for (String recipeComponent : recipe.recipeComponents) {
                String[] split = recipeComponent.split("\\*", 2);
                int count = Integer.parseInt(split[0]);
                String type = split[1];
                workstationRecipe.addIngredient(type, count);
            }
        }
        if (recipe.optionalRecipeComponents != null) {
            for (String recipeComponent : recipe.optionalRecipeComponents) {
                String[] split = recipeComponent.split("\\*", 2);
                int count = Integer.parseInt(split[0]);
                String type = split[1];
                workstationRecipe.addOptionalIngredient(type, count);
            }
        }
        if (recipe.recipeTools != null) {
            for (String recipeTool : recipe.recipeTools) {
                String[] split = recipeTool.split("\\*", 2);
                int count = Integer.parseInt(split[0]);
                String type = split[1];
                workstationRecipe.addRequiredTool(type, count);
            }
        }
        if (recipe.recipeFluids != null) {
            for (String recipeFluid : recipe.recipeFluids) {
                String[] split = recipeFluid.split("\\*", 2);
                float volume = Float.parseFloat(split[0]);
                String type = split[1];
                workstationRecipe.addFluid(type, volume);
            }
        }

        if (recipe.blockResult != null) {
            final BlockManager blockManager = CoreRegistry.get(BlockManager.class);
            String[] split = recipe.blockResult.split("\\*", 2);
            int count = Integer.parseInt(split[0]);
            String block = split[1];
            workstationRecipe.setResultFactory(new BlockRecipeResultFactory(blockManager.getBlockFamily(block).getArchetypeBlock(), count));
        }
        if (recipe.itemResult != null) {
            final PrefabManager prefabManager = CoreRegistry.get(PrefabManager.class);
            String[] split = recipe.itemResult.split("\\*", 2);
            int count = Integer.parseInt(split[0]);
            String item = split[1];
            workstationRecipe.setResultFactory(new ItemRecipeResultFactory(prefabManager.getPrefab(item), count));
        }

        if (recipe.processingDuration != 0) {
            workstationRecipe.setProcessingDuration(recipe.processingDuration);
        }
        if (recipe.requiredTemperature > 0) {
            workstationRecipe.setRequiredHeat(recipe.requiredTemperature);
        }

        return new CraftingWorkstationProcess(process.processType, recipe.recipeId, workstationRecipe, prefab,
                entityManager);
    }
}
