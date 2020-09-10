// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.heat.component;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.math.Side;
import org.terasology.engine.network.Replicate;
import org.terasology.engine.world.block.ForceBlockActive;
import org.terasology.nui.reflection.MappedContainer;

import java.util.List;
import java.util.Set;

/**
 * Indicates that an entity can consume fuel to produce heat.
 */
@ForceBlockActive
public class HeatProducerComponent implements Component {
    /**
     * Details of all fuels consumed by the entity.
     */
    @Replicate
    public List<FuelSourceConsume> fuelConsumed = Lists.newArrayList();

    /**
     * The rate at which the entity gains temperature.
     */
    @Replicate
    public float temperatureAbsorptionRate;

    /**
     * The rate at which the entity loses temperature.
     */
    @Replicate
    public float temperatureLossRate;

    /**
     * The maximum temperature that the entity can have.
     */
    @Replicate
    public float maximumTemperature;

    /**
     * The directions of heat flow.
     */
    @Replicate
    public Set<Side> heatDirections = Sets.newHashSet();

    /**
     * A class that represents fuel consumption.
     */
    @MappedContainer
    public static class FuelSourceConsume {
        /**
         * The game time when the fuel consumption was started.
         */
        @Replicate
        public long startTime;

        /**
         * The heat provided by the fuel.
         */
        @Replicate
        public float heatProvided;

        /**
         * The total time for which the fuel was consumed.
         */
        @Replicate
        public long burnLength;
    }
}
