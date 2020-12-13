// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.workstationCrafting.system.recipe.workstation;

import org.joml.primitives.Rectanglei;
import org.terasology.workstationCrafting.system.recipe.render.CraftIngredientRenderer;
import org.joml.Vector2i;
import org.terasology.nui.asset.font.Font;
import org.terasology.nui.Canvas;
import org.terasology.nui.TextLineBuilder;

import java.util.List;

public class HeatIngredientRenderer implements CraftIngredientRenderer {
    private int requiredTemperature;

    public HeatIngredientRenderer(int requiredTemperature) {
        this.requiredTemperature = requiredTemperature;
    }

    @Override
    public Vector2i getPreferredSize(Canvas canvas, int multiplier) {
        Font font = canvas.getCurrentStyle().getFont();

        List<String> lines = TextLineBuilder.getLines(font, getText(), canvas.size().x);
        Vector2i result = font.getSize(lines);
        return new Vector2i(result.x, result.y + 3);
    }

    @Override
    public void render(Canvas canvas, Rectanglei region, int multiplier) {
        canvas.drawText(getText(), region);
    }

    private String getText() {
        return requiredTemperature + " C";
    }
}
