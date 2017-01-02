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
package org.terasology.processing.system;

import com.google.common.base.Predicate;
import org.terasology.logic.common.ActivateEvent;
import org.terasology.math.Side;

/**
 * A filter that returns true only when an event is passed where the concerned item is activated from the top.
 */
public class UseOnTopFilter implements Predicate<ActivateEvent> {
    /**
     * Applies the filter to a given ActivateEvent.
     *
     * @param event The ActivateEvent to apply the filter to
     * @return      True only if the activated item is activated from the top
     */
    @Override
    public boolean apply(ActivateEvent event) {
        Side side = Side.inDirection(event.getHitNormal());
        return side == Side.TOP;
    }
}
