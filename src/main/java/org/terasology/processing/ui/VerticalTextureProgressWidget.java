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

import org.terasology.math.TeraMath;
import org.terasology.math.geom.Rect2i;
import org.terasology.math.geom.Vector2i;
import org.terasology.rendering.assets.texture.TextureRegion;
import org.terasology.rendering.nui.*;
import org.terasology.rendering.nui.databinding.Binding;
import org.terasology.rendering.nui.databinding.DefaultBinding;

/**
 * A UI widget that represents a vertical progress bar.
 */
public class VerticalTextureProgressWidget extends CoreWidget {
    @LayoutConfig
    private Binding<TextureRegion> image = new DefaultBinding<>();
    private int maxY;
    private int minY;
    private Binding<Float> value = new DefaultBinding<>();
    private Binding<Float> mark = new DefaultBinding<>();

    public VerticalTextureProgressWidget() {
        int test = 0;
    }

    public VerticalTextureProgressWidget(String id) {
        super(id);
    }

    public VerticalTextureProgressWidget(TextureRegion image) {
        this.image.set(image);
    }

    public VerticalTextureProgressWidget(String id, TextureRegion image) {
        super(id);
        this.image.set(image);
    }

    /**
     * Defines how the widget is drawn on the canvas.
     *
     * @param canvas The canvas on which the widget resides
     */
    @Override
    public void onDraw(Canvas canvas) {
        TextureRegion texture = getImage();
        if (texture != null) {
            float result = (float) TeraMath.clamp(getValue());

            Vector2i size = canvas.size();
            if (minY < maxY) {
                float yPerc = 1f * (minY + result * (maxY - minY)) / texture.getHeight();
                canvas.drawTextureRaw(texture, Rect2i.createFromMinAndSize(0, 0, size.x, Math.round(yPerc * size.y)), ScaleMode.STRETCH,
                        0f, 0f, 1f, yPerc);
            } else {
                float yPerc = 1f * (minY - result * (minY - maxY)) / texture.getHeight();
                canvas.drawTextureRaw(texture, Rect2i.createFromMinAndSize(0, Math.round(yPerc * size.y), size.x, Math.round((1 - yPerc) * size.y)), ScaleMode.STRETCH,
                        0, yPerc, 1, (1 - yPerc));
            }

            Float markValue = getMark();
            if (markValue != null) {
                float yPerc;
                if (minY < maxY) {
                    yPerc = 1f * (minY + markValue * (maxY - minY)) / texture.getHeight();
                } else {
                    yPerc = 1f * (minY - markValue * (minY - maxY)) / texture.getHeight();
                }
                int y = Math.round(yPerc * size.y);
                canvas.drawLine(0, y, size.x, y, Color.BLACK);
            }
        }
    }

    /**
     * Get the preferred content size of the widget.
     *
     * @param canvas   The canvas on which the widget resides
     * @param sizeHint A size hint indicating a recommended size
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
     * Get the texture of the widget.
     *
     * @return The texture of the widget
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
     * Get the value currently displayed by the progress bar.
     *
     * @return The value currently displayed on the progress bar
     */
    public float getValue() {
        return value.get();
    }

    /**
     * Set the value displayed by the progress bar.
     *
     * @param value The new value displayed by the progress bar
     */
    public void setValue(float value) {
        this.value.set(value);
    }

    /**
     * Set the binding of the value displayed by the progress bar.
     *
     * @param binding The new binding of the value displayed by the progress bar.
     */
    public void bindValue(Binding<Float> binding) {
        this.value = binding;
    }

    /**
     * Get the height currently displayed on the vertical progress bar.
     *
     * @return The height currently displayed on the vertical progress bar
     */
    public Float getMark() {
        return mark.get();
    }

    /**
     * Set the height displayed on the vertical progress bar.
     *
     * @param markValue The new height to be displayed on the vertical progress bar
     */
    public void setMark(Float markValue) {
        mark.set(markValue);
    }

    /**
     * Set the binding of the height displayed on the vertical progress bar.
     *
     * @param binding The new binding of the height to be displayed on the vertical progress bar
     */
    public void bindMark(Binding<Float> binding) {
        this.mark = binding;
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
     * Set the minimum Y coordinate of the widget on the canvas.
     *
     * @param minY The new minimum Y coordinate of the widget on the canvas.
     */
    public void setMinY(int minY) {
        this.minY = minY;
    }
}
