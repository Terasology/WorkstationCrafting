// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.workstationCrafting.component;

import com.google.common.collect.Lists;
import org.terasology.gestalt.entitysystem.component.Component;

import java.util.List;

public class CraftingStationUpgradeRecipeComponent implements Component<CraftingStationUpgradeRecipeComponent> {
    public static final String PROCESS_TYPE = "Crafting:UpgradeCraftingWorkstation";

    public String stationType;
    public String targetStationType;
    public String targetStationPrefab;
    public List<String> recipeComponents;
    public String resultBlockUri;

    @Override
    public void copyFrom(CraftingStationUpgradeRecipeComponent other) {
        this.stationType = other.stationType;
        this.targetStationType = other.targetStationType;
        this.targetStationPrefab = other.targetStationPrefab;
        this.recipeComponents = Lists.newArrayList(other.recipeComponents);
        this.resultBlockUri = other.resultBlockUri;
    }
}
