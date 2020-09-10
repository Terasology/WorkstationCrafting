// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.heat.component;

import com.google.common.collect.Lists;
import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.math.Side;
import org.terasology.engine.network.Replicate;
import org.terasology.engine.world.block.ForceBlockActive;
import org.terasology.nui.reflection.MappedContainer;

import java.util.List;
import java.util.Set;


/**
 * This component indicates that an entity can consume heat.
 */
@ForceBlockActive
public class HeatConsumerComponent implements Component {
    /**
     * The directions of heat flow.
     */
    @Replicate
    public Set<Side> heatDirections;

    /**
     * The efficiency with which heat is consumed. Used to calculate the fraction of the supplied heat that is actually
     * used.
     */
    @Replicate
    public float heatConsumptionEfficiency;

    /**
     * List of all residual heat effects on the entity. Residual heat is the heat left over after active heating.
     */
    @Replicate
    public List<ResidualHeat> residualHeat = Lists.newArrayList();

    /**
     * A class that represents a residual heat effect.
     */
    @MappedContainer
    public static class ResidualHeat {
        /**
         * How long the residual heat stays.
         */
        @Replicate
        public long time;

        /**
         * How intense the residual heat is.
         */
        @Replicate
        public float baseHeat;
    }
}
