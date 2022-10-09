/*
 * Copyright (c) 2022. Favouriteless
 * Enchanted, a minecraft mod.
 * GNU GPLv3 License
 *
 *     This file is part of Enchanted.
 *
 *     Enchanted is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Enchanted is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.favouriteless.enchanted.common.tileentity;

import com.favouriteless.enchanted.client.particles.SimpleColouredParticleType.SimpleColouredData;
import com.favouriteless.enchanted.common.init.EnchantedParticles;
import com.favouriteless.enchanted.common.init.EnchantedTileEntities;
import com.favouriteless.enchanted.common.recipes.WitchCauldronRecipe;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.stream.Collectors;

public class WitchCauldronTileEntity extends CauldronTileEntity<WitchCauldronRecipe> {

    public WitchCauldronTileEntity() {
        super(EnchantedTileEntities.WITCH_CAULDRON.get(), 3, 200);
    }

    @Override
    public double getWaterStartY() {
        return 0.1875D;
    }

    @Override
    public double getWaterMaxHeight() {
        return 0.4375D;
    }

    @Override
    public double getWaterWidth() {
        return 0.625D;
    }

    @Override
    public void handleCookParticles(long time) {
        double dx = worldPosition.getX() + 0.5D;
        double dy = worldPosition.getY() + getWaterY();
        double dz = worldPosition.getZ() + 0.5D;

        level.addParticle(new SimpleColouredData(EnchantedParticles.CAULDRON_COOK.get(), getRed(time), getGreen(time), getBlue(time)), dx, dy, dz, 0.0D, 0.0D, 0.0D);
    }

    @Override
    protected void matchRecipes() {
        if (level != null) {
            setPotentialRecipes(level.getRecipeManager()
                    .getRecipes()
                    .stream()
                    .filter(recipe -> recipe instanceof WitchCauldronRecipe)
                    .map(recipe -> (WitchCauldronRecipe)recipe)
                    .filter(recipe -> recipe.matches(this, level))
                    .collect(Collectors.toList()));
        }
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.witch_cauldron");
    }
}
