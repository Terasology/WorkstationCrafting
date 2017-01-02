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
package org.terasology.processing.generator.plant.forest.grass;

import org.terasology.anotherWorld.AnotherWorldBiomes;
import org.terasology.anotherWorld.decorator.BlockCollectionPredicate;
import org.terasology.gf.PlantType;
import org.terasology.gf.generator.StaticBlockFloraSpawnDefinition;
import org.terasology.processing.generator.Blocks;
import org.terasology.world.block.BlockUri;
import org.terasology.world.generator.plugin.RegisterPlugin;

import java.util.Arrays;

/**
 * Defines the basic properties of mushroom forests pertaining to generation.
 */
@RegisterPlugin
public class ShroomForestSpawnDefinition extends StaticBlockFloraSpawnDefinition {
    public ShroomForestSpawnDefinition() {
        super(PlantType.GRASS, AnotherWorldBiomes.FOREST.getId(), 0.5f, 0.3f, "Core:Shroom",
                Arrays.asList(new BlockUri("Core:BigBrownShroom"), new BlockUri("Core:BrownShroom"), new BlockUri("Core:RedShroom")),
                new BlockCollectionPredicate(Blocks.getBlock("Core:Grass")), null);
    }
}
