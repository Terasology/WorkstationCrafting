// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.processing.component;

import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.EmptyComponent;

/**
 * Indicates that an entity is capable of tilling soil.
 */
@Replicate
public class HoeComponent extends EmptyComponent<HoeComponent> {
}
