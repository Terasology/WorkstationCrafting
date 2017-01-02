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

/**
 * An activity filter that returns true for any passed ActivateEvent object.
 */
public class AnyActivityFilter implements Predicate<ActivateEvent> {
    /**
     * Applies the activity filter to a given ActivateEvent. This filter returns true for any ActivateEvent object.
     *
     * @param input The ActivateEvent ot apply the filter to
     * @return      The result of applying the filter to the event. This filter returns true for any given event.
     */
    @Override
    public boolean apply(ActivateEvent input) {
        return true;
    }
}
