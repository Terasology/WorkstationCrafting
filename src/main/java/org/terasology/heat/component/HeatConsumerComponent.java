// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.heat.component;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.terasology.engine.math.Side;
import org.terasology.engine.network.Replicate;
import org.terasology.engine.world.block.ForceBlockActive;
import org.terasology.gestalt.entitysystem.component.Component;
import org.terasology.reflection.MappedContainer;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * This component indicates that an entity can consume heat.
 */
@ForceBlockActive
public class HeatConsumerComponent implements Component<HeatConsumerComponent> {
    /** The directions of heat flow. */
    @Replicate
    public Set<Side> heatDirections;

    /** The efficiency with which heat is consumed. Used to calculate the fraction of the supplied heat that is actually used. */
    @Replicate
    public float heatConsumptionEfficiency;

    /** List of all residual heat effects on the entity. Residual heat is the heat left over after active heating. */
    @Replicate
    public List<ResidualHeat> residualHeat = Lists.newArrayList();

    @Override
    public void copy(HeatConsumerComponent other) {
        this.heatDirections = Sets.newHashSet(heatDirections);
        this.heatConsumptionEfficiency = other.heatConsumptionEfficiency;
        this.residualHeat = other.residualHeat.stream()
                .map(ResidualHeat::copy)
                .collect(Collectors.toList());
    }

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

        ResidualHeat copy() {
            ResidualHeat newHeat = new ResidualHeat();
            newHeat.time = this.time;
            newHeat.baseHeat = this.baseHeat;
            return newHeat;
        }
    }
}
