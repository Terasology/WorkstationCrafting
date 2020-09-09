// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.workstationCrafting.component;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.network.Replicate;

import java.util.List;

public class CraftingProcessComponent implements Component {
    @Replicate
    public List<String> parameters;
    @Replicate
    public int count;
}
