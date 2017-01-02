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
package org.terasology.heat.ui;

import org.terasology.math.geom.Rect2i;
import org.terasology.math.geom.Vector2i;
import org.terasology.rendering.assets.texture.TextureRegion;
import org.terasology.rendering.nui.*;
import org.terasology.rendering.nui.databinding.Binding;
import org.terasology.rendering.nui.databinding.DefaultBinding;

/**
 * A UI widget which acts as a thermometer.
 */
public class ThermometerWidget extends CoreWidget {
    private Binding<Float> temperature = new DefaultBinding<>();
    private Binding<Float> markedTemperature = new DefaultBinding<>();
    private Binding<Float> maxTemperature = new DefaultBinding<>();
    private Binding<Float> minTemperature = new DefaultBinding<>();
    private InteractionListener listener = new BaseInteractionListener();

    private float minHeightPerc = 0.87f;
    private float maxHeightPerc = 0.03f;

    /**
     * Get the preferred content size of the widget.
     *
     * @param canvas   The canvas on which the widget resides
     * @param sizeHint A size hint passed to indicate a recommended size
     * @return         The preferred content size
     */
    @Override
    public Vector2i getPreferredContentSize(Canvas canvas, Vector2i sizeHint) {
        return sizeHint;
    }

    /**
     * Defines how the widget is drawn on the canvas.
     *
     * @param canvas The canvas on which the widget resides
     */
    @Override
    public void onDraw(Canvas canvas) {
        canvas.setPart("front");
        TextureRegion foreground = canvas.getCurrentStyle().getBackground();
        float min = getMinTemperature();
        float max = getMaxTemperature();

        float current = getTemperature();

        Vector2i size = canvas.size();

        float temperaturePerc = minHeightPerc - (current - min) / (max - min) * (minHeightPerc - maxHeightPerc);

        canvas.drawTextureRaw(foreground,
                Rect2i.createFromMinAndSize(0, Math.round(temperaturePerc * size.y), size.x,
                        Math.round((1 - temperaturePerc) * size.y)),
                ScaleMode.STRETCH, 0, temperaturePerc, 1, (1 - temperaturePerc));

        Float markValue = getMarkedTemperature();
        if (markValue != null) {
            float markPerc = minHeightPerc - (markValue - min) / (max - min) * (minHeightPerc - maxHeightPerc);
            int y = Math.round(markPerc * size.y);
            canvas.drawLine(0, y, size.x, y, Color.BLACK);
        }

        canvas.addInteractionRegion(listener);
    }

    /**
     * Set the value of the temperature of the thermometer.
     *
     * @param value The value of the temperature to set the thermometer to
     */
    public void setTemperature(float value) {
        temperature.set(value);
    }

    /**
     * Set the temperature binding of the widget.
     *
     * @param temperatureToBind The temperature binding to set as the widget's temperature binding
     */
    public void bindTemperature(Binding<Float> temperatureToBind) {
        temperature = temperatureToBind;
    }

    /**
     * Get the temperature of the thermometer.
     *
     * @return The temperature of the thermometer
     */
    public float getTemperature() {
        return temperature.get();
    }

    /**
     * Set the value of the marked temperature of the thermometer. This temperature is used to display the thermometer reading.
     *
     * @param value The value to set as the marked temperature of the thermometer
     */
    public void setMarkedTemperature(Float value) {
        temperature.set(value);
    }

    /**
     * Set the marked temperature binding of the thermometer.
     *
     * @param markedTemperatureToBind The new marked temperature binding
     */
    public void bindMarkedTemperature(Binding<Float> markedTemperatureToBind) {
        markedTemperature = markedTemperatureToBind;
    }

    /**
     * Get the marked temperature on the thermometer.
     *
     * @return The marked temperature on the thermometer
     */
    public Float getMarkedTemperature() {
        return markedTemperature.get();
    }

    /**
     * Set the value of the highest temperature that the thermometer can display.
     *
     * @param value The new value of the maximum temperature that the thermometer can display
     */
    public void setMaxTemperature(float value) {
        maxTemperature.set(value);
    }

    /**
     * Set the maximum temperature binding of the thermometer.
     *
     * @param maxTemperatureToBind The new maximum temperature binding
     */
    public void bindMaxTemperature(Binding<Float> maxTemperatureToBind) {
        maxTemperature = maxTemperatureToBind;
    }

    /**
     * Get the maximum temperature that the thermometer can display.
     *
     * @return The maximum temperature that the thermometer can display
     */
    public float getMaxTemperature() {
        return maxTemperature.get();
    }

    /**
     * Set the value of the lowest temperature that the thermometer can display.
     *
     * @param value The new value of the minimum temperature that the thermometer can display
     */
    public void setMinTemperature(float value) {
        minTemperature.set(value);
    }

    /**
     * Set the minimum temperature binding of the thermometer.
     *
     * @param minTemperatureToBind The new minimum temperature binding
     */
    public void bindMinTemperature(Binding<Float> minTemperatureToBind) {
        minTemperature = minTemperatureToBind;
    }

    /**
     * Get the minimum temperature that the thermometer can display.
     *
     * @return The minimum temperature that the thermometer can display
     */
    public float getMinTemperature() {
        return minTemperature.get();
    }
}
