// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.processing.component;

import org.terasology.engine.network.Replicate;
import org.terasology.engine.world.block.items.AddToBlockBasedItem;
import org.terasology.gestalt.entitysystem.component.Component;
import org.terasology.module.inventory.components.ItemDifferentiating;

import java.util.Objects;

/**
 * A component generally attached to tree blocks which indicates the tree type.
 */
@AddToBlockBasedItem
public class TreeTypeComponent implements Component<TreeTypeComponent>, ItemDifferentiating {
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

    @Override
    public void copyFrom(TreeTypeComponent other) {
        this.treeType = other.treeType;
    }
}
