// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.heat.processPart;

import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.EmptyComponent;

/**
 * Indicates that an entity is a fueling part for a workstation.
 */
@Replicate
public class HeatFuelingComponent extends EmptyComponent<HeatFuelingComponent> {
}
