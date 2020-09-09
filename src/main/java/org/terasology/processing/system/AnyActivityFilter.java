// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.processing.system;

import com.google.common.base.Predicate;
import org.terasology.engine.logic.common.ActivateEvent;

/**
 * An activity filter that returns true for any passed ActivateEvent object.
 */
public class AnyActivityFilter implements Predicate<ActivateEvent> {
    /**
     * Applies the activity filter to a given ActivateEvent. This filter returns true for any ActivateEvent object.
     *
     * @param input The ActivateEvent ot apply the filter to
     * @return The result of applying the filter to the event. This filter returns true for any given event.
     */
    @Override
    public boolean apply(ActivateEvent input) {
        return true;
    }
}
