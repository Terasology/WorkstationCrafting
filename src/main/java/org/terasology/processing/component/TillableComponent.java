// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.processing.component;

import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.EmptyComponent;

/**
 * Indicates that a block is tillable.
 */
@Replicate
public class TillableComponent extends EmptyComponent<TillableComponent> {
}
