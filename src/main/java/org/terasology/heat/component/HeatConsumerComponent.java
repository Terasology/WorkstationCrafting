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
import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.math.Side;
import org.terasology.engine.network.Replicate;
import org.terasology.engine.world.block.ForceBlockActive;
import org.terasology.reflection.MappedContainer;

import java.util.List;
import java.util.Set;


/**
 * This component indicates that an entity can consume heat.
 */
@ForceBlockActive
public class HeatConsumerComponent implements Component {
    /** The directions of heat flow. */
    @Replicate
    public Set<Side> heatDirections;

    /** The efficiency with which heat is consumed. Used to calculate the fraction of the supplied heat that is actually used. */
    @Replicate
    public float heatConsumptionEfficiency;

    /** List of all residual heat effects on the entity. Residual heat is the heat left over after active heating. */
    @Replicate
    public List<ResidualHeat> residualHeat = Lists.newArrayList();

    /**
     * A class that represents a residual heat effect.
     */
    @MappedContainer
    public static class ResidualHeat {
        /** How long the residual heat stays. */
        @Replicate
        public long time;

        /** How intense the residual heat is. */
        @Replicate
        public float baseHeat;
    }
}
