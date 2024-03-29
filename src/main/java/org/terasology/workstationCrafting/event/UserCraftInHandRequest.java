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
package org.terasology.workstationCrafting.event;

import org.terasology.engine.network.ServerEvent;
import org.terasology.gestalt.entitysystem.event.Event;

import java.util.List;

@ServerEvent
public class UserCraftInHandRequest implements Event {
    private String recipeId;
    private List<String> parameters;
    private int count;

    public UserCraftInHandRequest() {
    }

    public UserCraftInHandRequest(String recipeId, List<String> parameters, int count) {
        this.recipeId = recipeId;
        this.parameters = parameters;
        this.count = count;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public int getCount() {
        return count;
    }
}
