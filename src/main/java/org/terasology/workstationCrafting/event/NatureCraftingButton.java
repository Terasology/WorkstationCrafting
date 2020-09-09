// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.workstationCrafting.event;

import org.lwjgl.input.Keyboard;
import org.terasology.engine.input.BindButtonEvent;
import org.terasology.engine.input.DefaultBinding;
import org.terasology.engine.input.RegisterBindButton;
import org.terasology.nui.input.InputType;

@RegisterBindButton(id = "natureCrafting", description = "Nature (Hand) Crafting", category = "interaction")
@DefaultBinding(type = InputType.KEY, id = Keyboard.KEY_N)
public class NatureCraftingButton extends BindButtonEvent {
}
