// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.workstationCrafting.component;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.network.Replicate;

// Used to distinguish that a workstation is in fact portable and should not be treated as a block.
@Replicate
public class PortableWorkstationComponent implements Component {
}
