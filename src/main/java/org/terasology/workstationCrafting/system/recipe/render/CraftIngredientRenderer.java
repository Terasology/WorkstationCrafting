// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.workstationCrafting.system.recipe.render;

import org.terasology.joml.geom.Rectanglei;
import org.joml.Vector2i;
import org.terasology.nui.Canvas;

public interface CraftIngredientRenderer {
    Vector2i getPreferredSize(Canvas canvas, int multiplier);

    void render(Canvas canvas, Rectanglei region, int multiplier);
}
