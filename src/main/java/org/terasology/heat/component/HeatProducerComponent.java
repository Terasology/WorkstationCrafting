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
package org.terasology.heat.component;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.math.Side;
import org.terasology.engine.network.Replicate;
import org.terasology.engine.world.block.ForceBlockActive;
import org.terasology.reflection.MappedContainer;

import java.util.List;
import java.util.Set;

/**
 * Indicates that an entity can consume fuel to produce heat.
 */
@ForceBlockActive
public class HeatProducerComponent implements Component {
    /** Details of all fuels consumed by the entity. */
    @Replicate
    public List<FuelSourceConsume> fuelConsumed = Lists.newArrayList();

    /** The rate at which the entity gains temperature. */
    @Replicate
    public float temperatureAbsorptionRate;

    /** The rate at which the entity loses temperature. */
    @Replicate
    public float temperatureLossRate;

    /** The maximum temperature that the entity can have. */
    @Replicate
    public float maximumTemperature;

    /** The directions of heat flow. */
    @Replicate
    public Set<Side> heatDirections = Sets.newHashSet();

    /**
     * A class that represents fuel consumption.
     */
    @MappedContainer
    public static class FuelSourceConsume {
        /** The game time when the fuel consumption was started. */
        @Replicate
        public long startTime;

        /** The heat provided by the fuel. */
        @Replicate
        public float heatProvided;

        /** The total time for which the fuel was consumed. */
        @Replicate
        public long burnLength;
    }
}
