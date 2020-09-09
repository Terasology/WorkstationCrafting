// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.workstationCrafting.component;

import org.terasology.engine.entitySystem.Component;

import java.util.List;

public class CraftingStationUpgradeRecipeComponent implements Component {
    public static final String PROCESS_TYPE = "Crafting:UpgradeCraftingWorkstation";

    public String stationType;
    public String targetStationType;
    public String targetStationPrefab;
    public List<String> recipeComponents;
    public String resultBlockUri;
}
