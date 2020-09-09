// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.heat.component;

import org.terasology.engine.entitySystem.Component;

/**
 * Indicates that an entity can be 'processed' using heat to be turned into another entity.
 */
public class HeatProcessedComponent implements Component {
    /**
     * The heat required to process the entity.
     */
    public float heatRequired;

    /**
     * The amount of time the entity has to be heated for to become completely processed.
     */
    public long processingTime;

    /**
     * The URI of the resultant block.
     */
    public String blockResult;

    /**
     * The URI of the resultant item.
     */
    public String itemResult;
}
