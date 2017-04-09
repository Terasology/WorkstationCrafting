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

import org.terasology.entitySystem.Component;

/**
 * Indicates that an entity can be 'processed' using heat to be turned into another entity.
 */
public class HeatProcessedComponent implements Component {
    /** The heat required to process the entity. */
    public float heatRequired;

    /** The amount of time the entity has to be heated for to become completely processed. */
    public long processingTime;

    /** The URI of the resultant block. */
    public String blockResult;

    /** The URI of the resultant item. */
    public String itemResult;
}
