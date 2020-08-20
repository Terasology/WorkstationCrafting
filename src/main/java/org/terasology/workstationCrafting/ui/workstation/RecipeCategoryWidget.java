// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.workstationCrafting.ui.workstation;

import org.terasology.input.MouseInput;
import org.terasology.math.JomlUtil;
import org.joml.Vector2i;
import org.terasology.nui.BaseInteractionListener;
import org.terasology.nui.Canvas;
import org.terasology.nui.CoreWidget;
import org.terasology.nui.InteractionListener;
import org.terasology.nui.TextLineBuilder;
import org.terasology.nui.asset.font.Font;
import org.terasology.nui.events.NUIMouseClickEvent;

import java.util.List;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class RecipeCategoryWidget extends CoreWidget {
    private boolean opened;
    private int leftIndent;
    private String name;
    private int count;
    private CategoryToggleCallback callback;

    private InteractionListener interactionListener = new BaseInteractionListener() {
        @Override
        public boolean onMouseClick(NUIMouseClickEvent event) {
            if (event.getMouseButton() == MouseInput.MOUSE_LEFT) {
                callback.categoryToggled();
                return true;
            }
            return false;
        }
    };

    public RecipeCategoryWidget(boolean opened, int leftIndent, String name, int count, CategoryToggleCallback callback) {
        this.opened = opened;
        this.leftIndent = leftIndent;
        this.name = name;
        this.count = count;
        this.callback = callback;
    }

    @Override
    public void onDraw(Canvas canvas) {
        Vector2i size = canvas.size();
        canvas.drawText(getText(), JomlUtil.rectangleiFromMinAndSize(leftIndent, 0, size.x, size.y));
        canvas.addInteractionRegion(interactionListener, "Toggle " + name);
    }

    private String getText() {
        return (opened ? "-" : "+") + " " + String.format("%s (%d)", name, count);
    }

    @Override
    public Vector2i getPreferredContentSize(Canvas canvas, Vector2i sizeHint) {
        Font font = canvas.getCurrentStyle().getFont();

        List<String> lines = TextLineBuilder.getLines(font, getText(), canvas.size().x);
        Vector2i textSize = font.getSize(lines);
        return new Vector2i(canvas.size().x, textSize.y);
    }

    @Override
    public Vector2i getMaxContentSize(Canvas canvas) {
        return getPreferredContentSize(canvas, null);
    }
}
