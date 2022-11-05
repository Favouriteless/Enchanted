/*
 *
 *   Copyright (c) 2022. Favouriteless
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

package com.favouriteless.enchanted.common.items;

import com.favouriteless.enchanted.Enchanted;
import com.favouriteless.enchanted.common.init.EnchantedBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class AnointingPasteItem extends Item {

    public AnointingPasteItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        BlockPos pos = pContext.getClickedPos();
        BlockState state = pContext.getLevel().getBlockState(pos);
        if(state.is(Blocks.CAULDRON)) {
            pContext.getLevel().setBlockAndUpdate(pos, EnchantedBlocks.WITCH_CAULDRON.get().defaultBlockState());
            pContext.getItemInHand().shrink(1);

            pContext.getLevel().playSound(pContext.getPlayer(), pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            spawnParticles(pContext.getLevel(), pos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    public static void spawnParticles(Level world, BlockPos pos) {
        for(int i = 0; i < 20; i++) {
            double x = Enchanted.RANDOM.nextDouble() * 2;
            double y = Enchanted.RANDOM.nextDouble() * 1.5D;
            double z = Enchanted.RANDOM.nextDouble() * 2;

            world.addParticle(ParticleTypes.WITCH, pos.getX()-0.5D + x, pos.getY() + y, pos.getZ()-0.5D + z, 0.0D, 0.0D, 0.0D);
        }
    }
}
