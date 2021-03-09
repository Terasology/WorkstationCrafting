// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.workstationCrafting.ui;

import org.joml.Vector2i;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.rendering.nui.layers.ingame.inventory.ItemIcon;
import org.terasology.input.Keyboard;
import org.terasology.joml.geom.Rectanglei;
import org.terasology.nui.Canvas;
import org.terasology.nui.CoreWidget;
import org.terasology.nui.UIWidget;
import org.terasology.nui.databinding.Binding;
import org.terasology.nui.events.NUIKeyEvent;
import org.terasology.nui.widgets.ActivateEventListener;
import org.terasology.nui.widgets.UIButton;
import org.terasology.workstationCrafting.system.recipe.render.CraftIngredientRenderer;
import org.terasology.workstationCrafting.system.recipe.render.CraftProcessDisplay;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class CraftRecipeWidget extends CoreWidget {
    private EntityRef entity;
    private CraftProcessDisplay processDisplay;
    private final CreationCallback callback;

    private ItemIcon result;

    private UIButton button;

    private int leftIndent;

    private int multiplier = 1;

    public CraftRecipeWidget(int leftIndent, final EntityRef entity,
                             final CraftProcessDisplay processDisplay, CreationCallback callback) {
        this.leftIndent = leftIndent;
        this.entity = entity;
        this.processDisplay = processDisplay;
        this.callback = callback;

        result = new ItemIcon();
        processDisplay.setupResultDisplay(result);
        result.bindQuantity(
                new Binding<Integer>() {
                    @Override
                    public Integer get() {
                        return processDisplay.getResultQuantity() * multiplier;
                    }

                    @Override
                    public void set(Integer value) {
                    }
                }
        );


        button = new UIButton();
        button.setText("Craft");
        button.bindVisible(
                new Binding<Boolean>() {
                    @Override
                    public Boolean get() {
                        return processDisplay.isValidForCrafting(entity, multiplier);
                    }

                    @Override
                    public void set(Boolean value) {
                    }
                }
        );
        button.subscribe(
                new ActivateEventListener() {
                    @Override
                    public void onActivated(UIWidget widget) {
                        produce();
                    }
                }
        );
    }

    private void produce() {
        callback.create(multiplier);
    }

    @Override
    public void onDraw(Canvas canvas) {
        int x = leftIndent;
        Vector2i size = canvas.size();

        for (CraftIngredientRenderer craftIngredientRenderer : processDisplay.getIngredientRenderers(entity)) {
            Vector2i preferredSize = craftIngredientRenderer.getPreferredSize(canvas, multiplier);
            craftIngredientRenderer.render(canvas, new Rectanglei(x, 0).setSize(preferredSize.x, size.y), multiplier);
            x += preferredSize.x;
        }

        Vector2i resultSize = canvas.calculatePreferredSize(result);
        Vector2i buttonSize = canvas.calculatePreferredSize(button);

        canvas.drawWidget(button, new Rectanglei(size.x - resultSize.x - buttonSize.x - 5,
                (size.y - buttonSize.y) / 2).setSize(buttonSize.x, buttonSize.y));
        canvas.drawWidget(result, new Rectanglei(size.x - resultSize.x, 0).setSize(resultSize.x, resultSize.y));
    }


    @Override
    public boolean onKeyEvent(NUIKeyEvent event) {
        int maxMultiplier = processDisplay.getMaxMultiplier(entity);
        if (event.getKeyboard().isKeyDown(Keyboard.KeyId.LEFT_SHIFT)) {
            multiplier = Math.min(maxMultiplier, 5);
        } else if (event.getKeyboard().isKeyDown(Keyboard.KeyId.LEFT_CTRL)) {
            multiplier = maxMultiplier;
        } else {
            multiplier = 1;
        }
        return false;
    }

    @Override
    public void update(float delta) {
        result.update(delta);
        button.update(delta);
    }

    @Override
    public Vector2i getPreferredContentSize(Canvas canvas, Vector2i sizeHint) {
        int maxX = canvas.size().x;
        int maxY = 0;

        for (CraftIngredientRenderer craftIngredientRenderer : processDisplay.getIngredientRenderers(entity)) {
            maxY = Math.max(maxY, craftIngredientRenderer.getPreferredSize(canvas, multiplier).y);
        }

        maxY = Math.max(maxY, canvas.calculatePreferredSize(result).y);
        maxY = Math.max(maxY, canvas.calculatePreferredSize(button).y);

        return new Vector2i(maxX, maxY);
    }

    @Override
    public Vector2i getMaxContentSize(Canvas canvas) {
        return getPreferredContentSize(canvas, null);
    }
}
