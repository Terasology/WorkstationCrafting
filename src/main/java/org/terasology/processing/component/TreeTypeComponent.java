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
package org.terasology.processing.component;

import org.terasology.entitySystem.Component;
import org.terasology.logic.inventory.ItemDifferentiating;
import org.terasology.network.Replicate;
import org.terasology.world.block.items.AddToBlockBasedItem;

import java.util.Objects;

/**
 * A component generally attached to tree blocks which indicates the tree type.
 */
@AddToBlockBasedItem
public class TreeTypeComponent implements Component, ItemDifferentiating {
    /** The type of the tree. */
    @Replicate
    public String treeType;

    /**
     * Check whether a given object is of the same data-type as this component and whether it contains the same tree type.
     *
     * @param o The other object
     * @return  Whether the given object is of the same data-type and contains the same tree type as this object
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TreeTypeComponent that = (TreeTypeComponent) o;

        if (treeType != null ? !treeType.equals(that.treeType) : that.treeType != null) {
            return false;
        }

        return true;
    }

    /**
     * Returns the hash code of this object.
     *
     * @return The hash code of this object
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(treeType);
    }
}
