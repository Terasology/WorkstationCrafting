// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.workstationCrafting.component;

import com.google.common.collect.Lists;
import org.terasology.gestalt.entitysystem.component.Component;

import java.util.List;

public class CraftInHandRecipeComponent implements Component<CraftInHandRecipeComponent> {
    public String recipeId;
    public List<String> recipeComponents;
    public List<String> recipeTools;
    public List<String> recipeActivators;

    public String itemResult;
    public String blockResult;

    @Override
    public void copy(CraftInHandRecipeComponent other) {
        this.recipeId = other.recipeId;
        this.recipeComponents = Lists.newArrayList(other.recipeComponents);
        this.recipeTools = Lists.newArrayList(this.recipeTools);
        this.recipeActivators = Lists.newArrayList(other.recipeActivators);
        this.itemResult = other.itemResult;
        this.blockResult = other.blockResult;
    }
}
