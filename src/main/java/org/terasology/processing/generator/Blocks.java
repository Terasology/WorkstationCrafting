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
package org.terasology.processing.generator;

import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;

/**
 * Utility class that can be used to access blocks from the Core Registry's block manager.
 */
public final class Blocks {
    private Blocks() {
    }

    /**
     * Get a block with a given ID.
     *
     * @param blockId The ID of the block
     * @return        The block with the given ID
     */
    public static Block getBlock(String blockId) {
        return CoreRegistry.get(BlockManager.class).getBlock(blockId);
    }
}
