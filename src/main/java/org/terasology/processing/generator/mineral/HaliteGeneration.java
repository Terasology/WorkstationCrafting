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
package org.terasology.processing.generator.mineral;

import org.terasology.anotherWorld.decorator.ore.OreDefinition;
import org.terasology.anotherWorld.decorator.structure.PocketStructureDefinition;
import org.terasology.anotherWorld.decorator.structure.provider.UniformPocketBlockProvider;
import org.terasology.anotherWorld.util.PDist;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.BlockManager;
import org.terasology.world.generator.plugin.RegisterPlugin;

/**
 * Defines an ore ("Halite") and the properties of the ore pertaining to generation.
 */
@RegisterPlugin
public class HaliteGeneration extends PocketStructureDefinition implements OreDefinition {
    public HaliteGeneration() {
        super(new UniformPocketBlockProvider(CoreRegistry.get(BlockManager.class).getBlock("WoodAndStone:Halite")),
                new PDist(0.45f, 0.15f), new PDist(4f, 1f), new PDist(2f, 1f), new PDist(1800f, 300f), new PDist(0f, 0.35f),
                new PDist(1f, 0f), new PDist(0.7f, 0.1f), new PDist(0.2f, 0f), new PDist(0f, 0f));
    }

    @Override
    public String getOreId() {
        return "WoodAndStone:Halite";
    }
}
