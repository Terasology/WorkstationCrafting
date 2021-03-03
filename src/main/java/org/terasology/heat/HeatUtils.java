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
package org.terasology.heat;

import org.joml.Vector3i;
import org.joml.Vector3ic;
import org.terasology.engine.Time;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.heat.component.HeatConsumerComponent;
import org.terasology.heat.component.HeatProducerComponent;
import org.terasology.math.Side;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.BlockEntityRegistry;
import org.terasology.world.block.BlockComponent;
import org.terasology.world.block.BlockRegion;
import org.terasology.world.block.regions.BlockRegionComponent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains utility functions that define how heat flow works.
 */
public final class HeatUtils {
    public static final float HEAT_MAGIC_VALUE = 2000f;

    private HeatUtils() {

    }

    /**
     * Calculates the heat of a heat producer.
     *
     * @param producer The heat producer component of the producer
     * @return         The heat produced by the producer
     */
    public static float calculateHeatForProducer(HeatProducerComponent producer) {
        long gameTime = CoreRegistry.get(Time.class).getGameTimeInMs();

        return Math.min(producer.maximumTemperature, calculateHeatForProducerAtTime(producer, gameTime));
    }

    /**
     * Calculates the final heat of a system using the heat equation.
     *
     * @param startingHeat           The initial heat of the system
     * @param appliedHeat            The heat applied to the system
     * @param heatTransferEfficiency The heat transfer efficiency of the process
     * @param duration               The duration for which heat was applied
     * @return                       The final heat of the system
     */
    public static float solveHeatEquation(float startingHeat, float appliedHeat, float heatTransferEfficiency, long duration) {
        return startingHeat + (appliedHeat - startingHeat) * (1 - (float) Math.pow(Math.E, -duration * heatTransferEfficiency / HEAT_MAGIC_VALUE));
    }

    /**
     * Calculate the heat of a heat producer at a given time.
     *
     * @param producer The heat producer component of the producer
     * @param time     The time at which the heat of the producer is to be calculated
     * @return         The heat of the producer at the given time
     */
    private static float calculateHeatForProducerAtTime(HeatProducerComponent producer, long time) {
        float heat = 20;
        long lastCalculated = 0;
        for (HeatProducerComponent.FuelSourceConsume fuelSourceConsume : producer.fuelConsumed) {
            if (fuelSourceConsume.startTime < time) {
                if (lastCalculated < fuelSourceConsume.startTime) {
                    heat = solveHeatEquation(heat, 20, producer.temperatureLossRate, fuelSourceConsume.startTime - lastCalculated);
                    lastCalculated = fuelSourceConsume.startTime;
                }
                long heatEndTime = Math.min(fuelSourceConsume.startTime + fuelSourceConsume.burnLength, time);
                heat = Math.min(producer.maximumTemperature,
                        solveHeatEquation(heat, fuelSourceConsume.heatProvided, producer.temperatureAbsorptionRate, heatEndTime - lastCalculated));
                lastCalculated = heatEndTime;
            } else {
                break;
            }
        }

        if (lastCalculated < time) {
            heat = solveHeatEquation(heat, 20, producer.temperatureLossRate, time - lastCalculated);
        }

        return heat;
    }

    /**
     * Calculate the heat of an entity.
     *
     * @param entity              The entity whose heat is to be calculated
     * @param blockEntityRegistry The block entity registry of the world
     * @return                    The heat of the given entity
     */
    public static float calculateHeatForEntity(EntityRef entity, BlockEntityRegistry blockEntityRegistry) {
        HeatProducerComponent producer = entity.getComponent(HeatProducerComponent.class);
        HeatConsumerComponent consumer = entity.getComponent(HeatConsumerComponent.class);
        if (producer != null) {
            return calculateHeatForProducer(producer);
        } else if (consumer != null) {
            return calculateHeatForConsumer(entity, blockEntityRegistry, consumer);
        } else {
            return 20;
        }
    }

