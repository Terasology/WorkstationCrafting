// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.workstationCrafting.system.recipe.hand;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.workstationCrafting.system.recipe.render.CraftProcessDisplay;

import java.util.List;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public interface CraftInHandRecipe {
    List<CraftInHandResult> getMatchingRecipeResults(EntityRef character);

    CraftInHandResult getResultByParameters(List<String> parameters);

    interface CraftInHandResult extends CraftProcessDisplay {
        List<String> getParameters();

        EntityRef craft(EntityRef character, int count);
    }
}
