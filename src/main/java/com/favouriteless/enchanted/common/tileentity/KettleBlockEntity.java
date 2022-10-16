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
import com.favouriteless.enchanted.common.blocks.KettleBlock;
import com.favouriteless.enchanted.common.init.EnchantedBlockEntityTypes;
import com.favouriteless.enchanted.common.init.EnchantedParticles;
import com.favouriteless.enchanted.common.recipes.KettleRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.state.BlockState;

import java.util.stream.Collectors;

public class KettleBlockEntity extends CauldronBlockEntity<KettleRecipe> {

    public KettleBlockEntity(BlockPos pos, BlockState state) {
        super(EnchantedBlockEntityTypes.KETTLE.get(), pos, state, 1, 160);
    }

    @Override
    public double getWaterStartY() {
        return level.getBlockState(worldPosition).getValue(KettleBlock.TYPE) == 1 ? 0.1875D : 0.0625D;
    }

    @Override
    public double getWaterMaxHeight() {
        return 0.25D;
    }

    @Override
    public double getWaterWidth() {
        return 0.375D;
    }

    @Override
    public void handleCookParticles(long time) {
        if(Math.random() > 0.5D) {
            double dx = worldPosition.getX() + Math.random();
            double dy = worldPosition.getY() + Math.random();
            double dz = worldPosition.getZ() + Math.random();

            level.addParticle(new SimpleColouredData(EnchantedParticles.KETTLE_COOK.get(), getRed(time), getGreen(time), getBlue(time)), dx, dy, dz, 0D, 0D, 0D);
        }
    }

    @Override
    protected void matchRecipes() {
        if (level != null) {
            setPotentialRecipes(level.getRecipeManager()
                    .getRecipes()
                    .stream()
                    .filter(recipe -> recipe instanceof KettleRecipe)
                    .map(recipe -> (KettleRecipe)recipe)
                    .filter(recipe -> recipe.matches(this, level))
                    .collect(Collectors.toList()));
        }
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("container.enchanted.kettle");
    }
}
