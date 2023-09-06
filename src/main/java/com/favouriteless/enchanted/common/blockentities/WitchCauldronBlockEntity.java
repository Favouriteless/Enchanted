/*
 *
 *   Copyright (c) 2023. Favouriteless
 *   Enchanted, a minecraft mod.
 *   GNU GPLv3 License
 *
 *       This file is part of Enchanted.
 *
 *       Enchanted is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       Enchanted is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with Enchanted.  If not, see <https://www.gnu.org/licenses/>.
 *
 *
 */

package com.favouriteless.enchanted.common.blockentities;

import com.favouriteless.enchanted.client.particles.types.SimpleColouredParticleType.SimpleColouredData;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlockEntityTypes;
import com.favouriteless.enchanted.common.init.registry.EnchantedBlocks;
import com.favouriteless.enchanted.common.init.registry.EnchantedParticles;
import com.favouriteless.enchanted.common.init.registry.EnchantedRecipeTypes;
import com.favouriteless.enchanted.common.recipes.WitchCauldronRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.state.BlockState;

public class WitchCauldronBlockEntity extends CauldronBlockEntity<WitchCauldronRecipe> {

    public WitchCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(EnchantedBlockEntityTypes.WITCH_CAULDRON.get(), pos, state, 3, 200);
    }

    @Override
    public double getWaterStartY(BlockState state) {
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
        double dy = worldPosition.getY() + getWaterY(EnchantedBlocks.WITCH_CAULDRON.get().defaultBlockState());
        double dz = worldPosition.getZ() + 0.5D;

        level.addParticle(new SimpleColouredData(EnchantedParticles.CAULDRON_COOK.get(), getRed(time), getGreen(time), getBlue(time)), dx, dy, dz, 0.0D, 0.0D, 0.0D);
    }

    @Override
    protected void matchRecipes() {
        if (level != null)
            setPotentialRecipes(level.getRecipeManager()
                    .getRecipesFor(EnchantedRecipeTypes.WITCH_CAULDRON, this, level));
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("container.witch_cauldron");
    }
}
