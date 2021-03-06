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

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.network.Replicate;
import org.terasology.engine.world.block.items.AddToBlockBasedItem;

/**
 * Indicates that an entity can be used as fuel to generate heat.
 */
@AddToBlockBasedItem
public class HeatFuelComponent implements Component {
    /** The amount of heat provided by the fuel every update. */
    public float heatProvided;

    /** The amount of time taken to completely consume the fuel. */
    @Replicate
    public long consumeTime;
}
