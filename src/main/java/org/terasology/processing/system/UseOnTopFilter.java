// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.processing.system;

import com.google.common.base.Predicate;
import org.terasology.engine.logic.common.ActivateEvent;
import org.terasology.engine.math.Side;

/**
 * A filter that returns true only when an event is passed where the concerned item is activated from the top.
 */
public class UseOnTopFilter implements Predicate<ActivateEvent> {
    /**
     * Applies the filter to a given ActivateEvent.
     *
     * @param event The ActivateEvent to apply the filter to
     * @return True only if the activated item is activated from the top
     */
    @Override
    public boolean apply(ActivateEvent event) {
        Side side = Side.inDirection(event.getHitNormal());
        return side == Side.TOP;
    }
}
