/*
 * Copyright 2016 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.processing.ui;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.fluid.component.FluidComponent;
import org.terasology.fluid.component.FluidInventoryComponent;
import org.terasology.fluid.system.FluidRegistry;
import org.terasology.fluid.system.FluidRenderer;
import org.terasology.math.geom.Rect2i;
import org.terasology.math.geom.Vector2i;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.assets.texture.TextureRegion;
import org.terasology.rendering.nui.*;
import org.terasology.rendering.nui.databinding.Binding;
import org.terasology.rendering.nui.databinding.DefaultBinding;

/**
 * A UI widget which represents a fluid holder.
 */
public class FluidHolderWidget extends CoreWidget {
    @LayoutConfig
    private Binding<TextureRegion> image = new DefaultBinding<>();
    private InteractionListener listener = new BaseInteractionListener();

    private int minX;
    private int maxX;

    private int minY;
    private int maxY;

    private EntityRef entity;
    private int slotNo;

    public FluidHolderWidget() {
        int x = 0;
    }

    public FluidHolderWidget(String id) {
        super(id);
    }

    public FluidHolderWidget(TextureRegion image) {
        this.image.set(image);
    }

    public FluidHolderWidget(String id, TextureRegion image) {
        super(id);
        this.image.set(image);
    }

    /**
     * Get the preferred content size of the widget.
     *
     * @param canvas   The canvas on which the widget resides
     * @param sizeHint A size hint which indicates a recommended size (passed by the engine)
     * @return         The preferred content size of the widget
     */
    @Override
    public Vector2i getPreferredContentSize(Canvas canvas, Vector2i sizeHint) {
        if (image.get() != null) {
            return image.get().size();
        }
        return Vector2i.zero();
    }

    /**
     * Defines how the widget is drawn on the canvas.
     *
     * @param canvas The canvas on which the widget resides
     */
    @Override
    public void onDraw(Canvas canvas) {
        TextureRegion texture = getImage();

        if (texture == null)
        {
            texture = canvas.getCurrentStyle().getBackground();
        }

        if (texture != null) {
            FluidInventoryComponent fluidInventory = entity.getComponent(FluidInventoryComponent.class);
            FluidComponent fluid = fluidInventory.fluidSlots.get(slotNo).getComponent(FluidComponent.class);
            float maxVolume = fluidInventory.maximumVolumes.get(slotNo);

            if (fluid != null) {
                float result = fluid.volume / maxVolume;

                FluidRegistry fluidRegistry = CoreRegistry.get(FluidRegistry.class);
                FluidRenderer fluidRenderer = fluidRegistry.getFluidRenderer(fluid.fluidType);

                Vector2i size = canvas.size();
                if (minY < maxY) {
                    float yPerc = 1f * (minY + result * (maxY - minY)) / texture.getHeight();
                    fluidRenderer.renderFluid(canvas, Rect2i.createFromMinAndSize(minX, minY, maxX, Math.round(yPerc * size.y) - minY));
                } else {
                    float yPerc = 1f * (minY - result * (minY - maxY)) / texture.getHeight();
                    int y = Math.round(yPerc * size.y);
                    fluidRenderer.renderFluid(canvas, Rect2i.createFromMinAndSize(minX, y, maxX, minY - y + 1));
                }
            }

            canvas.drawTexture(texture, canvas.getRegion());
        }

        canvas.addInteractionRegion(listener);
    }

    /**
     * Set the entity reference of this object.
     *
     * @param entity The new entity reference of this object
     */
    public void setEntity(EntityRef entity) {
        this.entity = entity;
    }

    /**
     * Set the inventory slot number of this object.
     *
     * @param slotNo The new slot number of this object
     */
    public void setSlotNo(int slotNo) {
        this.slotNo = slotNo;
    }

    /**
     * Get the texture of the widget.
     *
     * @return The texture of this widget
     */
    public TextureRegion getImage() {
        return image.get();
    }

    /**
     * Set the texture of the widget.
     *
     * @param image The new texture of the widget
     */
    public void setImage(TextureRegion image) {
        this.image.set(image);
    }

    /**
     * Set the image binding of the widget.
     *
     * @param binding The new image binding of the widget
     */
    public void bindTexture(Binding<TextureRegion> binding) {
        this.image = binding;
    }

    /**
     * Set the minimum Y coordinate of the widget on the canvas.
     *
     * @param minY The new minimum Y coordinate of the widget on the canvas.
     */
    public void setMinY(int minY) {
        this.minY = minY;
    }

    /**
     * Set the maximum Y coordinate of the widget on the canvas.
     *
     * @param maxY The new maximum Y coordinate of the widget on the canvas.
     */
    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    /**
     * Set the minimum X coordinate of the widget on the canvas.
     *
     * @param minX The new mini X coordinate of the widget on the canvas.
     */
    public void setMinX(int minX) {
        this.minX = minX;
    }

    /**
     * Set the maximum X coordinate of the widget on the canvas.
     *
     * @param maxX The new maximum X coordinate of the widget on the canvas.
     */
    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }
}
