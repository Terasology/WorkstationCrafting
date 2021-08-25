// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.workstationCrafting.component;

import org.terasology.engine.network.Replicate;
import org.terasology.engine.rendering.assets.texture.Texture;
import org.terasology.gestalt.entitysystem.component.Component;

public class CraftingStationComponent implements Component<CraftingStationComponent> {
    @Replicate
    public String type;
    @Replicate
    public Texture workstationUITexture;

    @Override
    public void copyFrom(CraftingStationComponent other) {
        this.type = other.type;
        this.workstationUITexture = other.workstationUITexture;
    }
}
