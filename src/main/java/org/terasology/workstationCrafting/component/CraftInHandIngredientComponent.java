// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.workstationCrafting.component;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.network.Replicate;

public class CraftInHandIngredientComponent implements Component {
    @Replicate
    public String componentType;
}
