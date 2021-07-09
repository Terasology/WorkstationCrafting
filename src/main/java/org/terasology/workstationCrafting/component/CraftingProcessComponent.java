// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.workstationCrafting.component;

import com.google.common.collect.Lists;
import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

import java.util.List;

public class CraftingProcessComponent implements Component<CraftingProcessComponent> {
    @Replicate
    public List<String> parameters;
    @Replicate
    public int count;

    @Override
    public void copy(CraftingProcessComponent other) {
        this.parameters = Lists.newArrayList(other.parameters);
        this.count = other.count;
    }
}
