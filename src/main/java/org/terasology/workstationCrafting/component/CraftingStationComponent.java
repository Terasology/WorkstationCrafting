// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.workstationCrafting.component;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.network.Replicate;
import org.terasology.engine.rendering.assets.texture.Texture;

public class CraftingStationComponent implements Component {
    @Replicate
    public String type;
    @Replicate
    public Texture workstationUITexture;
}
