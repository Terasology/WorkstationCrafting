// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.workstationCrafting.component;

import com.google.common.collect.Lists;
import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

import java.util.List;

public class CraftingStationToolComponent implements Component<CraftingStationToolComponent> {
    @Replicate
    public List<String> type;

    @Override
    public void copy(CraftingStationToolComponent other) {
        this.type = Lists.newArrayList(other.type);
    }
}
