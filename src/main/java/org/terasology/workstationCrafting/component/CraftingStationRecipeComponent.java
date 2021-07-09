// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.workstationCrafting.component;

import com.google.common.collect.Lists;
import org.terasology.gestalt.entitysystem.component.Component;

import java.util.List;

public class CraftingStationRecipeComponent implements Component<CraftingStationRecipeComponent> {
    public String recipeId;
    public List<String> recipeComponents;
    public List<String> optionalRecipeComponents;
    public List<String> recipeTools;
    public List<String> recipeFluids;

    public float requiredTemperature;
    public long processingDuration;

    public String itemResult;
    public String blockResult;

    @Override
    public void copy(CraftingStationRecipeComponent other) {
        this.recipeId = other.recipeId;
        this.recipeComponents = Lists.newArrayList(other.recipeComponents);
        this.optionalRecipeComponents = Lists.newArrayList(other.optionalRecipeComponents);
        this.recipeTools = Lists.newArrayList(other.recipeTools);
        this.recipeFluids = Lists.newArrayList(other.recipeFluids);
        this.requiredTemperature = other.requiredTemperature;
        this.processingDuration = other.processingDuration;
        this.itemResult = other.itemResult;
        this.blockResult = other.blockResult;
    }
}