    /**
     * Calculates the heat of a heat consumer.
     *
     * @param entity              The entity whose heat is to be calculated
     * @param blockEntityRegistry The block entity registry of the world
     * @param heatConsumer        The heat consumer component of the consumer
     * @return                    The heat of the heat consumer
     */
    private static float calculateHeatForConsumer(EntityRef entity, BlockEntityRegistry blockEntityRegistry, HeatConsumerComponent heatConsumer) {
        float result = 20;

        for (Map.Entry<Vector3i, Side> heaterBlock : getPotentialHeatSourceBlocksForConsumer(entity).entrySet()) {
            EntityRef potentialHeatProducer = blockEntityRegistry.getEntityAt(heaterBlock.getKey());
            HeatProducerComponent producer = potentialHeatProducer.getComponent(HeatProducerComponent.class);

            if (producer != null && producer.heatDirections.contains(heaterBlock.getValue().reverse())) {
                result += calculateHeatForProducer(producer);
            }
        }

        long gameTime = CoreRegistry.get(Time.class).getGameTimeInMs();

        for (HeatConsumerComponent.ResidualHeat residualHeat : heatConsumer.residualHeat) {
            double heat = calculateResidualHeatValue(gameTime, residualHeat);
            result += heat;
        }

        return result * heatConsumer.heatConsumptionEfficiency;
    }

    /**
     * Calculate the amount of residual heat in an entity at a given time.
     *
     * @param gameTime     The time at which the residual heat is to be calculated
     * @param residualHeat The residual heat object whose value is to be calculated
     * @return             The amount of residual heat contained by the entity at the given time
     */
    public static double calculateResidualHeatValue(long gameTime, HeatConsumerComponent.ResidualHeat residualHeat) {
        float timeSinceHeatWasEstablished = (gameTime - residualHeat.time) / 1000f;
        return residualHeat.baseHeat * Math.pow(Math.E, -1 * timeSinceHeatWasEstablished);
    }

    public static BlockRegion getEntityBlocks(EntityRef entityRef) {
        BlockComponent blockComponent = entityRef.getComponent(BlockComponent.class);
        if (blockComponent != null) {
            Vector3i blockPosition = blockComponent.getPosition(new Vector3i());
            return new BlockRegion(blockPosition);
        }
        BlockRegionComponent blockRegionComponent = entityRef.getComponent(BlockRegionComponent.class);
        return blockRegionComponent.region;
    }

    /**
     * Gets a map containing the location of potential heat producers near a consumer and the direction from which they
     * can heat the consumer.
     *
     * @param consumer The heat consumer
     * @return         A map containing the location and direction of potential heat sources near the consumer
     */
    public static Map<Vector3i, Side> getPotentialHeatSourceBlocksForConsumer(EntityRef consumer) {
        HeatConsumerComponent consumerComp = consumer.getComponent(HeatConsumerComponent.class);
        if (consumerComp == null) {
            return Collections.emptyMap();
        }

        BlockRegion entityBlocks = getEntityBlocks(consumer);

        Map<Vector3i, Side> result = new HashMap<>();

        for (Vector3ic entityBlock : entityBlocks) {
            for (Side heatDirection : consumerComp.heatDirections) {
                Vector3i heatedBlock = new Vector3i(entityBlock);
                heatedBlock.add(heatDirection.direction());
                if (!entityBlocks.contains(heatedBlock)) {
                    result.put(heatedBlock, heatDirection);
                }
            }
        }

        return result;
    }

    /**
     * Gets the position and direction of blocks that a heat producer could potentially heat.
     *
     * @param producer The heat producer
     * @return         A map containing the location and direction of blocks that could potentially be heated by the producer
     */
    public static Map<Vector3i, Side> getPotentialHeatedBlocksForProducer(EntityRef producer) {
        HeatProducerComponent producerComp = producer.getComponent(HeatProducerComponent.class);
        if (producerComp == null) {
            return Collections.emptyMap();
        }

        BlockRegion entityBlocks = getEntityBlocks(producer);

        Map<Vector3i, Side> result = new HashMap<>();

        for (Vector3ic entityBlock : entityBlocks) {
            for (Side heatDirection : producerComp.heatDirections) {
                Vector3i heatedBlock = new Vector3i(entityBlock);
                heatedBlock.add(heatDirection.direction());
                if (!entityBlocks.contains(heatedBlock)) {
                    result.put(heatedBlock, heatDirection);
                }
            }
        }

        return result;
    }
}
